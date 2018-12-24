package com.gmail.nossr50.datatypes.entity;

import com.gmail.nossr50.datatypes.ability.HeroAbility;

/*
 * Define heros and their properties
 */
public enum Hero {
	
	/*
	 * RED HEROS
	 */
	
	AXE(6, 2, 10),
	BEASTMASTER(5, 0, 12),
	BRISTLEBACK(8, 0, 12, HeroAbility.BARROOM_BRAWLER),
	CENTAUR_WARRUNNER(4, 0, 14, HeroAbility.RETURN),
	KEEF(6, 1, 11),
	LEGION_COMMANDER(6, 1, 11, HeroAbility.MOMENT_OF_COURAGE),
	MAZZIE(6, 3, 6),
	PUGNA(6, 0, 9),
	SVEN(5, 0, 11, HeroAbility.GREAT_CLEAVE),
	TIDEHUNTER(2, 1, 18),
	TIMBERSAW(4, 0, 11, HeroAbility.REACTIVE_ARMOR),
	URSA(7, 0, 10),
	
	/*
	 * BLUE HEROS
	 */
	
	CRYSTAL_MAIDEN(2, 0, 5),
	EARTHSHAKER(2, 0, 7),
	JMUY(3, 0, 8),
	KANNA(2, 0, 12),
	LUNA(3, 0, 8, HeroAbility.LUCENT_BEAM),
	MEEPO(4, 0, 5),
	OGRE_MAGI(3, 0, 7),
	OUTWORLD_DEVOURER(4, 0, 7),
	PRELLEX(3, 0, 5),
	SKYWRATH_MAGE(3, 0, 6),
	VENOMANCER(2, 0, 6),
	ZEUS(3, 0, 7),
	
	/*
	 * GREEN HEROS
	 */
	
	ABADDON(4, 0, 9),
	CHEN(4, 0, 9),
	DARK_SEER(5, 0, 9),
	DROW_RANGER(4, 0, 9, HeroAbility.PRECISION_AURA),
	ENCHANTRESS(4, 0, 8, HeroAbility.NATURES_ATTENDANTS),
	FARVHAN(4, 0, 10, HeroAbility.PACK_LEADERSHIP),
	LYCAN(4, 0, 10, HeroAbility.FERAL_IMPULSE),
	MAGNUS(4, 1, 9),
	OMNIKNIGHT(5, 0, 12),
	RIX(3, 0, 7),
	TREANT_PROTECTOR(4, 0, 10, HeroAbility.BRANCHES_OF_IRON),
	VIPER(4, 0, 10, HeroAbility.CORROSIVE_SKIN),
	
	/*
	 * BLACK HEROS
	 */
	
	BLOODSEEKER(7, 0, 7, HeroAbility.BLOOD_BATH),
	BOUNTY_HUNTER(7, 0, 7, HeroAbility.JINADA),
	DEBBI(7, 0, 5, HeroAbility.METICULOUS_PLANNER),
	LICH(5, 0, 9),
	LION(6, 0, 5),
	NECROPHOS(5, 0, 6, HeroAbility.SADIST),
	PHANTOM_ASSASIN(6, 0, 8, HeroAbility.EFFICIENT_KILLER),
	SNIPER(5, 0, 6),
	SORLA(8, 0, 6, HeroAbility.WARMONGER),
	STORM_SPIRIT(4, 0, 6),
	TINKER(7, 0, 5),
	WINTER_WYVERN(6, 0, 6);
	
	//Stats
	public int heroHealth;
	public int heroArmor;
	public int heroAttack;
	
	//Abilities
	public HeroAbility at = HeroAbility.MISSING_ABILITY; //Default to nothing
	
	Hero(int heroAttack, int heroArmor, int heroHealth)
	{
		this.heroAttack = heroAttack;
		this.heroArmor = heroArmor;
		this.heroHealth = heroHealth;
	}
	
	Hero(int heroAttack, int heroArmor, int heroHealth, HeroAbility at)
	{
		this.heroAttack = heroAttack;
		this.heroArmor = heroArmor;
		this.heroHealth = heroHealth;
		this.at = at;
	}
}
