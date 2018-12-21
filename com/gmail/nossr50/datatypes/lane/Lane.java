package com.gmail.nossr50.datatypes.lane;

import java.util.ArrayList;
import java.util.Random;

import com.gmail.nossr50.datatypes.ArrowType;
import com.gmail.nossr50.datatypes.ability.AbilityPriority;
import com.gmail.nossr50.datatypes.entity.Arrow;
import com.gmail.nossr50.datatypes.entity.CombatEntity;
import com.gmail.nossr50.datatypes.entity.EntityAlignment;
import com.gmail.nossr50.datatypes.entity.EntityFactory;
import com.gmail.nossr50.datatypes.entity.LaneEntity;
import com.gmail.nossr50.datatypes.entity.LaneEntityType;
import com.gmail.nossr50.datatypes.record.RecordType;
import com.gmail.nossr50.datatypes.record.SimRecord;
import com.gmail.nossr50.datatypes.record.Turn;
import com.gmail.nossr50.flopsim.combat.CombatTarget;

public class Lane {
	private SimRecord simRecordRef;
	
	public ArrayList<CombatEntity> combatEntities; //every combat entity in this lane
	
	public ArrayList<LaneEntity> predeploy_Dire 	= new ArrayList<LaneEntity>();
	public ArrayList<LaneEntity> predeploy_Radiant  = new ArrayList<LaneEntity>();
	
	//Keeps track of whether or not a hero is assigned to this lane yet
	private boolean hasDireHero 	= false;
	private boolean hasRadiantHero 	= false;
	
	//LaneEntity(s) which are deployed
	private LaneEntity[] deployed_Enemies;
	private LaneEntity[] deployed_Friendlies;
	
	//Lane state vars
	private LaneState state = LaneState.FRESH;
	private Turn currentTurn;
	int curTurnCount = 0;
	
	//Towers
	private Tower direTower;
	private Tower radiantTower;
	
	//Hero refs
	private CombatEntity radiantHero;
	private CombatEntity direHero;
	
	public Lane(SimRecord simRecordRef)
	{
		currentTurn = Turn.ON_FLOP; //New turn
		direTower 			= EntityFactory.makeTower(EntityAlignment.DIRE);
		radiantTower 		= EntityFactory.makeTower(EntityAlignment.RADIANT);
		this.simRecordRef 	= simRecordRef; //The SimRecord related to this particular simulation
		combatEntities = new ArrayList<CombatEntity>();
	}
	
	//TODO: Rewrite this garbo
	public void AddCombatEntity(CombatEntity ce)
	{
		//Add all combat entities to this list for combat stages
		combatEntities.add(ce);
		
		/*
		 * Assign entities to respective sides
		 */
		
		if(ce.getAlignment() == EntityAlignment.RADIANT)
		{
			predeploy_Radiant.add(ce);
		}
		else
		{
			predeploy_Dire.add(ce);
		}
		
		/*
		 * Keep track of heros added to the lane
		 */
		if(ce.isHero())
		{
			switch(ce.getAlignment())
			{
			case DIRE:
				direHero = ce;
				hasDireHero = true;
				break;
			case RADIANT:
				radiantHero = ce;
				hasRadiantHero = true;
				break;
			default:
				break;
			
			}
		}
	}
	
	/**
	 * Advances the state of the lane
	 * FRESH (New lane starting on flop) / PRE_DEPLOYMENT (Lane on its 2nd turn or higher) -> DEPLOYED -> PRE_ACTION -> PRE_COMBAT -> POST_COMBAT -> FINISHED
	 */
	public void AdvanceLaneState()
	{
		switch(state)
		{
		case DEPLOYED:
			state = LaneState.PRE_ACTION;
			break;
		case FINISHED:
			break;
		case FRESH:
			DeployEntities();
			state = LaneState.DEPLOYED;
			break;
		case PRE_DEPLOYMENT:
			DeployEntities();
			state = LaneState.DEPLOYED;
			break;
		case PRE_ACTION:
			initPreAction();
			state = LaneState.PRE_COMBAT;
			break;
		case PRE_COMBAT:
			initPreCombat();
			state = LaneState.POST_COMBAT;
			break;
		case POST_COMBAT:
			initPostCombat();
			state = LaneState.FINISHED;
			break;
		default:
			break;
		
		}
	}
	
	private void DeployEntities()
	{
		if(state == LaneState.FRESH)
			initFirstDeployment();
		else
		{
			//Turn 2 stuff
		}
	}
	
	private void executeAbilities()
	{
		//Executes all abilities waiting to happen
	}
	
	/*
	 * This is where we apply combat modifiers and execute any pre-action phase stuff
	 */
	private void initPreAction()
	{
		System.out.println("Advancing to pre-action");
		/*
		 * Check for any pre-action abilities
		 */
		
		printLaneASCIIArt();
		
		//Queue any abilities that need to fire now
		for(CombatEntity ce : combatEntities)
		{
			if(ce.hasAbility())
			{
				if(ce.getAbility().doesAbilityTriggerNow(AbilityPriority.PRE_ACTION))
				{
					if(ce.getLanePosition() == null)
						System.out.println("Somehow LP is null!");
					
					System.out.println("Triggering ability for "+ce.toString());
					ce.getAbility().applyAbility(currentTurn);
				}
			}
		}
		
		//Execute abilities
		for(CombatEntity ce : combatEntities)
		{
			System.out.println("Executing ability interactions");
			ce.executeAbilityInteractions();
		}
		
		/*
		 * Add stats about any blocked Radiant heros
		 */
		if(radiantHero.isBlocked())
			simRecordRef.addStat(EntityAlignment.RADIANT, RecordType.HERO_BLOCKS, 1);
		else
			System.out.println("Debug: Hero wasn't blocked");
		
		//Debug
		printDebugPostDeployStatus();
	}
	
	private void registerTargets()
	{
		/*
		 * Initialize targets
		 */
		getTargets(deployed_Enemies, deployed_Friendlies);
		getTargets(deployed_Friendlies, deployed_Enemies);
	}
	
	private void getTargets(LaneEntity[] laneEntRef_a, LaneEntity[] laneEntRef_b)
	{
		for(int x = 0; x < getLaneSize(); x++)
		{
			if(!laneEntRef_a[x].isArrow())
			{
				CombatEntity ce = (CombatEntity) laneEntRef_a[x];
				
				if(!laneEntRef_b[x].isArrow())
				{
					//Combat entities always attack forwards if something is in front of them
					ce.setCombatTarget(new CombatTarget(laneEntRef_b[x]));
				}
					
				else
				{
					//This means the position is empty and is an arrow, so we check where the arrow will point us
					laneEntRef_b[x].printType();
					Arrow arrow = (Arrow) laneEntRef_b[x];
					
					if(arrow.getArrowDirection() == ArrowType.STRAIGHT)
					{
						ce.setCombatTarget(new CombatTarget(laneEntRef_b[x]));
					} else {
						
						if(arrow.getArrowDirection() == ArrowType.LEFT)
						{
							int targetPos = x-1;		
							
							if(targetPos < 0)
								ce.setCombatTarget(new CombatTarget(laneEntRef_b[x])); //If the target is out of array index attack forward
							else
								ce.setCombatTarget(new CombatTarget(laneEntRef_b[targetPos]));
						} else {
							int targetPos = x+1;
							
							//Only possible result now is a RIGHT arrow
							
							if(targetPos > getLaneSize()-1)
								ce.setCombatTarget(new CombatTarget(laneEntRef_b[x])); //If the target is out of array index attack forward
							else
								ce.setCombatTarget(new CombatTarget(laneEntRef_b[targetPos]));
						}
					}
				}
				
				//If the thing we ended up targeting was an empty space then set its target to tower
				if(ce.getCombatTarget().isTargetTower())
				{
					setTargetToTower(ce);
				}
			}
		}
	}
	
	private void setTargetToTower(CombatEntity ce)
	{
		System.out.println(ce.toString() + " is attacking the enemy tower!");
		if(ce.getAlignment() == EntityAlignment.RADIANT)
			ce.setCombatTarget(new CombatTarget(direTower));
		else
			ce.setCombatTarget(new CombatTarget(radiantTower));
	}
	
	/**
	 * This is where buffs and modifiers will be applied
	 */
	private void initPreCombat()
	{
		System.out.println("Starting pre-combat");
		initCombat();
	}
	
	private void initCombat()
	{
		System.out.println("Starting combat");
		
	}
	
	/**
	 * This is where we apply modifiers, and remove any dead entities.
	 */
	private void initPostCombat()
	{
		System.out.println("Starting post-combat...");
		
		
	}
	
	private void initFirstDeployment()
	{
		/*
		 * For the first deployment we have to set some things up
		 */
		int curLaneSize = getLaneSize();
		System.out.println("Current Lane Width: " + curLaneSize);
		
		//Add arrows until both sides are the same size
		if(predeploy_Dire.size() < curLaneSize)
			AddArrows(EntityAlignment.DIRE, curLaneSize - predeploy_Dire.size());
		
		if(predeploy_Radiant.size() < curLaneSize)
			AddArrows(EntityAlignment.RADIANT, curLaneSize - predeploy_Radiant.size());
		
		//Assign positions randomly
		System.out.println("Assigning positions in lane deployment...");
		
		//Make & Init new lane entity arrays
		deployed_Friendlies 	= deployLaneEntities(predeploy_Radiant);
		deployed_Enemies 		= deployLaneEntities(predeploy_Dire);
		
		registerTargets(); //Get target data
		updateLanePositions(deployed_Enemies, deployed_Friendlies);
	}
	
	private void updateLanePositions(LaneEntity[] direEntities, LaneEntity[] radiantEntities)
	{
		int lanePos = 0;
		int laneSize = direEntities.length; //Dire and Radiant entities should always be the same size, which is the size of the lane
		
		System.out.println("Lane position : "+lanePos+", Size :"+laneSize);
		while(lanePos < laneSize-1)
		{
			System.out.println("Iteration: "+lanePos);
			makeLanePosition(direEntities, radiantEntities, lanePos, direEntities.length);
			makeLanePosition(radiantEntities, direEntities, lanePos, radiantEntities.length);
			lanePos++;
		}
	}
	
	/**
	 * This method builds neighbor data (LanePosition) for different CombatEntities
	 * @param allies Array of Entities considered allies
	 * @param enemies Array of Entities considered enemies
	 * @param lanePos Current position in the array
	 * @param laneSize Size of the lane
	 */
	private void makeLanePosition(LaneEntity[] allies, LaneEntity[] enemies, int lanePos, int laneSize)
	{
		if(!allies[lanePos].isArrow())
		{
			/*
			 * Combat Entity Found
			 */
			CombatEntity ce = (CombatEntity) allies[lanePos];
			LanePosition newLanePosition = new LanePosition(ce); //Init LanePosition
			
			System.out.println("Checking neighbors for position "+lanePos+" in a lane of size "+laneSize);
			
			/*
			 * Find neighbors
			 */
			
			//Neighbor to the left only exists if we aren't the left-most entity
			if(lanePos > 0)
			{
				newLanePosition.addPotentialAlliedNeighbor(allies[lanePos-1]); //One to the left
				newLanePosition.addPotentialEnemyNeighbor(enemies[lanePos-1]); //One to the left
			}
			
			//Neighbor to the right only exists if we aren't the right-most entity
			if(lanePos < laneSize-1)
			{
				newLanePosition.addPotentialAlliedNeighbor(allies[lanePos+1]);
				newLanePosition.addPotentialEnemyNeighbor(enemies[lanePos+1]);
			}
			
			//Finally add the enemy neighbor across from our position
			newLanePosition.addPotentialEnemyNeighbor(enemies[lanePos]);
			
			//Add all enemies in the lane to our enemiesinlane lanepos data
			for(LaneEntity enemyInLane : enemies)
			{
				newLanePosition.addPotentialEnemyInThisLane(enemyInLane);
			}
			
			//Add the lane position data to our Combat Entity
			ce.updateLanePosition(newLanePosition);
		}
	}
	
	private LaneEntity[] deployLaneEntities(ArrayList<LaneEntity> currentList)
	{
		LaneEntity[] newDestinationArray = new LaneEntity[currentList.size()];
		
		for(LaneEntity le : currentList)
		{
			boolean assigned = false;
			
			while(assigned == false)
			{
				int newPos = getRandom(0, newDestinationArray.length-1);
				
				if(newDestinationArray[newPos] == null)
				{
					newDestinationArray[newPos] = le;
					assigned = true;
				} else {
					//System.out.println("[DEBUG] Spot full");
				}
			}
		}
		
		return newDestinationArray;
	}
	
	private int getLaneSize() 
	{
		int laneSize = predeploy_Dire.size() >= predeploy_Radiant.size() ? predeploy_Dire.size() : predeploy_Radiant.size();
		return laneSize;
	}
	
	/*
	 * Add arrows until both sides have the same number of entities
	 */
	private void AddArrows(EntityAlignment ea, int numArrows)
	{
		int iteration = 0;
		while(iteration < numArrows)
		{
			if(curTurnCount >= 1)
			{
				addArrow(getRandomArrow(), getEntityArray(ea));
			} else {
				addArrow(ArrowType.STRAIGHT, getEntityArray(ea));
			}
			iteration++;
		}
	}
	
	private void addArrow(ArrowType at, ArrayList<LaneEntity> entityArray)
	{
		//This is a lazy way to do this, I should be using polymorphism I'll change it later
		entityArray.add(new Arrow(LaneEntityType.ARROW, at));
	}
	
	private ArrayList<LaneEntity> getEntityArray(EntityAlignment ea)
	{
		if(ea == EntityAlignment.DIRE)
			return predeploy_Dire;
		else
			return predeploy_Radiant;
	}
	
	private ArrowType getRandomArrow()
	{
		Random random = new Random();
		switch(random.nextInt(3) + 1)
		{
		case 1:
			return ArrowType.LEFT;
		case 2:
			return ArrowType.STRAIGHT;
		case 3:
			return ArrowType.RIGHT;
		default:
			System.out.println("WARNING: Switch case broken for lanes");
			return ArrowType.STRAIGHT;
		}
	}
	
	public int getEntityCount() { return predeploy_Dire.size() + predeploy_Radiant.size(); }
	
	public boolean hasHero(EntityAlignment ea)
	{
		boolean hasHero = ea == EntityAlignment.RADIANT ? hasRadiantHero : hasDireHero;
		return hasHero;
	}
	
	public void printDebugStatus()
	{
		System.out.println("Enemy Hero Present?: " + hasDireHero + " || Enemy Count: " + predeploy_Dire.size());
		System.out.println("Good Hero Present?: " + hasRadiantHero + "  || Friend Count: " + predeploy_Radiant.size());
	}
	
	public void printDebugPostDeployStatus()
	{
		for(LaneEntity le : deployed_Enemies)
		{
			String output = "";
			if(le instanceof CombatEntity)
			{
				CombatEntity ce = (CombatEntity) le;
				
				output+= ce.toString();
				System.out.println(output);
			}
		}
		
		for(LaneEntity le : deployed_Friendlies)
		{
			String output = "";
			if(le instanceof CombatEntity)
			{
				CombatEntity ce = (CombatEntity) le;
				
				output+= ce.toString();
				System.out.println(output);
			}
		}
		
		System.out.println();
	}
	
	private int getRandom(int min, int max)
	{
		Random random = new Random();
		return random.nextInt(max + 1) + min;
	}
	
	public LaneState getLaneState() { return state; }
	
	public void printLaneASCIIArt()
	{
		String radiantLaneASCII = "";
		String direLaneASCII = "";
		
		for(LaneEntity le : deployed_Enemies)
		{
			if(le instanceof Arrow)
				radiantLaneASCII+=((Arrow) le).toASCIIArt();
			if(le instanceof CombatEntity)
				radiantLaneASCII+=((CombatEntity) le).toASCIIArt();
			
			radiantLaneASCII+=" ";
		}
		
		for(LaneEntity le : deployed_Friendlies)
		{
			if(le instanceof Arrow)
				direLaneASCII+=((Arrow) le).toASCIIArt();
			if(le instanceof CombatEntity)
				direLaneASCII+=((CombatEntity) le).toASCIIArt();
			
			direLaneASCII+=" ";
		}
		
		System.out.println("Lane visualization");
		System.out.println(direLaneASCII);
		System.out.println(radiantLaneASCII);
	}
}
