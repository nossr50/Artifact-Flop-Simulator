package com.gmail.nossr50.datatypes.entity;

import com.gmail.nossr50.datatypes.ArrowType;

/*
 * Represents an Arrow
 */
public class Arrow extends LaneEntity {
	private final ArrowType at;
	
	public Arrow(LaneEntityType let, ArrowType at)
	{
		super(let);
		this.at = at;
	}
	
	public ArrowType getArrowDirection() { return at; }
	
	public String toASCIIArt() { return new String("["+"Arrow"+"]"); }
}
