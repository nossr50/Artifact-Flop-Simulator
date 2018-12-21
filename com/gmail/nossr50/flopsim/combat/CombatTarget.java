package com.gmail.nossr50.flopsim.combat;

import com.gmail.nossr50.datatypes.entity.CombatEntity;
import com.gmail.nossr50.datatypes.entity.LaneEntity;
import com.gmail.nossr50.datatypes.entity.LaneEntityType;

public class CombatTarget {
	public final LaneEntityType let;
	private final LaneEntity target;
	
	public CombatTarget(LaneEntity le)
	{
		this.target 	= le;
		this.let 		= le.getLaneEntityType();
	}
	
	public boolean isTargetTower()
	{
		return (let == LaneEntityType.ARROW);
	}
	
	public CombatEntity getTarget()
	{
		return (CombatEntity) target;
	}
	
	public boolean isTargetBlocking()
	{
		return (let != LaneEntityType.ARROW);
	}
}
