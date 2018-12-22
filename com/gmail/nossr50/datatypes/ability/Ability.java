package com.gmail.nossr50.datatypes.ability;

import java.util.ArrayList;

import com.gmail.nossr50.datatypes.entity.CombatEntity;
import com.gmail.nossr50.datatypes.entity.EntityStat;
import com.gmail.nossr50.datatypes.lane.LanePosition;
import com.gmail.nossr50.datatypes.record.Turn;
import com.gmail.nossr50.flopsim.combat.AbilityInteraction;
import org.jetbrains.annotations.NotNull;

public class Ability {

    public final HeroAbility heroAbility;
    private final CombatEntity source; //owner


    public Ability(CombatEntity source, HeroAbility at) {
        this.source = source;
        this.heroAbility = at;
    }

    /**
     * Returns the current value modifier of the ability
     *
     * @return Ability's value modifier
     */
    private int getValue() {
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
    public void addAbilityInteraction(@NotNull Turn turn) {
        LanePosition lp = source.getLanePosition(); //LanePosition has positional data relating to abilities

        //DEBUG
        if(lp == null)
            System.out.println("LP is null!");
        if(lp.getEnemyNeighbors() == null)
            System.out.println("Enemy neighbors are null");
        if(lp.getAlliedNeighbors() == null)
            System.out.println("Allied neighbors are null");
        if(lp.getEnemiesInLane() == null)
            System.out.println("Enemies in lane are null");

        ArrayList<CombatEntity> abilityTargets = new ArrayList<>();
        //This grabs all applicable targets for an ability, and its empty if there are none.
        lp.getTargetEntities(abilityTargets, this.getAbilityTargetType());

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

    private void addReactiveArmorInteraction(@NotNull Turn turn, ArrayList<CombatEntity> abilityTargets) {
        AbilityInteraction newInteraction;
        int reactiveArmorValue = -1;

        for (CombatEntity target : abilityTargets) {
            System.out.println("[DEBUG] Adding ability interaction from " + source.toString() + " via " + heroAbility.toString());

            //Apply -1 armor to attackers
            if (target != source)
                newInteraction = new AbilityInteraction(source, target, heroAbility.getModifierType(), heroAbility.getAbilityType(), reactiveArmorValue, turn);
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

    private void addAbilityInteraction(@NotNull Turn turn, ArrayList<CombatEntity> abilityTargets) {
        for (CombatEntity target : abilityTargets) {
            if (getConditionsFulfilled(target)) {
                System.out.println("[DEBUG] Adding ability interaction from " + source.toString() + " via " + heroAbility.toString());
                AbilityInteraction newInteraction = new AbilityInteraction(source, target, heroAbility.getModifierType(), heroAbility.getAbilityType(), getValue(), turn);
                target.addQueuedAbilityInteraction(newInteraction);
            }
        }
    }

    public boolean isTriggeredOnDefense() {
        return heroAbility == HeroAbility.MOMENT_OF_COURAGE
                || heroAbility == HeroAbility.CORROSIVE_SKIN
                || heroAbility == HeroAbility.RETURN;
    }

    public boolean isRetaliate() {
        return heroAbility == HeroAbility.MOMENT_OF_COURAGE || heroAbility == HeroAbility.RETURN;
    }

    public boolean isDefensiveDebuff() {
        return heroAbility == HeroAbility.CORROSIVE_SKIN;
    }

    public boolean isTriggeredOnAttack() {
        return heroAbility == HeroAbility.FURY_SWIPES;
    }

    private void triggerRetaliate(@NotNull CombatEntity target) {
        if (getConditionsFulfilled(target))
            target.addPendingStatChange(EntityStat.HEALTH, getValue());
    }

    public void triggerDefensiveSkill(@NotNull CombatEntity target, @NotNull Turn turn) {
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

    public void triggerOffensiveSkill(@NotNull CombatEntity target, @NotNull Turn turn) {
        if (getConditionsFulfilled(target)) {
            if (heroAbility == HeroAbility.FURY_SWIPES) {
                target.addPendingStatChange(EntityStat.ARMOR, getValue());
            }

            AbilityInteraction ai = new AbilityInteraction(source, target, heroAbility.getModifierType(), heroAbility.getAbilityType(), getValue(), turn);
            target.addQueuedAbilityInteraction(ai);
        }
    }

    private boolean getConditionsFulfilled(@NotNull CombatEntity target) {
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
