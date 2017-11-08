package com.brohoof.brohoofwarps;

public enum Access {

	GLOBAL(0), PUBLIC(1), PRIVATE(2);

	private int level;

	private Access(int level) {
		this.level = level;
	}
	
	public int getLevel() {
		return level;
	}
	public static Access valueOf(int level){
		switch(level) {
			case 0:
				return GLOBAL;
			case 1:
				return PUBLIC;
			case 2:
				return PRIVATE;
		}
		return null;
	}
}
