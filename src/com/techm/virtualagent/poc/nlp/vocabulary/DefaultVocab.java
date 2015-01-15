package com.techm.virtualagent.poc.nlp.vocabulary;

import java.util.HashMap;

public class DefaultVocab {
	public static HashMap<String, String> defaultVocab = new HashMap<String, String>();
	static {
		defaultVocab.put("hi",
				"Welcome to at&t retail store. How can i help you?");
		defaultVocab.put("hello",
				"Welcome to at&t retail store. How can i help you?");
		defaultVocab.put("goodbye", "Have a good day");
		defaultVocab.put("good bye", "Have a good day");
	}

	public static String getFromDefaultVocabulary(String formattedQuestion) {
		String ans = defaultVocab.get(formattedQuestion);
		return ans;
	}

}
