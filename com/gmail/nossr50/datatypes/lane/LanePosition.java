package com.gmail.nossr50.datatypes.lane;

import java.util.ArrayList;

import com.gmail.nossr50.datatypes.ability.Ability;
import com.gmail.nossr50.datatypes.ability.AbilityTargetType;
import com.gmail.nossr50.datatypes.entity.CombatEntity;
import com.gmail.nossr50.datatypes.entity.LaneEntity;
import com.gmail.nossr50.flopsim.SimTools;
import org.jetbrains.annotations.NotNull;

public class LanePosition {
    private final CombatEntity self; //Entity in this position (not sure if needed)



    private ArrayList<CombatEntity> alliedNeighbors; //Neighbors that are allies
    private ArrayList<CombatEntity> enemyNeighbors; //Neighbors that are not so nice
    private ArrayList<CombatEntity> enemiesInLane; //All other enemies in the lane

    public LanePosition(CombatEntity entity) {
        this.self = entity;

        //Init array lists
        alliedNeighbors = new ArrayList<>();
        enemyNeighbors = new ArrayList<>();
        enemiesInLane = new ArrayList<>();
    }

    public void addAlliedNeighbor(CombatEntity ce) {
        alliedNeighbors.add(ce);
    }

    /**
     * Adds only valid non-arrow neighbors
     *
     * @param potentialNeighbor The neighbor to check
     */
    public void addPotentialAlliedNeighbor(LaneEntity potentialNeighbor) {
        System.out.println("Debug: Attempting to add ally neighbor for " + self.toString());
        if (potentialNeighbor instanceof CombatEntity) {
            //Neighbor isn't an arrow
            CombatEntity newNeighbor = (CombatEntity) potentialNeighbor;
            alliedNeighbors.add(newNeighbor);
            System.out.println("Added allied neighbor [" + newNeighbor.toString() + "] for [" + self.toString() + "]");
        }
    }

    /**
     * Adds only valid non-arrow neighbors
     *
     * @param potentialNeighbor The neighbor to check
     */
    public void addPotentialEnemyNeighbor(LaneEntity potentialNeighbor) {
        System.out.println("Debug: Attempting to add enemy neighbor for " + self.toString());
        if (potentialNeighbor instanceof CombatEntity) {
            //Neighbor isn't an arrow
            CombatEntity newNeighbor = (CombatEntity) potentialNeighbor;
            enemyNeighbors.add(newNeighbor);
            System.out.println("Added enemy neighbor [" + newNeighbor.toString() + "] for [" + self.toString() + "]");
        }
    }

    public void addPotentialEnemyInThisLane(LaneEntity potentialNeighbor) {
        System.out.println("Debug: Attempting to add lane enemies for " + self.toString());
        if (potentialNeighbor instanceof CombatEntity) {
            //Neighbor isn't an arrow
            CombatEntity newNeighbor = (CombatEntity) potentialNeighbor;
            enemiesInLane.add(newNeighbor);
            System.out.println("Added enemy in lane [" + newNeighbor.toString() + "] for [" + self.toString() + "]");
        }
    }

    @NotNull
    public void getTargetEntities(@NotNull ArrayList<CombatEntity> targetEntities, AbilityTargetType abilityTargetType) {
        System.out.println("Grabbing targets");
        @NotNull CombatEntity currentTarget = self.getCombatTarget().getTarget();

        switch (abilityTargetType) {
            case SELF_AND_ATTACKER:
                targetEntities.addAll(getAttackers());
                targetEntities.add(self);
                break;
            case ALLIED_NEIGHBORS:
                targetEntities.addAll(alliedNeighbors);
                break;
            case ALLIED_NEIGHBORS_AND_SELF:
                targetEntities.addAll(alliedNeighbors);
                targetEntities.add(self);
                break;
            case ATTACKER:
                targetEntities.addAll(getAttackers());
                break;
            case BLOCKER:
                targetEntities.add(currentTarget);
                break;
            case COMBAT_TARGET_NEIGHBORS:
                //Add only the neighbors of our target (used for cleave)
                if (!self.getCombatTarget().isTargetTower())
                    targetEntities.addAll(currentTarget.getLanePosition().getAlliedNeighbors());
                break;
            case ENEMY_NEIGHBORS:
                targetEntities.addAll(getEnemyNeighbors());
                break;
            case RANDOM_ENEMY_NEIGHBOR:
                targetEntities.addAll(getRandom(getEnemyNeighbors()));
                break;
            case GLOBAL_ALLIES:
                //TODO: Fix this for drow
                break;
            case GLOBAL_ENEMIES:
                //TODO: needed?
                break;
            case RANDOM_ENEMY:
                targetEntities.addAll(getRandom(enemiesInLane));
                break;
            case SELF:
                targetEntities.add(self);
                break;
            default:
                break;
        }
        //TODO: Add better debugging?
        System.out.println("Targets found: " + targetEntities.size() +" for ability ");
    }

    @NotNull
    private ArrayList<CombatEntity> getAttackers() {
        ArrayList<CombatEntity> attackers = new ArrayList<>();

        for (CombatEntity ce : getEnemyNeighbors()) {
            if (ce.getCombatTarget().getTarget() == self) {
                attackers.add(ce);
            }
        }

        return attackers;
    }

    //TODO: Check randomness
    @NotNull
    private ArrayList<CombatEntity> getRandom(ArrayList<CombatEntity> potentialTargets) {
        ArrayList<CombatEntity> randomTarget = new ArrayList<>();

        int randomChoice = SimTools.getRandom(0, potentialTargets.size() - 1);

        randomTarget.add(potentialTargets.get(randomChoice));

        return randomTarget;
    }

//    public CombatEntity getCombatEntity() {
//        return self;
//    }

    public ArrayList<CombatEntity> getAlliedNeighbors() {
        return alliedNeighbors;
    }

    public ArrayList<CombatEntity> getEnemyNeighbors() {
        return enemyNeighbors;
    }

    public ArrayList<CombatEntity> getEnemiesInLane() {
        return enemiesInLane;
    }
}
