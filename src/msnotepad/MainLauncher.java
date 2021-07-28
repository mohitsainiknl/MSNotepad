/*
 * Copyright (c) 2021 Mohit Saini, Under MIT License. Use is subject to license terms.
 * 
 */

package msnotepad;

import javax.swing.UIManager;
import msnotepad.gui.GUIHandler;

/** 
 * MainLauncher is the starting point of this Notepad, Moreover,
 * it also set it's look and launch the GUIHandler class,
 * Which handle the further execution.
 */
public class MainLauncher {
    GUIHandler gui;

    private MainLauncher() {
        gui = new GUIHandler();
        gui.handle();
    }
     
	public static void main(String[] args) {
        loadLookAndFeel();
        new MainLauncher();     //<--- Internal Constructor
	}
    
    private static void loadLookAndFeel() {
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }
}
