package com.gmail.nossr50.datatypes.lane;

import com.gmail.nossr50.datatypes.ability.HeroAbility;
import com.gmail.nossr50.datatypes.entity.CombatEntity;
import com.gmail.nossr50.datatypes.entity.EntityAlignment;
import com.gmail.nossr50.datatypes.entity.LaneEntityType;

public class Tower extends CombatEntity {
	
	int accumulatedDamage = 0;
	
	public Tower(int uniqueId, String entityName, int baseHealth, int baseArmor, int baseAttack, HeroAbility abilityType, EntityAlignment entityAlignment, LaneEntityType let)
	{
		super(uniqueId, entityName, baseHealth, baseArmor, baseAttack, abilityType, entityAlignment, let);
	}
	
}
