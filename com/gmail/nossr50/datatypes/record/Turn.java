package com.gmail.nossr50.datatypes.record;

/**
 * This class is just to make the code more readable
 * @author nossr50
 *
 */
public enum Turn {
	ON_FLOP(0),
	TURN_AFTER_FLOP(1);
	
	final private int index;
	
	Turn (int index)
	{
		this.index = index;
	}
	
	public int getIndex() { return index; }
}
