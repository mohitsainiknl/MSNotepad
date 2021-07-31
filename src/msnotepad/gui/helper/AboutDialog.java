/*
 * Copyright (c) 2021 Mohit Saini, Under MIT License. Use is subject to license terms.
 * 
 */

package msnotepad.gui.helper;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

/**
 * AboutDialog class help to load the about JDialog from the HTML file.
 * Basically, its the combination of JLabel and the HTML file in the form
 * of string in the label.
 */
public class AboutDialog extends JDialog{
    private JLabel textLabel;

    /**
     * AboutDialog constructor help to get the label which will represents
     * the about dialog and their parent component.
     * @param frame parent component.
     * @param title the title of the dialog.
     * @param mobality dialog is movable if this is TRUE.
     */
    public AboutDialog(JFrame frame, String title, boolean mobality) {
        super(frame, title , mobality);
        setSize(460, 425);

        initialiseDialog();
    }

    /**
     * initialiseDialog method do the initial work of this dialog.
     */
    private void initialiseDialog() {
        textLabel = new JLabel();
        textLabel.setVerticalAlignment(JLabel.NORTH);
        JButton okButton = new JButton("OK");
        okButton.setBackground(Color.BLUE.brighter());

        JPanel labelHolder = new JPanel(new BorderLayout());
        JPanel buttonHolder = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 12));
        
        try {
            loadHTMLFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        okButton.addActionListener(ee -> dispose());
        
        labelHolder.add(textLabel, BorderLayout.CENTER);
        buttonHolder.add(okButton);

        setLayout(new BorderLayout());
        add(labelHolder, BorderLayout.CENTER);
        add(buttonHolder, BorderLayout.SOUTH);

        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * loadHTMLFile method help to load the text, from the HTML file, in the
     * form of String.
     * @throws FileNotFoundException throw the Exception.
     */
    private void loadHTMLFile() throws FileNotFoundException {
        StringBuilder text = new StringBuilder();
        {
            File htmlFile = new File("AboutDialog.html");
            BufferedReader reader = new BufferedReader(new FileReader(htmlFile));
            
            String holdText;
            try {
                while ((holdText = reader.readLine()) != null) {
                    text.append(holdText);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        textLabel.setText(text.toString());
    }
}
