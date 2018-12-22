package com.gmail.nossr50.datatypes.entity;

import java.util.ArrayList;
import java.util.HashMap;

import com.gmail.nossr50.datatypes.ability.*;
import com.gmail.nossr50.datatypes.lane.LanePosition;
import com.gmail.nossr50.datatypes.record.Turn;
import com.gmail.nossr50.flopsim.SimTools;
import com.gmail.nossr50.flopsim.combat.AbilityInteraction;
import com.gmail.nossr50.flopsim.combat.CombatTarget;
import com.gmail.nossr50.flopsim.combat.DamageType;

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

    private ArrayList<Aura> currentAuras;

    private HashMap<EntityStat, Integer> pendingStatChange;

    private int maxHealth;

    private LanePosition lp;

    private ArrayList<AbilityInteraction> queuedInteractions;
    private ArrayList<AbilityInteraction> removedInteractions;
    private ArrayList<AbilityInteraction> activeCombatModifierInteractions;


    //Special properties
    private HeroAbility heroAbility;
    private EntityAlignment entityAlignment;

    //Combat stuff
    private CombatTarget combatTarget; //Position of combat target which could be an empty space


    /*
     * Initialize the entity
     */
    public CombatEntity(int uniqueId, String entityName, int baseHealth, int baseArmor, int baseAttack, HeroAbility heroAbility, EntityAlignment entityAlignment, LaneEntityType let) {
        super(let);

        this.uniqueId = uniqueId;

        this.baseHealth = baseHealth;
        this.baseArmor = baseArmor;
        this.baseAttack = baseAttack;
        this.heroAbility = heroAbility;

        this.entityAlignment = entityAlignment;

        this.currentHealth = this.baseHealth;
        this.maxHealth = this.baseHealth;
        this.currentArmor = this.baseArmor;
        this.currentAttack = this.baseAttack;

        this.entityName = entityName;

        if (heroAbility != HeroAbility.MISSING_ABILITY)
            this.ability = new Ability(this, heroAbility);

        //All interactions with this entity are logged and done through this
        queuedInteractions = new ArrayList<AbilityInteraction>();
        removedInteractions = new ArrayList<AbilityInteraction>();
        activeCombatModifierInteractions = new ArrayList<AbilityInteraction>();
        currentAuras = new ArrayList<Aura>(); //Init auras
        pendingStatChange = new HashMap<EntityStat, Integer>();

        for (EntityStat es : EntityStat.values()) {
            pendingStatChange.put(es, 0);
        }
    }

    public int getBaseHealth() {
        return baseHealth;
    }

    public int getBaseArmor() {
        return baseArmor;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getCurrentArmor() {
        return currentArmor;
    }

    public int getCurrentAttack() {
        return currentAttack;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getRegen()
    {
        int regenValue = 0;

        //To calculate regen we most look at all active ability interactions which are regen in nature
        for (AbilityInteraction activeCombatModifierInteraction : activeCombatModifierInteractions) {
            if(activeCombatModifierInteraction.amt == AbilityModifierType.AURA_REGEN)
            {
                //Auras need their host to still be alive so check that first
                if(activeCombatModifierInteraction.source.isAlive())
                {
                    regenValue+=activeCombatModifierInteraction.abilityValue; //Add regen value
                }
            }

            if(activeCombatModifierInteraction.amt == AbilityModifierType.REGEN)
            {
                regenValue+=activeCombatModifierInteraction.abilityValue; //Add regen
            }
        }

        return regenValue;
    }

    public boolean isAlive()
    {
        return (currentHealth > 0);
    }

    public int getUID() {
        return uniqueId;
    }

    public EntityAlignment getAlignment() {
        return entityAlignment;
    }

    public boolean hasAbility() {
        return heroAbility != HeroAbility.MISSING_ABILITY;
    }

    public void setCombatTarget(CombatTarget ct) {
        combatTarget = ct;
    }

    public CombatTarget getCombatTarget() {
        return combatTarget;
    }

    public boolean hasCleave() {
        return heroAbility == HeroAbility.GREAT_CLEAVE; //For now this is the only cleave that a hero can start with
    }

    public boolean isBlocked() {
        return !combatTarget.isTargetTower();
    }

    public String getName() {
        return this.entityName;
    }

    @Override
    public String toString() {
        String newString = "";

        newString += this.entityName;
        newString += "(UID:" + getUID() + ")";
        //newString+=" : [HP" + this.getCurrentHealth() + " / " + this.getBaseHealth() + " ] ";

        return newString;
    }

    public Ability getAbility() {
        return ability;
    }

    public void addQueuedAbilityInteraction(AbilityInteraction newInteraction) {
        System.out.println("Adding new interaction to target " + toString());
        queuedInteractions.add(newInteraction);
    }

    public void executeQueuedAbilityInteractions() throws InvalidAbilityException {
        //Execute all queued interactions
        for (AbilityInteraction ai : queuedInteractions) {
            //Normal AbilityType refers to a plain stat change effect rather than a scripted complex interaction
            if (ai.at == AbilityType.NORMAL) {
                switch (ai.amt) {
                    case AURA_ARMOR:
                        addAura(new Aura(ai));
                        break;
                    case AURA_ATTACK:
                        addAura(new Aura(ai));
                        break;
                    case AURA_REGEN:
                        addAura(new Aura(ai));
                        break;
                    case MODIFY_ARMOR:
                        addPendingStatChange(ai.getTargetStat(), ai.abilityValue);
                        break;
                    case MODIFY_HEALTH:
                        dealDamage(ai.abilityValue, DamageType.NORMAL);
                        break;
                    case MODIFY_ATTACK:
                        addPendingStatChange(ai.getTargetStat(), ai.abilityValue);
                        break;
                    case REGEN:
                        addPendingStatChange(ai.getTargetStat(), ai.abilityValue);
                        break;
                    case RETALIATE:
                        dealDamage(ai.abilityValue, DamageType.NORMAL);
                        break;
                    case CLEAVE:
                        dealDamage(ai.abilityValue, DamageType.NORMAL);
                        break;
                    case PIERCE_DAMAGE:
                        dealDamage(ai.abilityValue, DamageType.PIERCE);
                        break;
                }
            }
        }
    }

    private void addAura(Aura newAura)
    {
        //TODO: Remove this debug
        System.out.println("Adding aura for "+this.toString());

        currentAuras.add(newAura);
    }

    public void addPendingStatChange(EntityStat targetStat, int modifier) {
        System.out.println("pending stat change [" + targetStat.toString() + " " + modifier + "] for " + toString());
        int old = pendingStatChange.get(targetStat);
        pendingStatChange.put(targetStat, (old + modifier));
    }

    public void updateStats() {
        currentHealth += pendingStatChange.get(EntityStat.HEALTH);
        maxHealth += pendingStatChange.get(EntityStat.MAX_HEALTH);
        currentAttack += pendingStatChange.get(EntityStat.ATTACK);
        currentArmor += pendingStatChange.get(EntityStat.ARMOR);
    }

    /**
     * This is for retaliate interactions and such
     *
     * @param source Source of the melee combat damage
     */
    public void dealMeleeCombatDamage(CombatEntity source, Turn turn, DamageType damageType) {
        int incDamage = source.getCurrentAttack();

        dealDamage(incDamage, damageType);

        //Trigger any defensive skills
        if (getAbility().isTriggeredOnDefense()) {
            getAbility().triggerDefensiveSkill(source, turn);
        }
    }

    private void dealDamage(int incDamage, DamageType damageType) {
        int damageReduction = 0;

        if(damageType == DamageType.NORMAL)
            damageReduction = this.getCurrentArmor();

        int endResult = incDamage - damageReduction;

        int regenValue = getRegen();

        endResult+= regenValue;

        if(endResult > 0)
            addPendingStatChange(EntityStat.HEALTH, -endResult); //Queue up the negative change in health

        //TODO: Apply damage, flag entity as dead, don't let max health go out of range
        //TODO: getCurrentArmor should account for auras, same for attack and other stats
    }


    public void dealRetaliateDamage(CombatEntity source, Turn turn) {
        /*
         * This code is run when this entity is being hit with retaliate damage which is processed seperately from attack
         * Retaliate can trigger offensive skills
         */

        //Check for defensive debuffs
        if (getAbility().isDefensiveDebuff()) {
            System.out.println("Defensive Debuff found " + toString());
            getAbility().triggerDefensiveSkill(source, turn);
        }
    }

    /**
     * Used for abilities that don't need to be queued to execute, mostly for statistic purposes
     *
     * @param ai Ability Interaction to log
     */
    public void recordAbilityInteraction(AbilityInteraction ai) {
        removedInteractions.add(ai);
    }

    public void setLanePosition(LanePosition lp) {
        this.lp = lp;
    }

    public LanePosition getLanePosition() {
        return lp;
    }

    public String toASCIIArt() {
        return new String("[" + SimTools.getShorter(entityName, 5) + "]");
    }
}
