package com.gmail.nossr50.datatypes.ability;

public enum AbilityRequirement {
    HERO_BLOCKER_DIES,
    BLOCKER_DIES,
    ENEMY_NEIGHBOR_DIES,
    DAMAGE_ABOVE_ZERO, //Damage is above 0 after reductions
    RANDOM_CHANCE, //This refers to random chance to activate (Jinada)
    TARGET_HERO,
    TARGET_TOWER,
    TARGET_NON_CREEP, //Towers & Heros
    NO_REQUIREMENT
}
