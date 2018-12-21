package com.gmail.nossr50.flopsim.combat;

import java.util.ArrayList;

import com.gmail.nossr50.datatypes.ability.AbilityPriority;
import com.gmail.nossr50.datatypes.entity.CombatEntity;

/**
 * Helper class for dealing with combat and recording interactions
 * @author nossr50
 *
 */
public class CombatManager {

	private ArrayList<CombatEntity> combatants;
	private AbilityPriority currentPhase;
	
	public CombatManager()
	{
		//this.combatants = combatants;
		currentPhase = AbilityPriority.PRE_COMBAT;
	}
	
	public void executeCombatTurn()
	{
		/*
		 * First apply pre-combat effects
		 */
		
		for(CombatEntity ce : combatants)
		{
			CombatTarget target = ce.getCombatTarget();
			
			if(ce.hasAbility())
			{
				//ce.getAbility().applyAbility();
			}
		}
	}
	
	private void simAttacks()
	{
		
	}
}
