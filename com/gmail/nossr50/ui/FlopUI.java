package com.gmail.nossr50.ui;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.TableItem;

import com.gmail.nossr50.datatypes.entity.EntityAlignment;
import com.gmail.nossr50.datatypes.entity.Hero;
import com.gmail.nossr50.datatypes.record.RecordType;
import com.gmail.nossr50.datatypes.record.SumRecord;
import com.gmail.nossr50.datatypes.record.Turn;
import org.jetbrains.annotations.NotNull;

/**
 * Helper class for easily updating specific UI elements
 * @author nossr50
 *
 */
public class FlopUI {
	private HashMap<StatElement, TableItem> tableElements;
	
	/**
	 * Initialize our reference maps
	 */
	public FlopUI()
	{
		tableElements = new HashMap<>();
	}
	
	/**
	 * Changes the text field for the appropriate TabItem
	 * @param se The target TabItem to edit
	 * @param index The index of the TabItem to edit
	 * @param newString The new string
	 */
	public void updateTabItem(StatElement se, int index, String newString)
	{
		if(tableElements.get(se) == null)
			System.out.println("Table item is null");
		
		TableItem tabItemRef = tableElements.get(se); //Grab ref
		tabItemRef.setText(index, newString); //Update text
		tabItemRef.getParent().update(); //Update parent widget
	}
	
	/**
	 * Registers SWT TableItem widgets so we can update them easily
	 * @param se The identifier of the widget
	 * @param ti The TableItem to register
	 */
	public void registerTabElement(StatElement se, TableItem ti)
	{
		tableElements.put(se, ti);
	}
	
	private void updateComboText(ArrayList<Combo> heroDropDownWidgets)
	{
		//Fill in the text
		for(Combo c : heroDropDownWidgets)
		{
			int pos = 0;
			for(Hero vals : Hero.values())
			{
				c.add(vals.toString() +" "+pos, pos);
				pos++;
			}
			
			c.update();
		}
		
		/*
		 * Hard coded presets for now for testing purposes
		 */
		heroDropDownWidgets.get(0).select(27); //Drow
		heroDropDownWidgets.get(1).select(3); //Centaur
		heroDropDownWidgets.get(2).select(28); //Enchantress
		
		heroDropDownWidgets.get(3).select(11); //Ursa
		heroDropDownWidgets.get(4).select(5); //Legion
		heroDropDownWidgets.get(5).select(35); //Viper
	}
	
	/**
	 * Setup our UI elements to our preferred defaults
	 * @param heroDropDownWidgets Combo widgets representing Hero selection for the Sim
	 */
	public void initWidgets(@NotNull ArrayList<Combo> heroDropDownWidgets)
	{
		updateComboText(heroDropDownWidgets);
	}
	
	public void updateStatWidgets()
	{
		//Update 3 hero blocked stat
		double heroBlockStatPerc = SumRecord.getStatAboveThreshold(EntityAlignment.RADIANT, Turn.ON_FLOP, RecordType.HERO_BLOCKS, 3);
		updateTabItem(StatElement.HEROS_BLOCKED, 1, formattedPercentage(heroBlockStatPerc));
	}
	
	public String formattedPercentage(double percentage)
	{
		NumberFormat defFormat = NumberFormat.getPercentInstance();
		defFormat.setMinimumFractionDigits(1);
		return defFormat.format(percentage);
	}

}
