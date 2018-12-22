package com.gmail.nossr50.datatypes.ability;

public enum AbilityTargetType {
	SELF,
	ATTACKER, //Entities attacking us
	SELF_AND_ATTACKER,
	BLOCKER, //Maybe should be reworded to defender?
	GLOBAL_ALLIES,
	GLOBAL_ENEMIES,
	ALLIED_NEIGHBORS,
	ALLIED_NEIGHBORS_AND_SELF,
	ENEMY_NEIGHBORS,
	RANDOM_ENEMY_NEIGHBOR,
	RANDOM_ENEMY,
	COMBAT_TARGET_NEIGHBORS, //Neighbors of combat target
}
