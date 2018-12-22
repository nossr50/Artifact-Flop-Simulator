package com.gmail.nossr50.datatypes.ability;

import com.gmail.nossr50.datatypes.entity.CombatEntity;
import com.gmail.nossr50.flopsim.combat.AbilityInteraction;

public class Aura {
    public final CombatEntity auraSource;
    public final AbilityInteraction auraInteraction;

    public Aura(AbilityInteraction auraInteraction) {
        this.auraInteraction = auraInteraction;
        this.auraSource = auraInteraction.source;
    }
}
