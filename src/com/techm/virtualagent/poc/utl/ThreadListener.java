package com.techm.virtualagent.poc.utl;

public abstract class ThreadListener<T> {

	public abstract void didStart();

	public abstract void didError(Exception e);

	public abstract void didClose();

	public abstract void didSucceed(T data);
}
