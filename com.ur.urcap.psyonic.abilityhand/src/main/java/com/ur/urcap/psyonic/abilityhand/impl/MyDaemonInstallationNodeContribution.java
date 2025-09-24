package com.ur.urcap.psyonic.abilityhand.impl;

import com.ur.urcap.api.contribution.DaemonContribution;
import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.userinteraction.inputvalidation.InputValidationFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class MyDaemonInstallationNodeContribution implements InstallationNodeContribution {

	private static final String ENABLED_KEY = "enabled";
	public static final int PORT = 40405;

	private DataModel model;

	private final MyDaemonInstallationNodeView view;
	private final MyDaemonDaemonService daemonService;
	private XmlRpcMyDaemonInterface xmlRpcDaemonInterface;
	private static String XMLRPC_VARIABLE = "ah_daemon";
	private Timer uiTimer;
	private boolean pauseTimer = false;

	public MyDaemonInstallationNodeContribution(InstallationAPIProvider apiProvider, MyDaemonInstallationNodeView view, DataModel model, MyDaemonDaemonService daemonService, CreationContext context) {
		this.view = view;
		this.daemonService = daemonService;
		this.model = model;
		xmlRpcDaemonInterface = new XmlRpcMyDaemonInterface("127.0.0.1", PORT);
		applyDesiredDaemonStatus();
	}

	@Override
	public void openView() {

		//UI updates from non-GUI threads must use EventQueue.invokeLater (or SwingUtilities.invokeLater)
		uiTimer = new Timer(true);
		uiTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						if (!pauseTimer) {
							updateUI();
						}
					}
				});
			}
		}, 0, 1000);
	}

	@Override
	public void closeView() {
		if (uiTimer != null) {
			uiTimer.cancel();
		}
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		// Assign XMLRPC_VARIABLE
		writer.assign(XMLRPC_VARIABLE, "rpc_factory(\"xmlrpc\", \"http://127.0.0.1:" + PORT + "/RPC2\")");
	}

	private void updateUI() {
		DaemonContribution.State state = getDaemonState();

		if (state == DaemonContribution.State.RUNNING) {
			view.setStartButtonEnabled(false);
			view.setStopButtonEnabled(true);
		} else {
			view.setStartButtonEnabled(true);
			view.setStopButtonEnabled(false);
		}

		String text = "";
		switch (state) {
		case RUNNING:
			text = "Daemon running";
			break;
		case STOPPED:
			text = "Daemon Stopped";
			break;
		case ERROR:
			text = "Daemon failed";
			break;
		}

		view.setStatusLabel(text);
	}

	public void onStartClick() {
		model.set(ENABLED_KEY, true);
		applyDesiredDaemonStatus();
	}

	public void onStopClick() {
		model.set(ENABLED_KEY, false);
		applyDesiredDaemonStatus();
	}

	private void applyDesiredDaemonStatus() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (isDaemonEnabled()) {
					// Download the daemon settings to the daemon process on initial start for real-time preview purposes
					try {
						pauseTimer = true;
						awaitDaemonRunning(5000);
					} catch(Exception e){
						System.err.println("Could not set the title in the daemon process.");
					} finally {
						pauseTimer = false;
					}
				} else {
					daemonService.getDaemon().stop();
				}
			}
		}).start();
	}

	private void awaitDaemonRunning(long timeOutMilliSeconds) throws InterruptedException {
		daemonService.getDaemon().start();
		long endTime = System.nanoTime() + timeOutMilliSeconds * 1000L * 1000L;
		while(System.nanoTime() < endTime && (daemonService.getDaemon().getState() != DaemonContribution.State.RUNNING || !xmlRpcDaemonInterface.isReachable())) {
			Thread.sleep(100);
		}
	}

	private DaemonContribution.State getDaemonState() {
		return daemonService.getDaemon().getState();
	}

	private Boolean isDaemonEnabled() {
		return model.get(ENABLED_KEY, true); //This daemon is enabled by default
	}
	
	public String getXMLRPCVariable(){
		return XMLRPC_VARIABLE;
	}
	
	public XmlRpcMyDaemonInterface getXmlRpcDaemonInterface() {
		return xmlRpcDaemonInterface;
	}
}
