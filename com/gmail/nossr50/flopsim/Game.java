package com.gmail.nossr50.flopsim;

import java.util.ArrayList;

import com.gmail.nossr50.datatypes.entity.CombatEntity;
import com.gmail.nossr50.datatypes.entity.EntityAlignment;
import com.gmail.nossr50.datatypes.entity.EntityFactory;
import com.gmail.nossr50.datatypes.entity.Hero;
import com.gmail.nossr50.datatypes.lane.LaneState;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a game of artifact
 */
public class Game {
	final int uniqueSimId; //needed?
	private Board board; //Game board
	
	//Heros selected for this game sim via GUI
	private final ArrayList<Hero> direHeroChoices;
	private final ArrayList<Hero> radiantHeroChoices;
	
	//CombatEntity representation of the heros
	@NotNull
	public ArrayList<CombatEntity> enemyHeros 		= new ArrayList<>();
	@NotNull
	public ArrayList<CombatEntity> friendlyHeros 	= new ArrayList<>();
	
	public Game(int uniqueSimId, ArrayList<Hero> direHeroChoices, ArrayList<Hero> radiantHeroChoices)
	{
		this.direHeroChoices 		= direHeroChoices;
		this.radiantHeroChoices 	= radiantHeroChoices;
		this.uniqueSimId 			= uniqueSimId;
		this.board 					= new Board();
	}
	
	/**
	 * Simulates the game from start to finish
	 */
	public void executeGame()
	{
		initHeroEntities(); //Make our heros alive
		fillLanes(); //Fill our lanes randomly
		
		advanceTurn();
	}
	
	/**
	 * Initializes heros into CombatEntities for the first time
	 */
	private void initHeroEntities()
	{
		for(Hero hero : direHeroChoices)
		{
			enemyHeros.add(initHeroEntity(hero, EntityAlignment.DIRE));
		}
		
		for(Hero hero : radiantHeroChoices)
		{
			friendlyHeros.add(initHeroEntity(hero, EntityAlignment.RADIANT));
		}
	}
	
	private CombatEntity initHeroEntity(Hero hero, EntityAlignment ea)
	{
		System.out.println("Constructing hero named :" + hero.toString() +" ["+ea.toString()+"]");
		return EntityFactory.makeHeroCombatEntity(hero, ea);
	}
	
	private CombatEntity constructMeleeCreep(EntityAlignment ea)
	{
		return EntityFactory.makeMeleeCreepCombatEntity(ea);
	}
	
	//Put the heros and creeps into random lanes
	private void fillLanes()
	{
		System.out.println("Filling Lanes...");
		
		/*
		 * Melee creeps will go to one of the three available lanes
		 */
		
		board.addEntitiesToLane(SimTools.getRandom(0, 2), constructMeleeCreep(EntityAlignment.DIRE));
		board.addEntitiesToLane(SimTools.getRandom(0, 2), constructMeleeCreep(EntityAlignment.DIRE));
		board.addEntitiesToLane(SimTools.getRandom(0, 2), constructMeleeCreep(EntityAlignment.RADIANT));
		board.addEntitiesToLane(SimTools.getRandom(0, 2), constructMeleeCreep(EntityAlignment.RADIANT));
		
		//Hero positions
		//System.out.println("Assigning Heros to lanes");
		addHerosToLane();
		System.out.println("Lanes filled!");
		
		//printLaneStatus();
	}
	
	/**
	 * Adds heros to random lanes
	 */
	private void addHerosToLane()
	{
		addHerosToRandomLane(enemyHeros);
		addHerosToRandomLane(friendlyHeros);
	}
	
	/**
	 * In order to avoid sending heros to already occupied lanes we check random lanes until we get one that is unoccupied
	 * @param currentList Array of heros to add to random lanes
	 */
	private void addHerosToRandomLane(ArrayList<CombatEntity> currentList)
	{
		for(CombatEntity currentHero : currentList)
		{
			boolean assignedLane = false;
			
			while(!assignedLane)
			{
				int randomLane = SimTools.getRandom(0, 2);
				
				if(!board.getLane(randomLane).hasHero(currentHero.getAlignment()))
				{
					//System.out.println("Found Lane!");
					board.getLane(randomLane).AddCombatEntity(currentHero);
					assignedLane = true;
				} else {
					//System.out.println("Lane is full");
				}
			}
		}
	}
	
	/**
	 * Simulates and advances the turn
	 */
	private void advanceTurn()
	{
		System.out.println("Simulating turn...");
		
		while(board.getLane(0).getLaneState() != LaneState.FINISHED
				&& board.getLane(1).getLaneState() != LaneState.FINISHED
				&& board.getLane(2).getLaneState() != LaneState.FINISHED)
		{
			board.getLane(0).AdvanceLaneState();
			board.getLane(1).AdvanceLaneState();
			board.getLane(2).AdvanceLaneState();
		}
		
		//Add data
		System.out.println("Sim complete!");
	}
	
}
