package com.gmail.nossr50.flopsim;

import com.gmail.nossr50.datatypes.entity.CombatEntity;
import com.gmail.nossr50.datatypes.lane.Lane;
import com.gmail.nossr50.datatypes.record.SimRecord;
import com.gmail.nossr50.datatypes.record.SumRecord;

/**
 * Represents the artifact game board
 * @author nossr50
 *
 */
public class Board {
	
	public Lane[] lanes; //The three lanes
	
	public Board()
	{
		lanes = new Lane[3]; //Init empty array
		
		//Make a new SimRecord
		SimRecord newSimRecord = SumRecord.makeRecord();
		
		//Initialize the lanes
		lanes[0] = new Lane(newSimRecord);
		lanes[1] = new Lane(newSimRecord);
		lanes[2] = new Lane(newSimRecord);
	}
	
	public void addEntitiesToLane(int pos, CombatEntity ce)
	{
		lanes[pos].AddCombatEntity(ce); //Adding entity
	}
	
	public Lane getLane(int lanePos) { return lanes[lanePos]; }

}
