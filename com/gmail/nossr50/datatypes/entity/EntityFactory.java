package com.gmail.nossr50.datatypes.entity;

import com.gmail.nossr50.datatypes.ability.HeroAbility;
import com.gmail.nossr50.datatypes.lane.Tower;

public class EntityFactory {
	static int uniqueId = 0; //Unique ID for entities
	
	public static CombatEntity makeHeroCombatEntity(Hero hero, EntityAlignment ea)
	{
		HeroEntity newEntity = new HeroEntity(uniqueId, hero.toString(), hero.heroHealth, hero.heroArmor, hero.heroAttack, hero.at, ea, LaneEntityType.HERO);
		uniqueId++; //Increment unique ids
		return newEntity;
	}
	
	public static CombatEntity makeMeleeCreepCombatEntity(EntityAlignment ea)
	{
		CombatEntity newEntity = new CombatEntity(uniqueId, "Melee Creep", 4, 0, 2, HeroAbility.MISSING_ABILITY, ea, LaneEntityType.MELEE_CREEP);
		uniqueId++; //Increment unique ids
		return newEntity;
	}
	
	public static Tower makeTower(EntityAlignment ea)
	{
		Tower newEntity = new Tower(uniqueId, ea.toString()+" Tower", 40, 0, 0, HeroAbility.MISSING_ABILITY, ea, LaneEntityType.TOWER);
		uniqueId++; //Increment unique ids
		return newEntity;
	}
}
