package com.gmail.nossr50.flopsim.combat;

import com.gmail.nossr50.datatypes.entity.CombatEntity;
import com.gmail.nossr50.datatypes.record.Turn;

public class CombatRecord {
    public final CombatEntity source;
    public final CombatEntity recipient;
    public final Turn currentTurn;
    private int damageDealt;

    public CombatRecord(CombatEntity source, CombatEntity recipient, Turn currentTurn, int damageDealt)
    {
        this.source = source;
        this.recipient = recipient;
        this.currentTurn = currentTurn;
        this.damageDealt = damageDealt;
    }

    public void addDamage(int newDamage)
    {
        damageDealt+=newDamage;
    }

    public int getDamageDealt()
    {
        return damageDealt;
    }
}
