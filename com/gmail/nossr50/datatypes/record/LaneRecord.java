package com.gmail.nossr50.datatypes.record;

import java.util.HashMap;

import com.gmail.nossr50.datatypes.entity.EntityAlignment;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a lane in our records
 * @author nossr50
 *
 */
public class LaneRecord {
	
	private HashMap<Integer, TurnRecord> radiantRecords;
	private HashMap<Integer, TurnRecord> direRecords;
	
	public LaneRecord()
	{
		//Init maps
		radiantRecords 	= new HashMap<>();
		direRecords 	= new HashMap<>();
		
		/*
		 * Init records
		 */
		
		//First turn
		radiantRecords.put(0, new TurnRecord(0)); //First turn
		direRecords.put(0, new TurnRecord(0)); //First turn
		
		//Second turn
		radiantRecords.put(1, new TurnRecord(1)); //Second turn
		direRecords.put(1, new TurnRecord(1)); //Second turn
	}
	
	public TurnRecord getRecord(EntityAlignment ea, @NotNull Turn turn)
	{
		return getRecord(ea, turn.getIndex());
	}
	
	private TurnRecord getRecord(EntityAlignment ea, int index)
	{
		if(ea == EntityAlignment.RADIANT)
		{
			return radiantRecords.get(index);
		} else {
			return direRecords.get(index);
		}
	}
	
	private TurnRecord getRadiantRecord(int index)
	{
		return radiantRecords.get(index);
	}
	
	private TurnRecord getDireRecord(int index)
	{
		return direRecords.get(index);
	}
	
	public TurnRecord getRadiantRecord(@NotNull Turn turn)
	{
		return getRadiantRecord(turn.getIndex());
	}
	
	public TurnRecord getDireRecord(@NotNull Turn turn)
	{
		return getDireRecord(turn.getIndex());
	}
}
