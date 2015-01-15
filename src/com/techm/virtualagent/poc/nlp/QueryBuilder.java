package com.techm.virtualagent.poc.nlp;

import java.io.IOException;

import com.techm.virtualagent.poc.nlp.POSTagger.PosTagData;

public enum QueryBuilder {

	instance;

	public String getTaggedQuery(String msg) throws QueryBuilderException {
		StringBuilder query = null;
		try {
			PosTagData posTagData = POSTagger.getInstance().POSTag(msg);
			String[] tags = posTagData.getTags();
			String[] tokens = posTagData.getToken();
			query = new StringBuilder();
			int i = 0;
			// TODO: need to change the logic for other tags
			for (String tag : tags) {
				switch (tag) {
				case "NN": // Singular noun
					setData(tokens[i], query);
					break;
				/*case "PRP":// Personal pronoun
					setData(tokens[i], query);
					break;*/
				case "PRP$":// Possesive Personal pronoun
					setData(tokens[i], query);
					break;
				case "NNS":// Plural noun
					setData(tokens[i], query);
					break;
				case "UH": // Interjection
					setData(tokens[i], query);
					break;
				case "CC": // Coordinating conjunction
					setData(tokens[i], query);
					break;
				case "NNP": // Proper singular noun
					setData(tokens[i], query);
					break;
				case "NNPS": // Proper plural noun
					setData(tokens[i], query);
					break;
				case "CD": // Cardinal number
					setData(tokens[i], query);
					break;
				case "VBG": // Verb, gerund/present participle
					setData(tokens[i], query);
					break;
				case "VB": // Verb, gerund/present participle
					setData(tokens[i], query);
					break;
				case "VBN": // Verb, gerund/present participle
					setData(tokens[i], query);
					break;
				case "JJ": // Verb, gerund/present participle
					setData(tokens[i], query);
					break;
				}
				i++;
			}
		} catch (IOException e) {
			throw new QueryBuilderException(
					"Query not formed due to pos model not found");
		} catch (NlpException e) {
			throw new QueryBuilderException(
					"Query not formed due to few missing taged");
		}

		return query.toString().trim();
	}

	private void setData(String msg, StringBuilder query) {
		query.append(msg);
		query.append(" ");
	}
}
