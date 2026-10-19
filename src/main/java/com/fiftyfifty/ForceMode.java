package com.fiftyfifty;

/** What the next dropper shot is allowed to be. */
public enum ForceMode {
	/** Fair coin flip: water bucket or lava bucket, exactly 50/50. */
	RANDOM,
	/** Always pull the water bucket out. */
	WATER,
	/** Always pull the lava bucket out. */
	LAVA;

	public static ForceMode byId(int id) {
		ForceMode[] values = values();
		return id >= 0 && id < values.length ? values[id] : RANDOM;
	}
}
