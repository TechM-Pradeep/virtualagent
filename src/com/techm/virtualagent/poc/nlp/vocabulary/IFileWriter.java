package com.techm.virtualagent.poc.nlp.vocabulary;

import com.techm.virtualagent.poc.utl.ThreadListener;

public interface IFileWriter<T> {
	public void write(ThreadListener<T> listener);
	public void stop();
}
