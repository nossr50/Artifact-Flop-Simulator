package com.gmail.nossr50.datatypes.entity;

import com.gmail.nossr50.datatypes.ArrowType;
import org.jetbrains.annotations.NotNull;

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
	
	@NotNull
	public String toASCIIArt()
	{ 
		String arrowPrefix = "?";
		switch(getArrowDirection())
		{
		case LEFT:
			//arrowPrefix = "\u2190";
			arrowPrefix = "<";
			break;
		case RIGHT:
			//arrowPrefix = "\u2192";
			arrowPrefix = ">";
			break;
		case STRAIGHT:
			//arrowPrefix = "\u2191";
			arrowPrefix = "^";
			break;
		default:
			break;
		
		}
		return "[" + arrowPrefix + "Arw" + arrowPrefix + "]";
	}
}
