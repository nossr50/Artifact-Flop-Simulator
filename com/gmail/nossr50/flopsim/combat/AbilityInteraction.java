package com.gmail.nossr50.flopsim.combat;

import com.gmail.nossr50.datatypes.ability.AbilityModifierType;
import com.gmail.nossr50.datatypes.ability.AbilityType;
import com.gmail.nossr50.datatypes.ability.InvalidAbilityException;
import com.gmail.nossr50.datatypes.entity.CombatEntity;
import com.gmail.nossr50.datatypes.entity.EntityStat;
import com.gmail.nossr50.datatypes.record.Turn;
import com.gmail.nossr50.flopsim.SimTools;

import static com.gmail.nossr50.flopsim.SimTools.getDebugFormat;

public class AbilityInteraction {
    public CombatEntity source; //Source of the interaction (usually the one that dealt this damage)
    public CombatEntity recipient; //The one on the receiving end of this interaction
    public AbilityType at;
    public AbilityModifierType amt;
    public Turn turn;
    public int abilityValue;

    /**
     * @param source    The source of this ability
     * @param recipient The target of this ability
     * @param amt       The type of ability interaction
     */
    public AbilityInteraction(CombatEntity source, CombatEntity recipient, AbilityModifierType amt, AbilityType at, int abilityValue, Turn turn) {
        this.source = source; //Source
        this.recipient = recipient; //Target
        this.at = at; //The type of interaction (special or normal)
        this.amt = amt; //The method of interaction
        this.turn = turn; //Which turn this happened on
        this.abilityValue = abilityValue;

        //TODO: Remove this debug
        System.out.println("[Debug] new Ability Interaction: "
                + getDebugFormat("Source", source.toString())
                + getDebugFormat("Target", recipient.toString())
                + getDebugFormat("AT", at.toString())
                + getDebugFormat("AMT", amt.toString())
                + getDebugFormat("TURN", turn.toString()
                + getDebugFormat("Ability Value", String.valueOf(abilityValue))));
    }

    /**
     * Get the target stat of an ability interaction
     * @return the target stat of an ability interaction
     * @throws InvalidAbilityException
     */
    public EntityStat getTargetStat() throws InvalidAbilityException {
        switch (amt) {
            case AURA_ARMOR:
                throw new InvalidAbilityException("Aura Armor is not a valid target stat!");
            case AURA_ATTACK:
                throw new InvalidAbilityException("Aura Attack is not a valid target stat!");
            case AURA_REGEN:
                throw new InvalidAbilityException("Aura Regen is not a valid target stat!");
            case MODIFY_ARMOR:
                return EntityStat.ARMOR;
            case MODIFY_HEALTH:
                return EntityStat.HEALTH;
            case MODIFY_ATTACK:
                return EntityStat.ATTACK;
            case REGEN:
                return EntityStat.REGEN;
            case RETALIATE:
                return EntityStat.HEALTH;
            case CLEAVE:
                return EntityStat.HEALTH;
            case PIERCE_DAMAGE:
                return EntityStat.HEALTH;
            default:
                throw new InvalidAbilityException("Ability must be specified");
        }
    }

}
