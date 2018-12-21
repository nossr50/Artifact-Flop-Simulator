package com.gmail.nossr50.datatypes.entity;

/*
 * Lane Entities are anything that can sit in a lane and occupy an entire position to themself, which includes arrows
 */
public class LaneEntity {
	public final LaneEntityType let;
	
	public LaneEntity(LaneEntityType let)
	{
		this.let = let;
	}
	
	public LaneEntityType getLaneEntityType() { return let; }
	
	public boolean isHero()  { return this instanceof HeroEntity; }
	public boolean isCreep() { return let == LaneEntityType.MELEE_CREEP; }
	public boolean isArrow() { return let == LaneEntityType.ARROW; }
	public boolean isTower() { return let == LaneEntityType.TOWER; }
	
	public void printType() {
		System.out.println(let.toString());
	}
}
