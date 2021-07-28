/*
 * Copyright (c) 2021 Mohit Saini, Under MIT License. Use is subject to license terms.
 * 
 */

package msnotepad.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.JPanel;

import msnotepad.gui.GUIHandler;
import msnotepad.gui.helper.StatusBar;
import msnotepad.init.InitialValues;

public class ViewMenuActions {
    
    public static class StatusBarViewAction extends AbstractAction {
        public StatusBarViewAction() {
            super();
            putValue(AbstractAction.NAME, "Status Bar");
            putValue(MNEMONIC_KEY, KeyEvent.VK_S);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            JPanel statusBar = GUIHandler.getStatusBar();
          
            if (InitialValues.getShowStatusBar() == true) {
                statusBar.setVisible(false);
                InitialValues.setShowStatusBar(false);
            } else {
                statusBar.setVisible(true);
                InitialValues.setShowStatusBar(true);
            }
        }
    }

    public static class ZoomInAction extends AbstractAction {
        public ZoomInAction() {
            super();
            putValue(AbstractAction.NAME, "Zoom In");
            putValue(MNEMONIC_KEY, KeyEvent.VK_I);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, ActionEvent.CTRL_MASK));
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            int zoomLevel = GUIHandler.getZoomValue();
            zoomLevel = zoomLevel + 10;
            GUIHandler.setZoomValue(zoomLevel);
            StatusBar.setZoomLevel(zoomLevel);
        }
    }

    public static class ZoomOutAction extends AbstractAction {
        public ZoomOutAction() {
            super();
            putValue(AbstractAction.NAME, "Zoom Out");
            putValue(MNEMONIC_KEY, KeyEvent.VK_O);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ActionEvent.CTRL_MASK));
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            int zoomLevel = GUIHandler.getZoomValue();
            zoomLevel = zoomLevel - 10;
            GUIHandler.setZoomValue(zoomLevel);
            StatusBar.setZoomLevel(zoomLevel);
        }
    }

    public static class DefaultZoomAction extends AbstractAction {
        public DefaultZoomAction() {
            super();
            putValue(AbstractAction.NAME, "Default Zoom");
            putValue(MNEMONIC_KEY, KeyEvent.VK_R);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_0, ActionEvent.CTRL_MASK));
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            GUIHandler.setZoomValue(100);
            StatusBar.setZoomLevel(100);
        }
    }
}
