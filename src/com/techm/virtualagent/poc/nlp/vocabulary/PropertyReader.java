package com.techm.virtualagent.poc.nlp.vocabulary;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.techm.virtualagent.poc.utl.ThreadListener;

public class PropertyReader implements IFileReader<Properties> {
	private String filePath;
	private ThreadListener<Properties> listener;
	private FileReader fileReader;

	public PropertyReader(String filePath) {
		System.out.println("property reader file name: " + filePath);
		this.filePath = filePath;
	}

	@Override
	public void read(ThreadListener<Properties> listener) {
		this.listener = listener;
		new Thread(new Writer()).start();
	}

	@Override
	public void stop() {
		if (fileReader != null) {
			try {
				fileReader.close();
			} catch (IOException e) {
				listener.didError(e);
			}
		}
	}

	private class Writer implements Runnable {

		@Override
		public void run() {
			listener.didStart();
			Properties p = new Properties();
			try {
				fileReader = new FileReader(filePath);
				p.load(fileReader);
				listener.didSucceed(p);
				listener.didClose();
			} catch (FileNotFoundException e) {
				listener.didError(e);
			} catch (IOException e) {
				listener.didError(e);
			}
		}

	}

}
