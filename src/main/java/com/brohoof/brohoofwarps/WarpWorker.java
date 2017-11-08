package com.brohoof.brohoofwarps;

import java.util.Optional;
import java.util.UUID;

public class WarpWorker<T> implements Runnable {
	private static Data d;
	private T t;
	private String warpName;
	private Object owner;
	private String param;

	static void setData(Data d) {
		if (WarpWorker.d != null)
			throw new UnsupportedOperationException("Cannot redefine data.");
		WarpWorker.d = d;
	}

	public WarpWorker(String param, String warpName, Object owner) {
		this.param = param;
		this.warpName = warpName;
		this.owner = owner;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void run() {
		switch (param) {
			case "Warp": {
				if (owner != null && owner instanceof String)
					t = (T) d.getWarp(warpName, (String) owner);
				else
					t = (T) Optional.<T>empty();
				return;
			}
			case "Warp[]": {
				t = (T) d.getWarps(warpName);
				return;
			}
			case "Sign": {
				if (owner != null && owner instanceof UUID) 
					t = (T) d.getSigns(warpName, (UUID) owner);
				else
				    t = (T) Optional.<T> empty();
			}
		}
		notifyAll();
	}

	public T get() throws InterruptedException {
		synchronized (this) {
			while (t == null)
				wait();
			return t;
		}
	}

}
