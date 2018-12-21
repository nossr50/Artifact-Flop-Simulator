package com.gmail.nossr50.flopsim.combat;

import com.gmail.nossr50.datatypes.ability.AbilityModifierType;
import com.gmail.nossr50.datatypes.ability.AbilityType;
import com.gmail.nossr50.datatypes.entity.CombatEntity;
import com.gmail.nossr50.datatypes.record.Turn;

public class AbilityInteraction {
	public CombatEntity source; //Source of the interaction (usually the one that dealt this damage)
	public CombatEntity recipient; //The one on the receiving end of this interaction
	public AbilityType at;
	public AbilityModifierType amt;
	public Turn turn;
	public int abilityValue;
	
	/**
	 * 
	 * @param source The source of this ability
	 * @param recipient The target of this ability
	 * @param amt The type of ability interaction
	 */
	public AbilityInteraction(CombatEntity source, CombatEntity recipient, AbilityModifierType amt, AbilityType at, int abilityValue, Turn turn)
	{
		this.source 		= source; //Source
		this.recipient 		= recipient; //Target
		this.at 			= at; //The type of interaction (special or normal)
		this.amt 			= amt; //The method of interaction
		this.turn 			= turn; //Which turn this happened on
		this.abilityValue 	= abilityValue;
	}

}
