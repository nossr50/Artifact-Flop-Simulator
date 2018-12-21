package com.gmail.nossr50;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.gmail.nossr50.datatypes.entity.Hero;
import com.gmail.nossr50.datatypes.record.SumRecord;
import com.gmail.nossr50.flopsim.FlopSimManager;
import com.gmail.nossr50.ui.FlopUI;
import com.gmail.nossr50.ui.StatElement;

public class FlopWindow {

	protected Shell shlArtifactFlopSim;
	private Table table;
	private SumRecord sumRecord;
	private Table table_1;
	private int simCount = 1; //Number of sims to run

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			FlopWindow window = new FlopWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlArtifactFlopSim.open();
		shlArtifactFlopSim.layout();
		while (!shlArtifactFlopSim.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		FlopUI flopUI = new FlopUI(); //Helper class
		sumRecord = new SumRecord();
		
		shlArtifactFlopSim = new Shell();
		shlArtifactFlopSim.setSize(1280, 720);
		shlArtifactFlopSim.setText("Artifact Flop Sim by nossr50");
		
		/*
		 * init our dropdowns
		 */
		ArrayList<Combo> heroDropDownWidgets = new ArrayList<Combo>();
		
		Menu menu = new Menu(shlArtifactFlopSim, SWT.BAR);
		shlArtifactFlopSim.setMenuBar(menu);
		
		MenuItem mntmMenu = new MenuItem(menu, SWT.CASCADE);
		mntmMenu.setText("Menu");
		
		Menu menu_1 = new Menu(mntmMenu);
		mntmMenu.setMenu(menu_1);
		
		TabFolder tabFolder = new TabFolder(shlArtifactFlopSim, SWT.NONE);
		tabFolder.setBounds(0, 0, 1264, 661);
		
		TabItem tbtmSim = new TabItem(tabFolder, SWT.NONE);
		tbtmSim.setText("Sim");
		
		Group grpFlopConfig = new Group(tabFolder, SWT.NONE);
		tbtmSim.setControl(grpFlopConfig);
		grpFlopConfig.setText("Flop Config");
		
		Label lblOpponentHeros = new Label(grpFlopConfig, SWT.NONE);
		lblOpponentHeros.setBounds(317, 22, 99, 15);
		lblOpponentHeros.setText("Opponent Heros");
		
		Combo drpOppHero1 = new Combo(grpFlopConfig, SWT.NONE);
		drpOppHero1.setBounds(317, 43, 200, 23);
		drpOppHero1.setText("Zeus");
		
		Combo drpOppHero2 = new Combo(grpFlopConfig, SWT.NONE);
		drpOppHero2.setBounds(523, 43, 200, 23);
		drpOppHero2.setText("Legion Commander");
		
		Combo drpOppHero3 = new Combo(grpFlopConfig, SWT.NONE);
		drpOppHero3.setBounds(729, 43, 200, 23);
		drpOppHero3.setText("Drow");
		
		Label lblRadiantHeros = new Label(grpFlopConfig, SWT.NONE);
		lblRadiantHeros.setText("Your Heros");
		lblRadiantHeros.setBounds(317, 72, 99, 15);
		
		Combo drpOurHero1 = new Combo(grpFlopConfig, SWT.NONE);
		drpOurHero1.setBounds(317, 93, 200, 23);
		drpOurHero1.setText("Tinker");
		
		Combo drpOurHero2 = new Combo(grpFlopConfig, SWT.NONE);
		drpOurHero2.setBounds(523, 93, 200, 23);
		drpOurHero2.setText("Bounty Hunter");
		
		Combo drpOurHero3 = new Combo(grpFlopConfig, SWT.NONE);
		drpOurHero3.setBounds(729, 93, 200, 23);
		drpOurHero3.setText("Axe");
		
		/*
		 * Adding the drop down menus to an array to make it easier to manipulate them all at once
		 */
		
		heroDropDownWidgets.add(drpOppHero1);
		heroDropDownWidgets.add(drpOppHero2);
		heroDropDownWidgets.add(drpOppHero3);
		
		heroDropDownWidgets.add(drpOurHero1);
		heroDropDownWidgets.add(drpOurHero2);
		heroDropDownWidgets.add(drpOurHero3);
		
		Button btnStartSim = new Button(grpFlopConfig, SWT.NONE);
		
		btnStartSim.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				/*
				 * Grab hero choices from GUI
				 */
				ArrayList<Hero> enemyHeroList = new ArrayList<Hero>();
				ArrayList<Hero> goodHeroList = new ArrayList<Hero>();
				ArrayList<Hero> choosenHeros = getHeros(heroDropDownWidgets);
				
				enemyHeroList.add(choosenHeros.get(0));
				enemyHeroList.add(choosenHeros.get(1));
				enemyHeroList.add(choosenHeros.get(2));
				
				goodHeroList.add(choosenHeros.get(0));
				goodHeroList.add(choosenHeros.get(1));
				goodHeroList.add(choosenHeros.get(2));
				
				/*
				 * Start the simulation
				 */
				
				FlopSimManager fsm = new FlopSimManager();
				fsm.StartFlopSim(simCount, enemyHeroList, goodHeroList);
				
				/*
				 * Update widgets now that we have our data
				 */
				flopUI.updateStatWidgets();
			}
		});
		
		btnStartSim.setBounds(336, 133, 91, 25);
		btnStartSim.setText("Simulate!");
		
		TabItem tbtmResults = new TabItem(tabFolder, SWT.NONE);
		tbtmResults.setText("Results");
		
		Group grpSimResults = new Group(tabFolder, SWT.NONE);
		tbtmResults.setControl(grpSimResults);
		grpSimResults.setText("Sim Results");
		
		table = new Table(grpSimResults, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 54, 468, 159);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnHero = new TableColumn(table, SWT.NONE);
		tblclmnHero.setWidth(100);
		tblclmnHero.setText("Hero");
		
		TableColumn tblclmnDeadOnFlop = new TableColumn(table, SWT.NONE);
		tblclmnDeadOnFlop.setWidth(100);
		tblclmnDeadOnFlop.setText("Dead on flop?");
		
		TableColumn tblclmnDeadRound = new TableColumn(table, SWT.NONE);
		tblclmnDeadRound.setWidth(143);
		tblclmnDeadRound.setText("Dead 1 round after flop?");
		
		Group grpFlopStatistics = new Group(grpSimResults, SWT.NONE);
		grpFlopStatistics.setText("Flop Statistics");
		grpFlopStatistics.setBounds(10, 250, 843, 243);
		
		table_1 = new Table(grpFlopStatistics, SWT.BORDER | SWT.FULL_SELECTION);
		table_1.setBounds(10, 25, 795, 208);
		table_1.setHeaderVisible(true);
		table_1.setLinesVisible(true);
		
		TableColumn tblclmnStat = new TableColumn(table_1, SWT.NONE);
		tblclmnStat.setWidth(221);
		tblclmnStat.setText("Stat");
		
		TableColumn tblclmnOnFlop = new TableColumn(table_1, SWT.NONE);
		tblclmnOnFlop.setWidth(100);
		tblclmnOnFlop.setText("On Flop");
		
		TableColumn tblclmnndTurn = new TableColumn(table_1, SWT.NONE);
		tblclmnndTurn.setWidth(100);
		tblclmnndTurn.setText("2nd Turn");
		
		TableItem tiHerosBlocked = new TableItem(table_1, SWT.NONE);
		tiHerosBlocked.setText("All 3 Heros blocked");
		tiHerosBlocked.setText(1, "NA");
		tiHerosBlocked.setText(2, "NA");
		flopUI.registerTabElement(StatElement.HEROS_BLOCKED, tiHerosBlocked); //Register our widget
		
		Label lblNumberOfSamples = new Label(grpSimResults, SWT.NONE);
		lblNumberOfSamples.setBounds(10, 21, 117, 15);
		lblNumberOfSamples.setText("Number of samples:");
		
		/*
		 * Initialize our widgets with default values and settings
		 */
		flopUI.initWidgets(heroDropDownWidgets);
	}
	
	/*
	 * Dumbo way to do this I'll change it later
	 */
	private ArrayList<Hero> getHeros(ArrayList<Combo> dropDownWidgets)
	{
		ArrayList<Hero> dropDownChoices = new ArrayList<Hero>();
		for(Combo c : dropDownWidgets)
		{
			Hero h = Hero.values()[c.getSelectionIndex()];
			dropDownChoices.add(h);
		} 
		
		return dropDownChoices;
	}
}
