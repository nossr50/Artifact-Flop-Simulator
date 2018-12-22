package com.gmail.nossr50.datatypes.record;

import com.gmail.nossr50.datatypes.entity.EntityAlignment;
import org.jetbrains.annotations.NotNull;

public class SimRecord {
	final int simRecordId;
	private int curTurn;
	
	/*
	 * Store records by lane
	 */
	
	private LaneRecord laneRecords;
	

	
	public SimRecord(int simRecordId)
	{
		//Init lane records
		this.simRecordId = simRecordId;
		laneRecords = new LaneRecord();
		curTurn = 0;
	}
	
	public void addStat(EntityAlignment ea, @NotNull RecordType rt, int addStat)
	{
		laneRecords.getRecord(ea, getTurn(curTurn)).addStat(rt, addStat);
		System.out.println(rt.toString() + " [" + addStat + "] : Stat recorded");
	}
	
	/**
	 * Get stat for a specific turn
	 * @param ea Specifies whether the stat is for Radiant or Dire side
	 * @param turn Specifies which turns data to grab
	 * @param stat Specifies which stat we want data on
	 * @return The integer representing the stat for that turn
	 */
	public int getTurnStat(EntityAlignment ea, @NotNull Turn turn, @NotNull RecordType stat)
	{
		return laneRecords.getRecord(ea, turn).getStat(stat);
	}
	
	
	public int getAllTurnSumStat(EntityAlignment ea, @NotNull RecordType stat)
	{
		int allTurnSum = 0;
		allTurnSum += laneRecords.getRecord(ea, Turn.ON_FLOP).getStat(stat); //First turn
		allTurnSum += laneRecords.getRecord(ea, Turn.TURN_AFTER_FLOP).getStat(stat); //Second turn
		return allTurnSum;
	}
	
	@NotNull
    public Turn getTurn(int turnIndex) {
		if(turnIndex == 0)
			return Turn.ON_FLOP;
		else
			return Turn.TURN_AFTER_FLOP;
	}
	
	public void advanceTurn() { curTurn+=1; }
}
