package com.ur.urcap.psyonic.abilityhand.torquecontrol.impl;

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

import java.awt.EventQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TorqueControlDaemonInstallationNodeContribution implements InstallationNodeContribution {
	private static final String POPUP_TITLE_KEY = "popuptitle";
	private static final String XMLRPC_VARIABLE = "TC_daemon";
	private static final String ENABLED_KEY = "enabled";
	private static final String DEFAULT_VALUE = "DefaultValue";
	private static final long DAEMON_TIME_OUT_NANO_SECONDS = TimeUnit.SECONDS.toNanos(20);
	private static final long RETRY_TIME_TO_WAIT_MILLI_SECONDS = TimeUnit.SECONDS.toMillis(1);

	private final TorqueControlDaemonInstallationNodeView view;
	private final TorqueControlDaemonService daemonService;
	private final InputValidationFactory inputValidationFactory;
	private final DataModel model;
	private final XmlRpcMyDaemonInterface daemonStatusMonitor;
	private final KeyboardInputFactory keyboardInputFactory;
	private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
	private ScheduledFuture<?> scheduleAtFixedRate;

	public TorqueControlDaemonInstallationNodeContribution(InstallationAPIProvider apiProvider,
												TorqueControlDaemonInstallationNodeView view,
												DataModel model,
												TorqueControlDaemonService daemonService,
												XmlRpcMyDaemonInterface xmlRpcMyDaemonInterface,
												CreationContext context) {
		keyboardInputFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();
		inputValidationFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getInputValidationFactory();
		this.view = view;
		this.model = model;
		this.daemonService = daemonService;
		this.daemonStatusMonitor = xmlRpcMyDaemonInterface;
		if (context.getNodeCreationType() == CreationContext.NodeCreationType.NEW) {
			model.set(POPUP_TITLE_KEY, DEFAULT_VALUE);
		}
		applyDesiredDaemonStatus();
	}

	@Override
	public void openView() {
		daemonStatusMonitor.startMonitorThread();

		//UI updates from non-GUI threads must use EventQueue.invokeLater (or SwingUtilities.invokeLater)
		Runnable updateUIRunnable = new Runnable() {
			@Override
			public void run() {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						updateUI();
					}
				});
			}
		};
		if (scheduleAtFixedRate != null) {
			scheduleAtFixedRate.cancel(true);
		}
		scheduleAtFixedRate = executorService.scheduleAtFixedRate(updateUIRunnable, 0, 1, TimeUnit.SECONDS);
	}

	@Override
	public void closeView() {
		if (scheduleAtFixedRate != null) {
			scheduleAtFixedRate.cancel(true);
		}
		daemonStatusMonitor.stopMonitorThread();
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		writer.assign(XMLRPC_VARIABLE, "rpc_factory(\"xmlrpc\", \"" + XmlRpcMyDaemonInterface.getDaemonUrl() + "\")");
		// Apply the settings to the daemon on program start in the Installation pre-amble
	}

	private void updateUI() {
		DaemonContribution.State state = getDaemonState();

	}

	private void applyDesiredDaemonStatus() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (TorqueControlDaemonInstallationNodeContribution.this.isDaemonEnabled()) {
					// Download the daemon settings to the daemon process on initial start for real-time preview purposes
					try {
						TorqueControlDaemonInstallationNodeContribution.this.awaitDaemonRunning();
						// daemonStatusMonitor.setTitle(TorqueControlDaemonInstallationNodeContribution.this.getPopupTitle());
					} catch (Exception e) {
						System.err.println("Could not set the title in the daemon process.");
						Thread.currentThread().interrupt();
					}
				} else {
					daemonService.getDaemon().stop();
				}
			}
		}).start();
	}

	private void awaitDaemonRunning() throws InterruptedException {
		daemonService.getDaemon().start();
		long endTime = System.nanoTime() + DAEMON_TIME_OUT_NANO_SECONDS;
		while (System.nanoTime() < endTime) {
			if (daemonStatusMonitor.isDaemonReachable()) {
				break;
			}
			Thread.sleep(RETRY_TIME_TO_WAIT_MILLI_SECONDS);
		}
	}


	private DaemonContribution.State getDaemonState() {
		return daemonStatusMonitor.isDaemonReachable() ? daemonService.getDaemon().getState() : DaemonContribution.State.STOPPED;
	}

	private Boolean isDaemonEnabled() {
		return model.get(ENABLED_KEY, true);
	}

	public String getXMLRPCVariable() {
		return XMLRPC_VARIABLE;
	}

	public XmlRpcMyDaemonInterface getDaemonStatusMonitor() {
		return daemonStatusMonitor;
	}
}
