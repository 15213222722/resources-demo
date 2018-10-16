package com.resourses.tool.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class NamedTask<T> extends FutureTask<T> implements Nameable {
	final String cName;

	public NamedTask(Runnable runnable, T result) {
		super(runnable, result);
		if (runnable instanceof Nameable) {
			this.cName = ((Nameable) runnable).getName();
		} else {
			this.cName = runnable.getClass().getName();
		}
	}

	public NamedTask(Callable<T> callable) {
		super(callable);
		if (callable instanceof Nameable) {
			this.cName = ((Nameable) callable).getName();
		} else {
			this.cName = callable.getClass().getName();
		}
	}

	public String getName() {
		return this.cName;
	}
	
}
