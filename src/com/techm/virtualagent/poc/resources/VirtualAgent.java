package com.techm.virtualagent.poc.resources;

import java.util.Properties;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.techm.virtualagent.poc.nlp.POSTagger;
import com.techm.virtualagent.poc.nlp.QueryBuilder;
import com.techm.virtualagent.poc.nlp.QueryBuilderException;
import com.techm.virtualagent.poc.nlp.opendomain.CustomSearch;
import com.techm.virtualagent.poc.nlp.opendomain.model.OpenDomainSearchResult;
import com.techm.virtualagent.poc.nlp.util.NlpUtility;
import com.techm.virtualagent.poc.nlp.vocabulary.DefaultVocab;
import com.techm.virtualagent.poc.nlp.vocabulary.IFileReader;
import com.techm.virtualagent.poc.nlp.vocabulary.IFileWriter;
import com.techm.virtualagent.poc.nlp.vocabulary.PropertyReader;
import com.techm.virtualagent.poc.nlp.vocabulary.PropertyWriter;
import com.techm.virtualagent.poc.utl.ThreadListener;

@Path("/askquestion")
public class VirtualAgent {
	private static String PROPERTY_FILE_PATH = "D:/vocab.properties";

	@GET
	public void askAgent(@Suspended final AsyncResponse asyncResponse,
			@Context ServletContext context,
			@QueryParam("question") String question) {
		updateRelativeUrl(context);
		getResponse(question, asyncResponse);
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/{question}")
	public void askAgent(@PathParam("question") String question,
			@Suspended final AsyncResponse asyncResponse,
			@Context ServletContext context) {
		updateRelativeUrl(context);
		getResponse(question, asyncResponse);
	}

	@POST
	@Produces("text/plain")
	public void askAgentPostMethod(String msg,
			@Suspended final AsyncResponse asyncResponse,
			@Context ServletContext context) {
		updateRelativeUrl(context);
		getResponse(msg, asyncResponse);
	}

	private void updateRelativeUrl(ServletContext context) {
		PROPERTY_FILE_PATH = context.getRealPath("/WEB-INF/vocab.properties");
		POSTagger.POSTAGGER_MODEL_FILE = context
				.getRealPath("/WEB-INF/en-pos-maxent.bin");
	}

	private void getResponse(final String question,
			@Suspended final AsyncResponse asyncResponse) {

		String formattedQuestion = NlpUtility.instance
				.gtFormattedString(question);
		System.out.println("Formatted qns: " + formattedQuestion);
		// get default vocabulary answer
		String defaultVocabAnswer = DefaultVocab
				.getFromDefaultVocabulary(formattedQuestion);
		if (defaultVocabAnswer == null) {
			String tagedQuestion = null;
			try {
				tagedQuestion = QueryBuilder.instance
						.getTaggedQuery(formattedQuestion);
				if (!(tagedQuestion.length() > 0)) {
					asyncResponse
							.resume("Not able to respond to your query. Please contact CSR.");
				} else {
					final String finalQuestion = tagedQuestion
							.replace(" ", "_").toLowerCase();
					System.out.println("finalqns: " + finalQuestion);
					IFileReader<Properties> propertyFileReader = new PropertyReader(
							PROPERTY_FILE_PATH);
					ThreadListener<Properties> listener = new ThreadListener<Properties>() {

						@Override
						public void didStart() {
						}

						@Override
						public void didError(Exception e) {
							System.out.println(e);
						}

						@Override
						public void didClose() {
						}

						@Override
						public void didSucceed(Properties data) {
							String val = data.getProperty(finalQuestion, "");
							onAnswerRecived(val, finalQuestion, asyncResponse);
							System.out.println(val);
						}
					};

					propertyFileReader.read(listener);
				}
				System.out.println("@@ " + tagedQuestion);
			} catch (QueryBuilderException e) {
				e.printStackTrace();
			}
		} else {
			asyncResponse.resume(defaultVocabAnswer);
		}
	}

	private void onAnswerRecived(String val, String qns,
			@Suspended final AsyncResponse asyncResponse) {
		if (val.trim().length() > 0) {
			asyncResponse.resume(val);
		} else {
			ThreadListener<OpenDomainSearchResult> threadListener = new ThreadListener<OpenDomainSearchResult>() {

				@Override
				public void didStart() {
					// TODO Auto-generated method stub

				}

				@Override
				public void didError(Exception e) {
					System.out.println(e);
				}

				@Override
				public void didClose() {
					// TODO Auto-generated method stub

				}

				@Override
				public void didSucceed(OpenDomainSearchResult data) {
					writeToPropertyFile(data, asyncResponse);
				}
			};
			qns = qns.replaceAll("_", " ");
			new CustomSearch().startSearch(qns, threadListener);
		}
	}

	private void writeToPropertyFile(final OpenDomainSearchResult data,
			@Suspended final AsyncResponse asyncResponse) {
		ThreadListener<Properties> threadListener = new ThreadListener<Properties>() {

			@Override
			public void didStart() {
				// TODO Auto-generated method stub

			}

			@Override
			public void didError(Exception e) {
				System.out.println(e);
			}

			@Override
			public void didClose() {
				// TODO Auto-generated method stub

			}

			@Override
			public void didSucceed(Properties prop) {
				if (prop instanceof Properties) {
					String result = data.getDescription()
							+ "\nFor more details visit: " + data.getUrl();
					System.out.println(result);
					String notSureText = "I am not sure about this, but here is my best suggestion...\n";
					asyncResponse.resume(notSureText + result);
				}
			}
		};
		Properties property = new Properties();
		property.setProperty(data.getKey().toLowerCase().replace(" ", "_"),
				data.getDescription() + "\n" + data.getUrl());
		IFileWriter<Properties> propFileWriter = new PropertyWriter(property,
				PROPERTY_FILE_PATH);
		propFileWriter.write(threadListener);
	}
}
