package com.gmail.nossr50.datatypes.ability;

import static com.gmail.nossr50.datatypes.ability.AbilityModifierType.*;
import static com.gmail.nossr50.datatypes.ability.AbilityPriority.*;
import static com.gmail.nossr50.datatypes.ability.AbilityTargetType.*;
import static com.gmail.nossr50.datatypes.ability.AbilityType.*;
import static com.gmail.nossr50.datatypes.ability.AbilityRequirement.*;

public enum HeroAbility {
    MISSING_ABILITY(null, null, null, null, null, 0), //Default
    BARROOM_BRAWLER(NORMAL, SELF, MODIFY_ARMOR, AFTER_COMBAT_ENDS, HERO_BLOCKER_DIES, 2), //BRISTLE
    RETURN(NORMAL, ATTACKER, RETALIATE, WHILE_DEALING_DAMAGE, NO_REQUIREMENT, 2), //CENTAUR
    MOMENT_OF_COURAGE(NORMAL, ATTACKER, RETALIATE, WHILE_DEALING_DAMAGE, NO_REQUIREMENT, 2), //LC
    GREAT_CLEAVE(NORMAL, COMBAT_TARGET_NEIGHBORS, CLEAVE, WHILE_DEALING_DAMAGE, NO_REQUIREMENT, 0), //SVEN
    REACTIVE_ARMOR(SPECIAL, SELF_AND_ATTACKER, AURA_ARMOR, LANE_BUFF, NO_REQUIREMENT, 1), //TIMBER
    FURY_SWIPES(NORMAL, BLOCKER, MODIFY_ARMOR, AFTER_DAMAGE_DEALT, DAMAGE_ABOVE_ZERO, -1), //URSA
    LUCENT_BEAM(NORMAL, RANDOM_ENEMY, PIERCE_DAMAGE, ENTERING_PRE_ACTION, NO_REQUIREMENT, 1), //LUNA
    BLOOD_BATH(NORMAL, SELF, REGEN, AFTER_COMBAT_ENDS, BLOCKER_DIES, 0), //BloodCyka
    JINADA(NORMAL, SELF, AURA_ATTACK, LANE_BUFF, RANDOM_CHANCE, 4), //BountyHunter
    METICULOUS_PLANNER(NORMAL, BLOCKER, AURA_ATTACK, CONDITIONAL_COMBAT_TARGET_BUFF, TARGET_NON_CREEP, 2), //Debbie
    SADIST(NORMAL, ENEMY_NEIGHBORS, MODIFY_HEALTH, AFTER_COMBAT_ENDS, ENEMY_NEIGHBOR_DIES, 1), //Necro
    EFFICIENT_KILLER(NORMAL, BLOCKER, AURA_ATTACK, CONDITIONAL_COMBAT_TARGET_BUFF, TARGET_HERO, 4), //PA
    WARMONGER(NORMAL, BLOCKER, AURA_ATTACK, CONDITIONAL_COMBAT_TARGET_BUFF, TARGET_TOWER, 4), //Sorla
    PRECISION_AURA(NORMAL, GLOBAL_ALLIES, AURA_ATTACK, GLOBAL_BUFF, NO_REQUIREMENT, 1), //Drow
    NATURES_ATTENDANTS(NORMAL, ALLIED_NEIGHBORS_AND_SELF, AURA_REGEN, LANE_BUFF, NO_REQUIREMENT, 2), //Enchantress
    PACK_LEADERSHIP(NORMAL, ALLIED_NEIGHBORS, AURA_ARMOR, LANE_BUFF, NO_REQUIREMENT, 1), //Farvhan
    FERAL_IMPULSE(NORMAL, ALLIED_NEIGHBORS, AURA_ATTACK, LANE_BUFF, NO_REQUIREMENT, 2), //Lycan
    CORROSIVE_SKIN(NORMAL, ATTACKER, MODIFY_ATTACK, AFTER_DAMAGE_DEALT, DAMAGE_ABOVE_ZERO, -1), //Viper
    BRANCHES_OF_IRON(NORMAL, ALLIED_NEIGHBORS, AURA_ARMOR, LANE_BUFF, NO_REQUIREMENT, 2); //Treant

    private final AbilityType at;
    private final AbilityTargetType att;
    private final AbilityModifierType amt;
    private final AbilityPriority ap;
    private final AbilityRequirement ar;
    private final int value;

    HeroAbility(AbilityType at, AbilityTargetType att, AbilityModifierType amt, AbilityPriority ap, AbilityRequirement ar, int value) {
        this.at = at;
        this.att = att;
        this.amt = amt;
        this.value = value;
        this.ap = ap;
        this.ar = ar;
    }

    public AbilityType getAbilityType() {
        return at;
    }

    public AbilityTargetType getTargetType() {
        return att;
    }

    public AbilityRequirement getAbilityRequirement() {
        return ar;
    }

    public AbilityModifierType getModifierType() {
        return amt;
    }

    public AbilityPriority getPriority() {
        return ap;
    }

    public boolean isAbilitySpecial() {
        return at == SPECIAL;
    }

    public int getValue() {
        return value;
    }
}
