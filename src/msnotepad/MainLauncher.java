package msnotepad;

import javax.swing.UIManager;

import msnotepad.gui.GUIHandler;

public class MainLauncher {

    private MainLauncher() {

        new GUIHandler();
    }
     
	public static void main(String[] args) {	//<-------- Main Mathod
        loadLookAndFeel();
        new MainLauncher();
	}
    
    private static void loadLookAndFeel() {
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }
}
