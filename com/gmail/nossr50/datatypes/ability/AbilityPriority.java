package com.gmail.nossr50.datatypes.ability;

/**
 * This represents the speed of an interaction
 * When applying abilities we need to know when to trigger their effects
 */
public enum AbilityPriority {
	GLOBAL_BUFF,
	LANE_BUFF,
	CONDITIONAL_COMBAT_TARGET_BUFF,
	ENTERING_PRE_ACTION,
	WHILE_DEALING_DAMAGE,
	AFTER_DAMAGE_DEALT,
	AFTER_COMBAT_ENDS
}
