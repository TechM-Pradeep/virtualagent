package com.techm.virtualagent.poc.nlp.vocabulary;

import com.techm.virtualagent.poc.utl.ThreadListener;

public interface IFileReader <T> {
	public void read(ThreadListener<T> listener);
	public void stop();
}
