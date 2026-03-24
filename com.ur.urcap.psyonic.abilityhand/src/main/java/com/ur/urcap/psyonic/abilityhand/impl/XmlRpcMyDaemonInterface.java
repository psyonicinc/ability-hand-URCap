package com.ur.urcap.psyonic.abilityhand.impl;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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

	public boolean setPosition(List<Double> cmd) {
		return processBoolean(client.execute("setPosition", Collections.singletonList(cmd)));
	}

	public boolean setTorque(List<Double> cmd) {
		return processBoolean(client.execute("setTorque", Collections.singletonList(cmd)));
	}

	public boolean setDuty(List<Double> cmd) {
		return processBoolean(client.execute("setDuty", Collections.singletonList(cmd)));
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
