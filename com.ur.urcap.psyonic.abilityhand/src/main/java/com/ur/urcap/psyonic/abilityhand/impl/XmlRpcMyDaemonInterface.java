package com.ur.urcap.psyonic.abilityhand.impl;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class XmlRpcMyDaemonInterface {

	private static final XmlRpcClient client = new XmlRpcClient();

	public XmlRpcMyDaemonInterface(String host, int port) {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setEnabledForExtensions(true);

		try {
			config.setServerURL(new URL("http://" + host + ":" + port + "/RPC2"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		config.setConnectionTimeout(10000); //10s
		config.setReplyTimeout(10000); //10s ... used to be 60s

		client.setConfig(config);
	}

	public boolean isReachable() {
		try {
			client.execute("get_title", new ArrayList<String>());
			return true;
		} catch (XmlRpcException e) {
			return false;
		}
	}

	public String getTitle() throws XmlRpcException, UnknownResponseException {
		Object result = client.execute("get_title", new ArrayList<String>());
		return processString(result);
	}

	public String setTitle(String title) throws XmlRpcException, UnknownResponseException {
		ArrayList<String> args = new ArrayList<String>();
		args.add(title);
		Object result = client.execute("set_title", args);
		return processString(result);
	}

	public String getMessage(String name) throws XmlRpcException, UnknownResponseException {
		ArrayList<String> args = new ArrayList<String>();
		args.add(name);
		Object result = client.execute("get_message", args);
		return processString(result);
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
