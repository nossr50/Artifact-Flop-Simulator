package com.gmail.nossr50.datatypes.lane;

import java.util.ArrayList;

import com.gmail.nossr50.datatypes.ability.Ability;
import com.gmail.nossr50.datatypes.entity.CombatEntity;
import com.gmail.nossr50.datatypes.entity.LaneEntity;
import com.gmail.nossr50.flopsim.SimTools;

public class LanePosition {
	//private int positionInLane; //Position within the lane
	private final CombatEntity self; //Entity in this position (not sure if needed)
	private ArrayList<CombatEntity> alliedNeighbors;
	private ArrayList<CombatEntity> enemyNeighbors;
	private ArrayList<CombatEntity> enemiesInLane; //Enemy's in this lane
	
	public LanePosition(CombatEntity entity)
	{
		this.self = entity;
		
		//Init array lists
		alliedNeighbors = new ArrayList<CombatEntity>();
		enemyNeighbors = new ArrayList<CombatEntity>();
		enemiesInLane = new ArrayList<CombatEntity>();
	}
	
	public void addAlliedNeighbor(CombatEntity ce)
	{
		alliedNeighbors.add(ce);
	}
	
	/**
	 * Adds only valid non-arrow neighbors
	 * @param potentialNeighbor The neighbor to check
	 */
	public void addPotentialAlliedNeighbor(LaneEntity potentialNeighbor)
	{
		System.out.println("Debug: Attempting to add ally neighbor for "+self.toString());
		if(potentialNeighbor instanceof CombatEntity)
		{
			//Neighbor isn't an arrow
			CombatEntity newNeighbor = (CombatEntity) potentialNeighbor;
			alliedNeighbors.add(newNeighbor);
			System.out.println("Added allied neighbor ["+newNeighbor.toString()+"] for ["+self.toString() +"]");
		}
	}
	
	/**
	 * Adds only valid non-arrow neighbors
	 * @param potentialNeighbor The neighbor to check
	 */
	public void addPotentialEnemyNeighbor(LaneEntity potentialNeighbor)
	{
		System.out.println("Debug: Attempting to add enemy neighbor for "+self.toString());
		if(potentialNeighbor instanceof CombatEntity)
		{
			//Neighbor isn't an arrow
			CombatEntity newNeighbor = (CombatEntity) potentialNeighbor;
			enemyNeighbors.add(newNeighbor);
			System.out.println("Added enemy neighbor ["+newNeighbor.toString()+"] for ["+self.toString() +"]");
		}
	}
	
	public void addPotentialEnemyInThisLane(LaneEntity potentialNeighbor)
	{
		System.out.println("Debug: Attempting to add lane enemies for "+self.toString());
		if(potentialNeighbor instanceof CombatEntity)
		{
			//Neighbor isn't an arrow
			CombatEntity newNeighbor = (CombatEntity) potentialNeighbor;
			enemiesInLane.add(newNeighbor);
			System.out.println("Added enemy in lane ["+newNeighbor.toString()+"] for ["+self.toString() +"]");
		}
	}
	
	public ArrayList<CombatEntity> getTargetEntities(Ability ability)
	{
		System.out.println("Grabbing targets");
		ArrayList<CombatEntity> targetEntities = new ArrayList<CombatEntity>();
		CombatEntity currentTarget = self.getCombatTarget().getTarget();
		
		switch(ability.getAbilityTargetType())
		{
		case ALLIED_NEIGHBORS:
			targetEntities.addAll(alliedNeighbors);
			break;
		case ALLIED_NEIGHBORS_AND_SELF:
			targetEntities.addAll(alliedNeighbors);
			targetEntities.add(self);
			break;
		case BLOCKER:
			targetEntities.add(currentTarget);
			break;
		case COMBAT_TARGET_ALL:
			targetEntities.add(currentTarget);
			break;
		case COMBAT_TARGET_HERO:
			if(currentTarget.isHero())
				targetEntities.add(currentTarget);
			break;
		case COMBAT_TARGET_HERO_OR_TOWER:
			if(currentTarget.isHero() || currentTarget.isTower())
				targetEntities.add(currentTarget);
			break;
		case COMBAT_TARGET_NEIGHBORS:
			//Add only the neighbors of our target (used for cleave)
			if(!self.getCombatTarget().isTargetTower())
				targetEntities.addAll(currentTarget.getAlliedNeighbors());
			break;
		case COMBAT_TARGET_TOWER:
			if(currentTarget.isTower())
				targetEntities.add(currentTarget);
			break;
		case ENEMY_NEIGHBORS:
			targetEntities.addAll(self.getEnemyNeighbors());
			break;
		case RANDOM_ENEMY_NEIGHBOR:
			targetEntities.addAll(getRandom(getEnemyNeighbors()));
			break;
		case GLOBAL_ALLIES:
			//TODO: Fix this for drow
			break;
		case GLOBAL_ENEMIES:
			break;
		case RANDOM_ENEMY:
			targetEntities.addAll(getRandom(enemiesInLane));
			break;
		case SELF:
			targetEntities.add(self);
			break;
		default:
			break;
		}
		
		System.out.println("Targets found: "+ targetEntities.size());
		return targetEntities;
	}
	
	//TODO: Check randomness
	public ArrayList<CombatEntity> getRandom(ArrayList<CombatEntity> potentialTargets)
	{
		ArrayList<CombatEntity> randomTarget = new ArrayList<CombatEntity>();
		
		int randomChoice = SimTools.getRandom(0, potentialTargets.size()-1);
		
		randomTarget.add(potentialTargets.get(randomChoice));
		
		return randomTarget;
	}
	
	public ArrayList<CombatEntity> getAlliedNeighbors() { return alliedNeighbors; }
	public ArrayList<CombatEntity> getEnemyNeighbors() { return enemyNeighbors; }
}
