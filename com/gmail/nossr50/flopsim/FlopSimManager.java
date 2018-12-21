package com.gmail.nossr50.flopsim;

import java.util.ArrayList;

import com.gmail.nossr50.datatypes.entity.Hero;

public class FlopSimManager {

	public void StartFlopSim(int simSampleSize, ArrayList<Hero> oppFlop, ArrayList<Hero> friendlyFlop)
	{
		System.out.println("Starting simulation, sample size: "+simSampleSize);
		for(int x = 0; x < simSampleSize; x++)
		{
			System.out.println("Simulation count: " + x);
			Game newGame = new Game(x, oppFlop, friendlyFlop);
			newGame.executeGame();
		}
	}
}
