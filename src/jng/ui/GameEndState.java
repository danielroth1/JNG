package jng.ui;

import java.awt.Point;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.action.basicactions.ChangeStateInitAction;
import eea.engine.entity.Entity;
import eea.engine.event.ANDEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;
import jng.actions.sound.SoundManager;
import jng.statistics.GameStatistics;
import jng.ui.buttonGameState.BasicButtonGameState;



public class GameEndState extends BasicButtonGameState {

	private boolean won;
	private GameStatistics statistics;

	public GameEndState(int sid, boolean won) {
		super(sid);
		this.won = won;
		this.statistics = GameStatistics.getInstance();
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		Point res = Controls.displayResolution;

		this.statistics = GameStatistics.getInstance();

		// Hintergrund laden
    	Entity background = addBackground("/assets/backgroundMenu.png");
    	
    	// Fuege die Entity zum StateBasedEntityManager hinzu
    	entityManager.addEntity(stateID, background);
    	
    	// Sound symbol
    	SoundManager.getInstance().setVolumeControl(stateID);
    	
    	// Label result
    	labels.add(new Label(won ? "You have won!" : "You have lost." ,
    	        new Vector2f(res.x/2, res.y/2-BasicButtonGameState.buttonSize.y*3)));

    	addStatisticsLabels(res);

    	// initiate buttons
    	Entity back_to_menu_Entity = addButton((int)(res.x/2+BasicButtonGameState.buttonSize.x/2+25), 
				res.y/2 + 150,
				BasicButtonGameState.buttonSize,
				"Back to main menu",
				"/assets/button.png");
    	
		// Erstelle das Ausloese-Event und die zugehoerige Action
    	ANDEvent mainEvents = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	Action back_to_menu_Action = new ChangeStateInitAction(Jng.MAINMENU_STATE);
    	mainEvents.addAction(back_to_menu_Action);
    	back_to_menu_Entity.addComponent(mainEvents);
    	entityManager.addEntity(stateID, back_to_menu_Entity);
    	
    	
    	Entity restart_Entity = addButton((int)(res.x/2-BasicButtonGameState.buttonSize.x/2-25), 
				res.y/2 + 150,
				BasicButtonGameState.buttonSize,
				"Restart",
				"/assets/button.png");
    	mainEvents = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
    	Action restart_Action = new ChangeStateInitAction(Jng.GAMEPLAY_STATE);
    	mainEvents.addAction(restart_Action);
    	restart_Entity.addComponent(mainEvents);
    	entityManager.addEntity(this.stateID, restart_Entity);
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
	    super.enter(container, game);

	    // Aktualisieren der Statistik bei jedem Betreten des Zustands
	    this.statistics = GameStatistics.getInstance();

	    System.out.println("GameEndState ENTER - Aktualisierte Statistiken:");
	    System.out.println("Enemies Destroyed: " + statistics.getEnemiesDestroyed());
	    System.out.println("Structures Destroyed: " + statistics.getStructuresDestroyed());
	    System.out.println("Shots Fired: " + statistics.getShotsFired());

	    // Labels aktualisieren
	    Point res = Controls.displayResolution;
	    addStatisticsLabels(res);

		// Clear up for next game
		GameStatistics.getInstance().resetStatistics();
	}

	/**
	 * Adds the statistics labels to the GameEndState
	 * @param res Bildschirmaufloesung
	 */
	private void addStatisticsLabels(Point res) {
	    // Clear existing labels before adding new ones to prevent duplicates
	    labels.clear();

	    // Re-add the result label
	    labels.add(new Label(won ? "You have won!" : "You have lost." ,
    	        new Vector2f(res.x/2, res.y/2-BasicButtonGameState.buttonSize.y*3)));

	    // Add statistics header
	    labels.add(new Label("Game Statistics",
	            new Vector2f(res.x/2, res.y/2-BasicButtonGameState.buttonSize.y*1.5f)));

	    int column1X = res.x/2 - 200;
	    int startY = res.y/2 - 50;
	    int yOffset = 30;

	    // Column 1: General statistics
	    labels.add(new Label("Play Time: " + statistics.getFormattedPlayTime(),
	            new Vector2f(column1X, startY)));
	    labels.add(new Label("Enemies Destroyed: " + statistics.getEnemiesDestroyed(),
	            new Vector2f(column1X, startY + yOffset)));
	    labels.add(new Label("Structures Destroyed: " + statistics.getStructuresDestroyed(),
	            new Vector2f(column1X, startY + yOffset*2)));
	    labels.add(new Label("Items Collected: " + statistics.getItemsCollected(),
	            new Vector2f(column1X, startY + yOffset*3)));

	    // Column 2: Combat statistics
	    int column2X = res.x/2 + 200;

	    labels.add(new Label("Shots Fired: " + statistics.getShotsFired(),
	            new Vector2f(column2X, startY)));
	    labels.add(new Label("MG Shots: " + statistics.getMgShotsFired(),
	            new Vector2f(column2X, startY + yOffset)));
	    labels.add(new Label("Rockets: " + statistics.getRocketsFired(),
	            new Vector2f(column2X, startY + yOffset*2)));
	    labels.add(new Label("Bombs Dropped: " + statistics.getBombsDropped(),
	            new Vector2f(column2X, startY + yOffset*3)));
	}
}
