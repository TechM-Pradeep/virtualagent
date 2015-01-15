package com.techm.virtualagent.poc.nlp.opendomain;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.techm.virtualagent.poc.nlp.opendomain.model.OpenDomainSearchResult;
import com.techm.virtualagent.poc.utl.ThreadListener;
import com.techm.virtualagent.poc.utl.UrlCreator;

public class CustomSearch {
	public static final String CUSTOM_SEARCH_API_KEY = "AIzaSyDFCuIrCeaHNaN18BomP1mHKHAWT4Rdn5w";
	public static final String CUSTOM_SEARCH_ENGINE_KEY = "013208444580292702645:7t27v9dldyw";

	private ThreadListener<OpenDomainSearchResult> listener;
	private String searchQuery;

	public void startSearch(String searchString,
			ThreadListener<OpenDomainSearchResult> listener) {
		this.listener = listener;
		searchQuery = searchString;
		read(UrlCreator.INSTANCE.makeSearchString(searchString + " att", 1, 1));
	}

	private void read(String pUrl) {
		try {
			URL url = new URL(pUrl);
			System.out.println("URL is " + url);
			this.listener.didStart();
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
					"10.254.40.57", Integer.parseInt("80")));
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection(proxy);
			connection.setDoOutput(true);
			connection.setConnectTimeout(50000);
			connection.setReadTimeout(50000);
			System.out.println("Checking the webservice call");
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
				String line;
				StringBuffer buffer = new StringBuffer();
				while ((line = br.readLine()) != null) {
					buffer.append(line);
				}
				parseJson(buffer.toString());
				this.listener.didClose();
			} else {
				System.out.println("response not correct");
				this.listener.didError(new Exception("Resonse code: "
						+ connection.getResponseCode()));
			}
		} catch (Exception e) {
			this.listener.didError(e);
		}
	}

	// TODO: This logic needs to be separated out in different class
	@SuppressWarnings("unchecked")
	private void parseJson(String json) {
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = null;
		try {
			jsonObject = (JSONObject) jsonParser.parse(json);
			JSONArray items = (JSONArray) jsonObject.get("items");
			Iterator<JSONObject> i = items.iterator();
			OpenDomainSearchResult openDomainSearchResult = new OpenDomainSearchResult();
			while (i.hasNext()) {
				JSONObject innerObj = i.next();
				String snippet = ((String) innerObj.get("snippet")).replaceAll(
						"\n", " ").replace("...", "");
				openDomainSearchResult.setDescription(snippet);

				String url = (String) innerObj.get("formattedUrl");
				openDomainSearchResult.setUrl(url);
			}
			openDomainSearchResult.setKey(searchQuery);
			this.listener.didSucceed(openDomainSearchResult);
		} catch (ParseException e) {
			this.listener.didError(e);
		}

	}
}
