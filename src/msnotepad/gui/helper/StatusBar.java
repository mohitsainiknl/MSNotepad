/*
 * Copyright (c) 2021 Mohit Saini, Under MIT License. Use is subject to license terms.
 * 
 */

package msnotepad.gui.helper;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import msnotepad.gui.GUIHandler;
import msnotepad.init.InitialValues;

/**
 * StatusBar class make basic statusBar for the MSNotepad, with the
 * help of the JPanel.
 */
public class StatusBar extends JPanel{
	private static JPanel blankPanel, labelHolder;
	private static StatusBarLabel caretPosition;
    private static StatusBarLabel zoomLevel;
    private static StatusBarLabel encoding;

    /**
     * StatusBar constuctor help to do the initial work.
     */
	public StatusBar() {
        super();
        setOpaque(false);
        setPreferredSize(new Dimension(GUIHandler.getFrame().getWidth(), 23));
        initialiseStatusBar();
        setVisible(InitialValues.getShowStatusBar());
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        if(g instanceof Graphics2D) {
            
            Graphics2D g2d = (Graphics2D)g;
            g2d.setPaint(new Color(215, 215, 215));
            g2d.drawLine(0, 0, getWidth(), 0);
            
            int width = blankPanel.getWidth() - 11;
            g2d.drawLine(width, 2, width, getHeight() - 2);
            
            width += caretPosition.getWidth();
            g2d.drawLine(width, 2, width, getHeight() - 2);
            
            width += zoomLevel.getWidth();
            g2d.drawLine(width, 2, width, getHeight() - 2);
        }
    }

    /**
     * setCaretPosition method help to change the value of the
     * caret position label in the statusBar.
     */
    public void setCaretPosition(int row, int column) {
        caretPosition.setText("Ln "+ row + ", Col " + column);
    }

    /**
     * setZoomLevel method help to change the value of the
     * zoom level label in the statusBar.
     */
    public static void setZoomLevel(int zoom) {
        zoomLevel.setText(zoom + "%");
    }

    /**
     * setEncoding method help to change the value of the
     * encoding label in the statusBar.
     */
    public void setEncoding(String encoding) {
        StatusBar.encoding.setText(encoding);
    }

    /**
     * initialiseStatusBar method help to do the initial work and setup
     * components.
     */
    private void initialiseStatusBar() {
        blankPanel = new JPanel();
        blankPanel.setOpaque(false);
        blankPanel.setBackground(new Color(0, 0, 0, 0));
        
        labelHolder = new JPanel(new FlowLayout(1, 0, 0));
        labelHolder.setOpaque(false);
        labelHolder.setBackground(new Color(0, 0, 0, 0));
		
		caretPosition = new StatusBarLabel("Ln 1, Col 1", 140);
		zoomLevel = new StatusBarLabel("100%", 70);
		encoding = new StatusBarLabel("UTF-8", 120);
		
		labelHolder.add(caretPosition);
		labelHolder.add(zoomLevel);
		labelHolder.add(encoding);
        
        setLayout(new BorderLayout());
		add(labelHolder, BorderLayout.EAST);
		add(blankPanel, BorderLayout.CENTER);
	}

    /**
     * StatusBarLabel inner class help to make label ready for to be
     * added on the statusBar panel.
     */
    private class StatusBarLabel extends JLabel {
        Font font = new Font("", Font.PLAIN , 12);
        
        private StatusBarLabel(String name,int width) {
            super(name);
            setFont(font);
            setPreferredSize(new Dimension(width, 23));
            setAlignmentX(LEFT_ALIGNMENT);
        }
    }
}
