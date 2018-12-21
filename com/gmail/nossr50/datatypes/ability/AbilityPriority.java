package com.gmail.nossr50.datatypes.ability;

public enum AbilityPriority {
	DAMAGE_DEALT, //Happens before the end of combat
	END_OF_COMBAT, //Happens before we check if heros died (Regens)
	AFTER_COMBAT, //Happens after heros have died
	PRE_ACTION, //before pre-combat
	PRE_COMBAT, //before combat
	ON_DEPLOY, //If the hero is on the board its applied
	NEVER; //Never applied
}
