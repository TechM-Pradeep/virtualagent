package com.techm.virtualagent.poc.nlp;

import java.io.File;
import java.io.IOException;

import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;

public class POSTagger {
	private static POSTagger posTagger;
	private static POSModel model;
	public static String POSTAGGER_MODEL_FILE = "D:/work/poc/virtual_store_assistance/workspace-j2ee/virtualagent/en-pos-maxent.bin";

	private POSTagger() {
	}

	public static POSTagger getInstance() {
		if (posTagger == null) {
			posTagger = new POSTagger();
		}
		/*
		 * if (model == null) { // TODO: Need to change the file location logic
		 * model = new POSModelLoader() .load(new File(
		 * "D:/work/poc/virtual_store_assistance/workspace-j2ee/virtualagent/en-pos-maxent.bin"
		 * )); }
		 */
		return posTagger;
	}

	public PosTagData POSTag(String msg) throws IOException, NlpException {
		model = new POSModelLoader().load(new File(POSTAGGER_MODEL_FILE));

		// Initializing the performance monitor
		PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");

		/*
		 * Setting the model in the Part of speech tagger Tries to predict
		 * whether words are nouns, verbs, or any of 70 other POS tags
		 */
		POSTaggerME tagger = new POSTaggerME(model);

		// starting the performance monitor
		perfMon.start();

		PosTagData tagedData = null;
		// This tokenizer uses white spaces to tokenize the input text.
		String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
				.tokenize(msg);
		String[] tags = tagger.tag(whitespaceTokenizerLine);

		tagedData = new PosTagData();
		tagedData.setTags(tags);
		tagedData.setToken(whitespaceTokenizerLine);

		// Represents an pos-tagged sentence
		POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
		System.out.println(sample.toString());

		perfMon.incrementCounter();

		perfMon.stopAndPrintFinalResult();
		return tagedData;
	}

	public class PosTagData {
		private String[] tags;
		private String[] token;

		public String[] getTags() {
			return tags;
		}

		public void setTags(String[] tag) {
			this.tags = tag;
		}

		public String[] getToken() {
			return token;
		}

		public void setToken(String[] token) {
			this.token = token;
		}
	}
}
