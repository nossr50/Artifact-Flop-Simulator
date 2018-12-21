package com.gmail.nossr50.datatypes.ability;

import static com.gmail.nossr50.datatypes.ability.AbilityModifierType.AURA_ARMOR;
import static com.gmail.nossr50.datatypes.ability.AbilityModifierType.AURA_ATTACK;
import static com.gmail.nossr50.datatypes.ability.AbilityModifierType.AURA_REGEN;
import static com.gmail.nossr50.datatypes.ability.AbilityModifierType.CLEAVE;
import static com.gmail.nossr50.datatypes.ability.AbilityModifierType.MODIFY_ARMOR;
import static com.gmail.nossr50.datatypes.ability.AbilityModifierType.MODIFY_ATTACK;
import static com.gmail.nossr50.datatypes.ability.AbilityModifierType.MODIFY_HEALTH;
import static com.gmail.nossr50.datatypes.ability.AbilityModifierType.PIERCE_DAMAGE;
import static com.gmail.nossr50.datatypes.ability.AbilityModifierType.REGEN;
import static com.gmail.nossr50.datatypes.ability.AbilityModifierType.RETALIATE;
import static com.gmail.nossr50.datatypes.ability.AbilityPriority.AFTER_COMBAT;
import static com.gmail.nossr50.datatypes.ability.AbilityPriority.DAMAGE_DEALT;
import static com.gmail.nossr50.datatypes.ability.AbilityPriority.END_OF_COMBAT;
import static com.gmail.nossr50.datatypes.ability.AbilityPriority.ON_DEPLOY;
import static com.gmail.nossr50.datatypes.ability.AbilityPriority.PRE_ACTION;
import static com.gmail.nossr50.datatypes.ability.AbilityPriority.PRE_COMBAT;
import static com.gmail.nossr50.datatypes.ability.AbilityTargetType.ALLIED_NEIGHBORS;
import static com.gmail.nossr50.datatypes.ability.AbilityTargetType.ALLIED_NEIGHBORS_AND_SELF;
import static com.gmail.nossr50.datatypes.ability.AbilityTargetType.BLOCKER;
import static com.gmail.nossr50.datatypes.ability.AbilityTargetType.COMBAT_TARGET_ALL;
import static com.gmail.nossr50.datatypes.ability.AbilityTargetType.COMBAT_TARGET_HERO;
import static com.gmail.nossr50.datatypes.ability.AbilityTargetType.COMBAT_TARGET_HERO_OR_TOWER;
import static com.gmail.nossr50.datatypes.ability.AbilityTargetType.COMBAT_TARGET_NEIGHBORS;
import static com.gmail.nossr50.datatypes.ability.AbilityTargetType.COMBAT_TARGET_TOWER;
import static com.gmail.nossr50.datatypes.ability.AbilityTargetType.ENEMY_NEIGHBORS;
import static com.gmail.nossr50.datatypes.ability.AbilityTargetType.GLOBAL_ALLIES;
import static com.gmail.nossr50.datatypes.ability.AbilityTargetType.RANDOM_ENEMY;
import static com.gmail.nossr50.datatypes.ability.AbilityTargetType.SELF;
import static com.gmail.nossr50.datatypes.ability.AbilityType.NORMAL;
import static com.gmail.nossr50.datatypes.ability.AbilityType.SPECIAL;

public enum HeroAbility {
	MISSING_ABILITY(null, null, null, null, 0), //Default
	BARROOM_BRAWLER(NORMAL, SELF, MODIFY_ARMOR, AFTER_COMBAT, 2), //BRISTLE
	RETURN(NORMAL, BLOCKER, RETALIATE, DAMAGE_DEALT,  2), //CENTAUR
	MOMENT_OF_COURAGE(NORMAL, BLOCKER, RETALIATE, DAMAGE_DEALT, 2), //LC
	GREAT_CLEAVE(NORMAL, COMBAT_TARGET_NEIGHBORS, CLEAVE, DAMAGE_DEALT, 0), //SVEN
	REACTIVE_ARMOR(NORMAL, SELF, AURA_ARMOR, PRE_COMBAT, 1), //TIMBER
	FURY_SWIPES(NORMAL, COMBAT_TARGET_ALL, MODIFY_ARMOR, DAMAGE_DEALT, -1), //URSA
	LUCENT_BEAM(SPECIAL, RANDOM_ENEMY, PIERCE_DAMAGE, PRE_ACTION, 1), //LUNA
	BLOOD_BATH(NORMAL, SELF, REGEN, AFTER_COMBAT, 0), //BloodCyka
	JINADA(NORMAL, SELF, AURA_ATTACK, PRE_ACTION, 4), //BountyHunter
	METICULOUS_PLANNER(NORMAL, COMBAT_TARGET_HERO_OR_TOWER, AURA_ATTACK, DAMAGE_DEALT, 2), //Debbie
	SADIST(NORMAL, ENEMY_NEIGHBORS, MODIFY_HEALTH, AFTER_COMBAT, 1), //Necro
	EFFICIENT_KILLER(NORMAL, COMBAT_TARGET_HERO, AURA_ATTACK, DAMAGE_DEALT, 4), //PA
	WARMONGER(NORMAL, COMBAT_TARGET_TOWER, AURA_ATTACK, DAMAGE_DEALT, 4), //Sorla
	PRECISION_AURA(NORMAL, GLOBAL_ALLIES, AURA_ATTACK, ON_DEPLOY, 1), //Drow
	NATURES_ATTENDANTS(NORMAL, ALLIED_NEIGHBORS_AND_SELF, AURA_REGEN, END_OF_COMBAT, 2), //Enchantress
	PACK_LEADERSHIP(NORMAL, ALLIED_NEIGHBORS, AURA_ARMOR, PRE_COMBAT, 1), //Farvhan
	FERAL_IMPULSE(NORMAL, ALLIED_NEIGHBORS, AURA_ATTACK, PRE_COMBAT, 2), //Lycan
	CORROSIVE_SKIN(NORMAL, BLOCKER, MODIFY_ATTACK, DAMAGE_DEALT, -1), //Viper
	BRANCHES_OF_IRON(NORMAL, ALLIED_NEIGHBORS, AURA_ARMOR, PRE_COMBAT, 2); //Treant
	
	private final AbilityType at;
	private final AbilityTargetType att;
	private final AbilityModifierType amt;
	private final AbilityPriority ap;
	private final int value;
	
	private HeroAbility(AbilityType at, AbilityTargetType att, AbilityModifierType amt, AbilityPriority ap, int value)
	{
		this.at 		= at;
		this.att 		= att;
		this.amt 		= amt;
		this.value 		= value;
		this.ap  		= ap;
	}
	
	public AbilityType getAbilityType() { return at; }
	public AbilityTargetType getTargetType() { return att; }
	public AbilityModifierType getModifierType() { return amt; }
	public AbilityPriority getPriority() { return ap; }
	public boolean isAbilitySpecial() { return at == AbilityType.SPECIAL; }
	public int getValue() { return value; }
}
