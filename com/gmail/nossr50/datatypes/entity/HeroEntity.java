package com.gmail.nossr50.datatypes.entity;

import com.gmail.nossr50.datatypes.ability.HeroAbility;

public class HeroEntity extends CombatEntity {

	public HeroEntity(int uniqueId, String entityName, int baseHealth, int baseArmor, int baseAttack,
			HeroAbility heroAbility, EntityAlignment entityAlignment, LaneEntityType let) {
		super(uniqueId, entityName, baseHealth, baseArmor, baseAttack, heroAbility, entityAlignment, let);
	}

}
