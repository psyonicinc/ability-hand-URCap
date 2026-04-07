package com.ur.urcap.psyonic.abilityhand.impl;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Collections;

public class XmlRpcMyDaemonInterface {
	private static final int PORT = 40405;
	private static final String HOST_IP = "127.0.0.1";
	private static final XmlRpcClient client = new XmlRpcClient();
	private static final XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();

	private final AtomicBoolean isDaemonReachable = new AtomicBoolean(false);
	private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
	private ScheduledFuture<?> scheduleAtFixedRate;

	public XmlRpcMyDaemonInterface() {
		setupXmlRpcClient();
		startMonitorThread();
	}

	public static String getDaemonUrl() {
		return "http://" + HOST_IP + ":" + PORT + "/RPC2";
	}

	private static void setupXmlRpcClient() {
		try {
			config.setEnabledForExtensions(true);
			config.setServerURL(new URL(getDaemonUrl()));
			config.setConnectionTimeout(10000); //10s
			config.setReplyTimeout(10000); //10s ... used to be 60s

			client.setConfig(config);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public void startMonitorThread() {
		Runnable containerMonitorRunnable = new Runnable() {
			@Override
			public void run() {
				isDaemonReachable.set(XmlRpcMyDaemonInterface.this.tryExecuteIsReachable());
			}
		};

		stopMonitorThread();
		scheduleAtFixedRate = executorService.scheduleWithFixedDelay(containerMonitorRunnable, 0, 1, TimeUnit.SECONDS);
	}

	private boolean tryExecuteIsReachable() {
		try {
			return (Boolean) client.execute("isReachable", new ArrayList<String>());
		} catch (XmlRpcException ignored) {
			return false;
		}
	}

	public void stopMonitorThread() {
		if (scheduleAtFixedRate != null) {
			scheduleAtFixedRate.cancel(true);
		}
	}

	public boolean isDaemonReachable() {
		return isDaemonReachable.get();
	}

	public boolean startPositionThread() {
		try {
		return processBoolean(client.execute("startPositionThread", new Object[]{}));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean startGripThread() {
		try {
		return processBoolean(client.execute("startGripThread", new Object[]{}));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean stopPositionThread() {
		try {
		return processBoolean(client.execute("stopPositionThread", new Object[]{}));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean stopGripThread() {
		try {
		return processBoolean(client.execute("stopGripThread", new Object[]{}));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean setPosition(List<Double> cmd) {
		try {
			cmd.set(5, -cmd.get(5));
			return processBoolean(client.execute("setPosition", Collections.singletonList(cmd)));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean setGrip(int grip, int speed) {
		try {
			return processBoolean(client.execute("setGrip", new Object[]{grip, speed}));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean setTorque(List<Double> cmd) {
		try {
			cmd.set(0, 0.1 * cmd.get(0));
			cmd.set(1, 0.1 * cmd.get(1));
			cmd.set(2, 0.1 * cmd.get(2));
			cmd.set(3, 0.1 * cmd.get(3));
			cmd.set(4, 0.1 * cmd.get(4));
			cmd.set(5, -0.1 * cmd.get(5));
			return processBoolean(client.execute("setTorque", Collections.singletonList(cmd)));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean setDuty(List<Double> cmd) {
		try {
			cmd.set(5, -cmd.get(5));
			return processBoolean(client.execute("setDuty", Collections.singletonList(cmd)));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean processBoolean(Object response) throws UnknownResponseException {
		if (response instanceof Boolean) {
			Boolean val = (Boolean) response;
			return val.booleanValue();
		} else {
			throw new UnknownResponseException();
		}
	}

	private String processString(Object response) throws UnknownResponseException {
		if (response instanceof String) {
			return (String) response;
		} else {
			throw new UnknownResponseException();
		}
	}
}
