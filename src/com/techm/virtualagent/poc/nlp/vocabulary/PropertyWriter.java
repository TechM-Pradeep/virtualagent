package com.techm.virtualagent.poc.nlp.vocabulary;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.techm.virtualagent.poc.utl.ThreadListener;

public class PropertyWriter implements IFileWriter<Properties> {
	private Properties properties;
	private ThreadListener<Properties> listener;
	private String fileName;

	public PropertyWriter(Properties properties, String fileName) {
		this.properties = properties;
		System.out.println("property writer file name: "+ fileName);
		this.fileName = fileName;
	}

	@Override
	public void write(ThreadListener<Properties> listener) {
		this.listener = listener;
		new Thread(new Writer()).start();
	}

	private class Writer implements Runnable {

		@Override
		public void run() {
			listener.didStart();
			try {
				FileInputStream in = new FileInputStream(fileName);
				properties.load(in);
				in.close();

				FileOutputStream out = new FileOutputStream(fileName);
				properties.store(out, null);
				out.close();

				listener.didSucceed(properties);
				listener.didClose();
			} catch (IOException e) {
				listener.didError(e);
			}
		}

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}
}
