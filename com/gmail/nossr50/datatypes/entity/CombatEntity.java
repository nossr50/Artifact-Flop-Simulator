package com.gmail.nossr50.datatypes.entity;

import java.util.ArrayList;
import java.util.HashMap;

import com.gmail.nossr50.datatypes.ability.Ability;
import com.gmail.nossr50.datatypes.ability.AbilityType;
import com.gmail.nossr50.datatypes.ability.HeroAbility;
import com.gmail.nossr50.datatypes.lane.LanePosition;
import com.gmail.nossr50.datatypes.record.Turn;
import com.gmail.nossr50.flopsim.SimTools;
import com.gmail.nossr50.flopsim.combat.AbilityInteraction;
import com.gmail.nossr50.flopsim.combat.CombatTarget;

public class CombatEntity extends LaneEntity {

	/*
	 * Static vars
	 */
	private final int uniqueId;
	private final String entityName;
	private final int baseHealth;
	private final int baseArmor;
	private final int baseAttack;
	
	private Ability ability;
	
	private int currentHealth;
	private int currentArmor;
	private int currentAttack;
	
	private HashMap<EntityStat, Integer> pendingStatChange;
	
	private int maxHealth;
	
	private ArrayList<AbilityInteraction> queuedInteractions;
	private ArrayList<AbilityInteraction> removedInteractions;
	private ArrayList<AbilityInteraction> activeCombatModifierInteractions;
	
	//Lane stuff
	private LanePosition lp; //Uses for stuff like grabbing neighbors
	
	
	//Special properties
	private HeroAbility heroAbility;
	private EntityAlignment entityAlignment;
	
	//Combat stuff
	private CombatTarget combatTarget; //Position of combat target which could be an empty space
	
	
	/*
	 * Initialize the entity
	 */
	public CombatEntity(int uniqueId, String entityName, int baseHealth, int baseArmor, int baseAttack, HeroAbility heroAbility, EntityAlignment entityAlignment, LaneEntityType let)
	{
		super(let);
		
		this.uniqueId 			= uniqueId;
		
		this.baseHealth 		= baseHealth;
		this.baseArmor 			= baseArmor;
		this.baseAttack 		= baseAttack;
		this.heroAbility 		= heroAbility;
		
		this.entityAlignment 	= entityAlignment;
		
		this.currentHealth 		= this.baseHealth;
		this.maxHealth			= this.baseHealth;
		this.currentArmor 		= this.baseArmor;
		this.currentAttack 		= this.baseAttack;
		
		this.entityName 		= entityName;
		
		if(heroAbility != HeroAbility.MISSING_ABILITY)
			this.ability 			= new Ability(this, heroAbility);
		
		//All interactions with this entity are logged and done through this
		queuedInteractions 				= new ArrayList<AbilityInteraction>();
		removedInteractions 				= new ArrayList<AbilityInteraction>();
		activeCombatModifierInteractions 	= new ArrayList<AbilityInteraction>();
		
		pendingStatChange = new HashMap<EntityStat, Integer>();
		
		for(EntityStat es : EntityStat.values()) { pendingStatChange.put(es, 0); }
	}
	
	public void updateLanePosition(LanePosition newPosition)
	{
		System.out.println("Updating LP for "+toString());
		lp = newPosition;
	}
	
	public int getBaseHealth() { return baseHealth; }
	public int getBaseArmor() { return baseArmor; }
	public int getBaseAttack() { return baseAttack; }
	
	public int getCurrentHealth() { return currentHealth; }
	public int getCurrentArmor()  { return currentArmor; }
	public int getCurrentAttack() { return currentAttack; }
	
	public int getMaxHealth() { return maxHealth; }
	
	public int getUID() { return uniqueId; }
	
	public EntityAlignment getAlignment() { return entityAlignment; }
	
	public boolean hasAbility() { return heroAbility != HeroAbility.MISSING_ABILITY; }

	public void setCombatTarget(CombatTarget ct) { combatTarget = ct; }
	
	public CombatTarget getCombatTarget() { return combatTarget; }
	
	public ArrayList<CombatEntity> getAlliedNeighbors() { return lp.getAlliedNeighbors(); }
	public ArrayList<CombatEntity> getEnemyNeighbors() { return lp.getEnemyNeighbors(); }
	public ArrayList<CombatEntity> getAbilityTargets() { return lp.getTargetEntities(ability); }
	public LanePosition getLanePosition() { return lp; }
	
	public boolean hasCleave()
	{
		return heroAbility == HeroAbility.GREAT_CLEAVE; //For now this is the only cleave that a hero can start with
	}
	
	public boolean isBlocked() {
		return !combatTarget.isTargetTower();
	}
	
	public String getName() { return this.entityName; }
	
	public String toString()
	{
		String newString = "";
		
		newString+=this.entityName;
		newString+="(UID:"+getUID()+")";
		//newString+=" : [HP" + this.getCurrentHealth() + " / " + this.getBaseHealth() + " ] ";
		
		return newString;
	}
	
	public Ability getAbility() { return ability; }
	
	public void addAbilityInteraction(AbilityInteraction newInteraction) 
	{
		System.out.println("Adding new interaction to target "+toString());
		queuedInteractions.add(newInteraction);
	}
	
	public void executeAbilityInteractions()
	{
		//Execute all queued interactions
		for(AbilityInteraction ai : queuedInteractions)
		{
			if(ai.at == AbilityType.NORMAL)
			{
				
			}
		}
	}
	
	public void addPendingStatChange(EntityStat targetStat, int modifier)
	{
		System.out.println("pending ["+targetStat.toString() + " " + modifier + "] for "+toString());
		int old = pendingStatChange.get(targetStat);
		pendingStatChange.put(targetStat, (old + modifier));
	}
	
	public void updateStats()
	{
		currentHealth 	+= pendingStatChange.get(EntityStat.HEALTH);
		maxHealth 	  	+= pendingStatChange.get(EntityStat.MAX_HEALTH);
		currentAttack 	+= pendingStatChange.get(EntityStat.ATTACK);
		currentArmor 	+= pendingStatChange.get(EntityStat.ARMOR);
	}
	
	/**
	 * This is for retaliate interactions and such
	 * @param source Source of the melee combat damage
	 */
	public void dealMeleeCombatDamage(CombatEntity source, Turn turn)
	{
		int incDamage = source.getCurrentAttack();
		int damageReduction = this.getCurrentArmor();
		
		int endResult = incDamage - damageReduction;
		
		if(endResult < 0)
			endResult = 0;
		
		//Trigger any defensive skills
		if(getAbility().isTriggeredOnDefense())
		{
			getAbility().triggerDefensiveSkill(source, turn);
		}
	}
	
	
	public void dealRetaliateDamage(CombatEntity source, Turn turn)
	{
		/*
		 * This code is run when this entity is being hit with retaliate damage which is processed seperately from attack
		 * Retaliate can trigger offensive skills
		 */
		
		//Check for defensive debuffs
		if(getAbility().isDefensiveDebuff())
		{
			System.out.println("Defensive Debuff found " + toString());
			getAbility().triggerDefensiveSkill(source, turn);
		}
	}
	
	/**
	 * Used for abilities that don't need to be queued to execute, mostly for statistic purposes
	 * @param ai Ability Interaction to log
	 */
	public void recordAbilityInteraction(AbilityInteraction ai)
	{
		removedInteractions.add(ai);
	}
	
	public String toASCIIArt() { return new String("["+SimTools.getShorter(entityName, 5)+"]"); }
}
