package jng.ui;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import eea.engine.entity.StateBasedEntityManager;
import jng.actions.sound.SoundManager;


public class Jng extends StateBasedGame {
	
	
	
	// Jeder State wird durch einen Integer-Wert gekennzeichnet
    public static final int MAINMENU_STATE = 0;
    public static final int GAMEPLAY_STATE = 1;
    public static final int NEWGAME_STATE = 2;
    public static final int GAMEWON_STATE = 3;
    public static final int GAMELOST_STATE = 4;
    public static final int CONTROLS_STATE = 5;
    
    private static AppGameContainer app;
    
    public Jng()
    {
        super("Jets'n'Guns");
    }
 
    public static void main(String[] args) throws SlickException
    {
        // Set the library path depending on the OS. Prefer explicit folders (windows, macosx)
        // but verify the directory exists. If not found, print helpful diagnostics.
        String userDir = System.getProperty("user.dir");
        String osName = System.getProperty("os.name").toLowerCase();
        String[] candidates;
        if (osName.contains("windows")) {
            candidates = new String[] { "native/windows", "native/" + osName };
        } else if (osName.contains("mac")) {
            // historically this project used "macosx" as the folder name
            candidates = new String[] { "native/macosx", "native/mac", "native/" + osName };
        } else {
            candidates = new String[] { "native/" + osName, "native/linux" };
        }

        String chosen = null;
        for (String rel : candidates) {
            java.io.File f = new java.io.File(userDir, rel);
            if (f.exists() && f.isDirectory()) {
                chosen = f.getAbsolutePath();
                break;
            }
        }

        if (chosen != null) {
            System.setProperty("org.lwjgl.librarypath", chosen);
            System.out.println("Using LWJGL natives from: " + chosen);
        } else {
            // No suitable native folder found — fall back to default and print instructions
            String attempted = String.join(", ", candidates);
            System.err.println("WARNING: No LWJGL native folder found for this OS. Tried: " + attempted);
            System.err.println("The game will attempt to load native libraries from the default path, which may fail.");
            System.err.println("Please place the LWJGL native libraries for your platform into one of the project's 'native' folders (e.g. native/macosx) or start Java with -Djava.library.path=/path/to/natives");
            System.err.println("Existing native folders: " + java.util.Arrays.toString(new java.io.File(userDir, "native").list()));
        }

    	// Setze dieses StateBasedGame in einen App Container (oder Fenster)
        app = new AppGameContainer(new Jng());
        app.setVSync(true);
        if (!Controls.debug)
        	app.setShowFPS(false);
 
        // Lege die Einstellungen des Fensters fest und starte das Fenster
        // (nicht aber im Vollbildmodus)
        app.setDisplayMode(Controls.displayResolution.x, Controls.displayResolution.y, false);

        // List of available Display-Modi (optional)
        try {
        	for (DisplayMode dm : Display.getAvailableDisplayModes())
        		System.out.println(dm.toString());
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

        SoundManager.getInstance().setSoundOn(Controls.soundOn);
        app.start();
        
    }

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		if (key == Input.KEY_F1) {
	         if (app != null) {
	            try {
	            	app.setFullscreen(!app.isFullscreen());
	            } catch (SlickException e) {
                    throw new RuntimeException("Error toggling fullscreen mode", e);
	            }
	         }
	      }
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
		
		// Fuege dem StateBasedGame die States hinzu 
		// (der zuerst hinzugefuegte State wird als erster State gestartet)
		addState(new MainMenuState(MAINMENU_STATE));
        addState(new GameplayState(GAMEPLAY_STATE));
        addState(new NewGameState(NEWGAME_STATE));
        addState(new GameEndState(GAMEWON_STATE, true));
        addState(new GameEndState(GAMELOST_STATE, false));
        addState(new ControlsState(CONTROLS_STATE));
        
        // Fuege dem StateBasedEntityManager die States hinzu
        StateBasedEntityManager.getInstance().addState(MAINMENU_STATE);
        StateBasedEntityManager.getInstance().addState(GAMEPLAY_STATE);
        StateBasedEntityManager.getInstance().addState(NEWGAME_STATE);
        StateBasedEntityManager.getInstance().addState(GAMEWON_STATE);
        StateBasedEntityManager.getInstance().addState(GAMELOST_STATE);
        StateBasedEntityManager.getInstance().addState(CONTROLS_STATE);
		
	}
}