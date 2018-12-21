package com.gmail.nossr50.datatypes.record;

import java.util.ArrayList;

import com.gmail.nossr50.datatypes.entity.EntityAlignment;

/*
 * I'm terrible with names
 * 
 * The purpose of this class is to hold the data of all simulations, to summarize and pull information from
 */
public class SumRecord {
	private static int totalSimulations = 0;
	private static ArrayList<SimRecord> simRecords = new ArrayList<SimRecord>(); //Copy of data from all simulations
	
	static public SimRecord makeRecord()
	{
		SimRecord newRecord = new SimRecord(totalSimulations);
		simRecords.add(newRecord);
		totalSimulations++;
		return newRecord;
	}
	
	public int getFactionStatsByTurn(EntityAlignment ea, Turn turn, RecordType stat)
	{
		int statSum = 0;
		
		//Compile the stats
		for(SimRecord curSimRecord : simRecords)
		{
			statSum += curSimRecord.getTurnStat(ea, turn, stat);
		}
		
		return statSum;
	}
	
	public int getRadiantStatsByTurn(Turn turn, RecordType stat)
	{
		return getFactionStatsByTurn(EntityAlignment.RADIANT, turn, stat);
	}
	
	public int getDireStatsByTurn(Turn turn, RecordType stat)
	{
		return getFactionStatsByTurn(EntityAlignment.DIRE, turn, stat);
	}
	
	/*
	public double getPercentage(EntityAlignment ea, int turnIndex, RecordType stat)
	{
		
	}
	*/
	
	static public double getStatAboveThreshold(EntityAlignment ea, Turn turn, RecordType stat, int threshold)
	{
		double sampleSize = simRecords.size();
		double statSum = 0.0; //Everytime the stat is true we add 1
		
		System.out.println("Number of samples: "+sampleSize);
		
		for(SimRecord curSim : simRecords)
		{
			int curStat = curSim.getTurnStat(ea, turn, stat);
			
			if(curStat >= threshold)
			{
				//System.out.println("Stat equal to or above threshold!");
				statSum+=1.0;
			} else {
				//System.out.println("Stat below threshold: "+ea.toString() +", turnindex: "+turn.getIndex() + ", "+ stat.toString() +", current value: " +curStat);
				//System.out.println("Derp2 Debug: " + SumRecord.getStatAboveThreshold(EntityAlignment.RADIANT, turn, RecordType.HERO_BLOCKS, 1));
			}
		}
		
		//# of samples
		
		
		double percentageAvg = statSum / sampleSize;
		
		System.out.println("Average value : "+percentageAvg);
		
		return percentageAvg;
	}
}
