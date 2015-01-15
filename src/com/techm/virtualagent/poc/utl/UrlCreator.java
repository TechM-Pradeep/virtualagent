package com.techm.virtualagent.poc.utl;

import com.techm.virtualagent.poc.nlp.opendomain.CustomSearch;

public enum UrlCreator {
	INSTANCE;

	private static final String searchURL = "https://www.googleapis.com/customsearch/v1?";

	public String makeSearchString(String qSearch, int start, int numOfResults) {
		String toSearch = searchURL + "key="
				+ CustomSearch.CUSTOM_SEARCH_API_KEY + "&cx="
				+ CustomSearch.CUSTOM_SEARCH_ENGINE_KEY + "&q=";

		// replace spaces in the search query with +
		String keys[] = qSearch.split("[ ]+");
		for (String key : keys) {
			toSearch += key + "+"; // append the keywords to the url
		}

		// specify response format as json
		toSearch += "&alt=json";

		// specify starting result number
		toSearch += "&start=" + start;

		// specify the number of results you need from the starting position
		toSearch += "&num=" + numOfResults;
		System.out.println("url: " + toSearch);
		return toSearch;
	}
}
