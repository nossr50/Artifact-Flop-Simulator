package com.gmail.nossr50.datatypes.record;

import org.jetbrains.annotations.NotNull;

/**
 * Records data for a specific turn
 * @author nossr50
 *
 */
public class TurnRecord {
	private int turnNumber;
	private int herosBlocked;
	private int creepKills;
	private int heroKills;
	private int goldGained;
	private int towerDmg;
	private int heroDmg;
	private int heroDeaths;
	
	public TurnRecord(int turnNumber)
	{
		this.turnNumber = turnNumber;
		
		herosBlocked = 0;
		creepKills = 0;
		heroKills = 0;
		goldGained = 0;
		towerDmg = 0;
		heroDmg = 0;
		heroDeaths = 0;
	}
	
	public void addBlocked(int x) { herosBlocked+=x; }
	public void addGoldGained(int x) { goldGained+=x; }
	public void addTowerDmg(int x) { towerDmg+=x; }
	public void addHeroDmg(int x) { heroDmg+=x; }
	public void addHeroDeaths(int x) { heroDeaths+=x; }
	public void addCreepKills(int x) { creepKills+=x; }
	public void addHeroKills(int x) { heroKills+=x; }
	
	public void addStat(@NotNull RecordType stat, int x)
	{
		switch(stat)
		{
		case CREEP_KILLS:
			addCreepKills(x);
			break;
		case GOLD_GAINED:
			addGoldGained(x);
			break;
		case HERO_BLOCKS:
			addBlocked(x);
			//System.out.println("DEBUG: !!!!!!!!!!! + "+herosBlocked);
			//System.out.println("Derp Debug: " + SumRecord.getStatAboveThreshold(EntityAlignment.RADIANT, 0, RecordType.HERO_BLOCKS, 1));
			break;
		case HERO_DEATHS:
			addHeroDeaths(x);
			break;
		case HERO_DMG:
			addHeroDmg(x);
			break;
		case HERO_KILLS:
			addHeroKills(x);
			break;
		case TOWER_DMG:
			addTowerDmg(x);
			break;
		default:
			break;
		}
	}
	
	public int getStat(@NotNull RecordType stat)
	{
		switch(stat)
		{
		case CREEP_KILLS:
			return creepKills;
		case GOLD_GAINED:
			return goldGained;
		case HERO_BLOCKS:
			return herosBlocked;
		case HERO_DEATHS:
			return heroDeaths;
		case HERO_DMG:
			return heroDmg;
		case HERO_KILLS:
			return heroKills;
		case TOWER_DMG:
			return towerDmg;
		default:
			System.out.println("This case should never be reached!");
			return 0;
		}
	}
}
