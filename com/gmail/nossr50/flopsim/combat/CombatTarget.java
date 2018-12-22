package com.gmail.nossr50.flopsim.combat;

import com.gmail.nossr50.datatypes.entity.CombatEntity;
import com.gmail.nossr50.datatypes.entity.LaneEntity;
import com.gmail.nossr50.datatypes.entity.LaneEntityType;
import org.jetbrains.annotations.NotNull;

public class CombatTarget {
	public final LaneEntityType let;
	@NotNull
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
	
	@NotNull
    public CombatEntity getTarget()
	{
		return (CombatEntity) target;
	}
	
	public boolean isTargetBlocking()
	{
		return (let != LaneEntityType.ARROW);
	}
}
