package com.brohoof.brohoofwarps;

public class InvalidWarpStateException extends Exception {

	private static final long serialVersionUID = 4350058833445105553L;
	
	public InvalidWarpStateException() {
		super("This warp state is invalid.");
	}
	public InvalidWarpStateException(String message) {
		super("This warp state is invalid: " + message);
	}
	public InvalidWarpStateException(String message, Throwable cause) {
		super("This warp state is invalid: " + message, cause);
	}
}
