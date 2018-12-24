package com.gmail.nossr50.flopsim.combat;

import java.util.ArrayList;

import com.gmail.nossr50.datatypes.ability.AbilityPriority;
import com.gmail.nossr50.datatypes.entity.CombatEntity;
import com.gmail.nossr50.datatypes.record.Turn;

/**
 * Helper class for dealing with combat and recording interactions
 * @author nossr50
 *
 */
public class CombatManager {

	private ArrayList<CombatEntity> currentCombatants;
	private Turn currentTurn;


	public CombatManager(ArrayList<CombatEntity> currentCombatants, AbilityPriority currentPhase, Turn currentTurn) {
		this.currentTurn = currentTurn;
		this.currentCombatants = currentCombatants;
	}
	
	private void executeCombatTurn()
	{
		//First activate lane buffs
		triggerAbilities(AbilityPriority.LANE_BUFF); //Buffs from just being in the lane
		//TODO: Trigger conditional lane buffs here?
		//triggerAbilities(AbilityPriority.CONDITIONAL_COMBAT_TARGET_BUFF); //Buffs from attacking specific units

		//TODO: Trigger abilities that activate while attacking (cleave)
		//Deal melee damage
		for(CombatEntity currentAttacker : currentCombatants)
		{
			CombatEntity targetEntity = currentAttacker.getCombatTarget().getTarget(); //This is the unit that will be taking direct damage from our current entity
			targetEntity.dealMeleeCombatDamage(currentAttacker, currentTurn); //Deal melee damage
			//TODO: Fury swipes isn't firing correctly
		}

		/*
		Go through the final phases of combat (stat modification, regen, and death)
		 */
		for(CombatEntity currentCombatant : currentCombatants)
		{
			triggerAbilities(currentPhase); //Trigger abilities
			currentCombatant.applyRegen(); //This is applied after damage has been taken but before death is checked for
			updateEntities(); //Always fire dead last
		}

	}

	private void updateEntities()
	{
		for(CombatEntity currentCombatant : currentCombatants) {
			currentCombatant.updateStats(); //Changes any stats which may have been altered from ability interactions or direct combat damage
			currentCombatant.deathCheck(); //See if the entity died, and if it died flag it as dead
		}
	}
	
	private void executeAbilitiesFiringNow()
	{
		triggerAbilities(currentPhase); //Trigger abilities
		updateEntities(); //Update stats and check for deaths
	}

	private void triggerAbilities(AbilityPriority currentPhase) {
		/**
		 * Apply ability interactions
		 */
		for (CombatEntity currentCombatant : currentCombatants) {
			//Check if the ability of the current combatant fires during this phase
			if(currentCombatant.hasAbility() && currentCombatant.getAbility().doesAbilityTriggerNow(currentPhase))
			{
				System.out.println(currentCombatant.toString() +" has ability firing now!");
				//Apply new AbilityInteraction(s) to all appropriate targets
				currentCombatant.getAbility().addAbilityInteraction(currentTurn);
			}
		}
	}
}
