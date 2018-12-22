package com.gmail.nossr50.datatypes.ability;

import java.util.ArrayList;

import com.gmail.nossr50.datatypes.entity.CombatEntity;
import com.gmail.nossr50.datatypes.entity.EntityStat;
import com.gmail.nossr50.datatypes.lane.LanePosition;
import com.gmail.nossr50.datatypes.record.Turn;
import com.gmail.nossr50.flopsim.combat.AbilityInteraction;

public class Ability {

    public final HeroAbility heroAbility;
    public final CombatEntity source; //owner


    public Ability(CombatEntity source, HeroAbility at) {
        this.source = source;
        this.heroAbility = at;
    }

    /**
     * Returns the current value modifier of the ability
     *
     * @return Ability's value modifier
     */
    public int getValue() {
        //Greater cleave is rounded down
        if (heroAbility == HeroAbility.GREAT_CLEAVE) {
            //Java rounds down to zero automatically
            //Example 5 divided by 2 = 2.5, result will be 2
            return (source.getCurrentAttack() / 2);
        }

        //Blood bath fully heals
        if (heroAbility == HeroAbility.BLOOD_BATH)
            return source.getMaxHealth();

        return heroAbility.getValue();
    }

    public boolean doesAbilityTriggerNow(AbilityPriority ap) {
        return heroAbility.getPriority() == ap;
    }

    /**
     * Finds CombatEntities fulfilling targeting requirements of this ability and then registers a new AbilityInteraction onto said entities
     *
     * @param turn Turn that the game is currently on
     */
    public void addAbilityInteraction(Turn turn) {
        LanePosition lp = source.getLanePosition(); //LanePosition has positional data relating to abilities
        ArrayList<CombatEntity> abilityTargets = lp.getTargetEntities(this); //This grabs all applicable targets for an ability, and its empty if there are none.


        //Special Abilities have complex rules and need their own code
        if (heroAbility.isAbilitySpecial()) {
            if (heroAbility == HeroAbility.REACTIVE_ARMOR) {
                addReactiveArmorInteraction(turn, abilityTargets);
            }
        } else {
            //All other abilities are relatively simple and can be piped in through here
            addAbilityInteraction(turn, abilityTargets);
        }
        //TODO: Check ability target code
    }

    private void addReactiveArmorInteraction(Turn turn, ArrayList<CombatEntity> abilityTargets) {
        AbilityInteraction newInteraction;
        int reactiveArmorValue = -1;

        for (CombatEntity target : abilityTargets) {
            System.out.println("[DEBUG] Adding ability interaction from " + source.toString() + " via " + heroAbility.toString());

            //Apply -1 armor to attackers
            if (target != source)
                newInteraction = new AbilityInteraction(source, target, heroAbility.getModifierType(), heroAbility.getAbilityType(), -1, turn);
            else
                newInteraction = new AbilityInteraction(source, target, heroAbility.getModifierType(), heroAbility.getAbilityType(), getReactiveArmorValue(abilityTargets), turn);

            target.addQueuedAbilityInteraction(newInteraction);
        }
    }

    /**
     * Timbersaw gets 1 armor for every attacker
     * This returns that value
     *
     * @param abilityTargets
     * @return The amount of armor added by the ability
     */
    private int getReactiveArmorValue(ArrayList<CombatEntity> abilityTargets) {
        /*
            Timbersaw gets 1 armor for every attacker
            This array includes himself so we'll start the value at -1 (it will always be at least zero due to timbersaw being included in the array)
            So with 2 entities in this array (one being timbersaw) he will get 1 armor as a result
         */
        int value = -1;

        for (CombatEntity abilityTarget : abilityTargets) {
            value += 1;
        }

        return value;
    }

    private void addAbilityInteraction(Turn turn, ArrayList<CombatEntity> abilityTargets) {
        for (CombatEntity target : abilityTargets) {
            if (getConditionsFulfilled(target)) {
                System.out.println("[DEBUG] Adding ability interaction from " + source.toString() + " via " + heroAbility.toString());
                AbilityInteraction newInteraction = new AbilityInteraction(source, target, heroAbility.getModifierType(), heroAbility.getAbilityType(), getValue(), turn);
                target.addQueuedAbilityInteraction(newInteraction);
            }
        }
    }

    public boolean isTriggeredOnDefense() {
        if (heroAbility == HeroAbility.MOMENT_OF_COURAGE
                || heroAbility == HeroAbility.CORROSIVE_SKIN
                || heroAbility == HeroAbility.RETURN) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isRetaliate() {
        if (heroAbility == HeroAbility.MOMENT_OF_COURAGE || heroAbility == HeroAbility.RETURN) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isDefensiveDebuff() {
        if (heroAbility == HeroAbility.CORROSIVE_SKIN)
            return true;
        else
            return false;
    }

    public boolean isTriggeredOnAttack() {
        if (heroAbility == HeroAbility.FURY_SWIPES) {
            return true;
        } else {
            return false;
        }
    }

    private void triggerRetaliate(CombatEntity target) {
        if (getConditionsFulfilled(target))
            target.addPendingStatChange(EntityStat.HEALTH, getValue());
    }

    public void triggerDefensiveSkill(CombatEntity target, Turn turn) {
        if (getConditionsFulfilled(target)) {
            //NOTE: Retaliate can trigger defensive skills
            if (heroAbility == HeroAbility.MOMENT_OF_COURAGE || heroAbility == HeroAbility.RETURN) {
                triggerRetaliate(target);
            }

            if (heroAbility == HeroAbility.CORROSIVE_SKIN) {
                target.addPendingStatChange(EntityStat.ATTACK, getValue()); //Remove 1 attack
            }

            AbilityInteraction ai = new AbilityInteraction(source, target, heroAbility.getModifierType(), heroAbility.getAbilityType(), getValue(), turn);
            target.addQueuedAbilityInteraction(ai);
        }
    }

    public void triggerOffensiveSkill(CombatEntity target, Turn turn) {
        if (getConditionsFulfilled(target)) {
            if (heroAbility == HeroAbility.FURY_SWIPES) {
                target.addPendingStatChange(EntityStat.ARMOR, getValue());
            }

            AbilityInteraction ai = new AbilityInteraction(source, target, heroAbility.getModifierType(), heroAbility.getAbilityType(), getValue(), turn);
            target.addQueuedAbilityInteraction(ai);
        }
    }

    public boolean getConditionsFulfilled(CombatEntity target) {
        switch (heroAbility.getAbilityRequirement()) {
            case TARGET_NON_CREEP:
                if (target.isHero() || target.isTower())
                    return true;
            case TARGET_TOWER:
                if (target.isTower())
                    return true;
            case TARGET_HERO:
                if (target.isHero())
                    return true;
            case BLOCKER_DIES:
                if (!target.isAlive())
                    return true;
            case DAMAGE_ABOVE_ZERO:
            case RANDOM_CHANCE:
            case HERO_BLOCKER_DIES:
            case ENEMY_NEIGHBOR_DIES:
            case NO_REQUIREMENT:
                return true;
            default:
                return false;
        }
    }

    public AbilityTargetType getAbilityTargetType() {
        return heroAbility.getTargetType();
    }
}
