package com.techm.virtualagent.poc.nlp.util;

public enum NlpUtility {
	instance;

	public String gtFormattedString(String msg) {
		// TODO: need to add more formatting
		String formattedMsg = msg.replace(".", " .").replace("?", "");
		return formattedMsg.trim().toLowerCase();
	}
}
