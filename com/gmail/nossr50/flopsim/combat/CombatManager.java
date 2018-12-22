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
	private AbilityPriority currentPhase;
	private Turn currentTurn;


	public CombatManager(ArrayList<CombatEntity> currentCombatants, AbilityPriority currentPhase, Turn currentTurn) {
		this.currentPhase = currentPhase;
		this.currentTurn = currentTurn;
		this.currentCombatants = currentCombatants;
	}
	
//	public void executeCombatTurn()
//	{
//		/*
//		 * First apply pre-combat effects
//		 */
//
//		for(CombatEntity ce : combatants)
//		{
//			CombatTarget target = ce.getCombatTarget();
//
//			if(ce.hasAbility())
//			{
//				//ce.getAbility().addQueuedAbilityInteraction();
//			}
//		}
//	}
//
//	private void simAttacks()
//	{
//
//	}
	
	public void executeAbilitiesFiringNow()
	{
		triggerAbilities(); //Trigger abilities

	}

	private void triggerAbilities() {
		/**
		 * Apply ability interactions
		 */
		for (CombatEntity currentCombatant : currentCombatants) {
			//Check if the ability of the current combatant fires during this phase
			if(currentCombatant.hasAbility() && currentCombatant.getAbility().doesAbilityTriggerNow(currentPhase))
			{
				//Apply new AbilityInteraction(s) to all appropriate targets
				currentCombatant.getAbility().addAbilityInteraction(currentTurn);
			}
		}

		/**
		 * Execute the results of those interactions
		 */
		for (CombatEntity currentCombatant : currentCombatants) {

		}
	}


}
