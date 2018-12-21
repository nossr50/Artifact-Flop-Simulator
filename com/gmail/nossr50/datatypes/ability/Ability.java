package com.gmail.nossr50.datatypes.ability;

import java.util.ArrayList;

import com.gmail.nossr50.datatypes.entity.CombatEntity;
import com.gmail.nossr50.datatypes.entity.EntityStat;
import com.gmail.nossr50.datatypes.record.Turn;
import com.gmail.nossr50.flopsim.combat.AbilityInteraction;

public class Ability {
	
	public final HeroAbility heroAbility;
	public final CombatEntity source; //owner
	
	
	public Ability(CombatEntity source, HeroAbility at)
	{
		this.source 	= source;
		this.heroAbility 		= at;
	}
	
	public boolean isAura()
	{
		if(heroAbility.getModifierType() == AbilityModifierType.AURA_ARMOR
				|| heroAbility.getModifierType() == AbilityModifierType.AURA_ATTACK
				|| heroAbility.getModifierType() == AbilityModifierType.AURA_HEALTH
				|| heroAbility.getModifierType() == AbilityModifierType.AURA_REGEN)
			return true;
		else
			return false;
	}
	
	public int getValue()
	{
		//Greater cleave is rounded down
		if(heroAbility == HeroAbility.GREAT_CLEAVE)
		{
			//Java rounds down to zero automatically
			//Example 5 divided by 2 = 2.5, result will be 2
			return (source.getCurrentAttack() / 2);
		}
		
		//Blood bath fully heals
		if(heroAbility == HeroAbility.BLOOD_BATH)
			return source.getMaxHealth();
		
		return heroAbility.getValue();
	}
	
	public boolean doesAbilityTriggerNow(AbilityPriority ap)
	{
		return heroAbility.getPriority() == ap;
	}
	
	public void applyAbility(Turn turn)
	{
		ArrayList<CombatEntity> abilityTargets = source.getAbilityTargets();
		
		/*
		 * Add the interactions
		 */
		for(CombatEntity target : abilityTargets)
		{
			System.out.println("Adding ability interaction");
			AbilityInteraction newInteraction = new AbilityInteraction(source, target, heroAbility.getModifierType(), heroAbility.getAbilityType(), getValue(), turn);
			target.addAbilityInteraction(newInteraction);
		}
		
	}
	
	public boolean isTriggeredOnDefense()
	{
		if(heroAbility == HeroAbility.MOMENT_OF_COURAGE
				|| heroAbility == HeroAbility.CORROSIVE_SKIN
				|| heroAbility == HeroAbility.RETURN)
		{
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isRetaliate()
	{
		if(heroAbility == HeroAbility.MOMENT_OF_COURAGE || heroAbility == HeroAbility.RETURN)
		{
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isDefensiveDebuff()
	{
		if(heroAbility == HeroAbility.CORROSIVE_SKIN)
			return true;
		else
			return false;
	}
	
	public boolean isTriggeredOnAttack()
	{
		if(heroAbility == HeroAbility.FURY_SWIPES)
		{
			return true;
		} else {
			return false;
		}
	}
	
	private void triggerRetaliate(CombatEntity target)
	{
		target.addPendingStatChange(EntityStat.HEALTH, getValue());
		
		
	}
	
	public void triggerDefensiveSkill(CombatEntity target, Turn turn)
	{
		//NOTE: Retaliate can trigger defensive skills
		if(heroAbility == HeroAbility.MOMENT_OF_COURAGE || heroAbility == HeroAbility.RETURN)
		{
			triggerRetaliate(target);
		}
		
		if(heroAbility == HeroAbility.CORROSIVE_SKIN)
		{
			target.addPendingStatChange(EntityStat.ATTACK, getValue()); //Remove 1 attack
		}
		
		AbilityInteraction ai = new AbilityInteraction(source, target, heroAbility.getModifierType(), heroAbility.getAbilityType(), getValue(), turn);
		target.addAbilityInteraction(ai);
	}
	
	public void triggerOffensiveSkill(CombatEntity target, Turn turn)
	{
		if(heroAbility == HeroAbility.FURY_SWIPES)
		{
			target.addPendingStatChange(EntityStat.ARMOR, getValue());
		}
		
		AbilityInteraction ai = new AbilityInteraction(source, target, heroAbility.getModifierType(), heroAbility.getAbilityType(), getValue(), turn);
		target.addAbilityInteraction(ai);
	}
	
	public AbilityTargetType getAbilityTargetType() { return heroAbility.getTargetType(); }
}
