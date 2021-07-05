package com.gmail.mohitsainiknl2.mynotepad.guihelper;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class AboutDialog extends JDialog{
    private JButton okButton;
    private JLabel textLabel;
    private JPanel labelHolder, buttonHolder;
    
    public AboutDialog(JFrame frame, String title, boolean mobality) {
        super(frame, title , mobality);
        setSize(460, 425);

        initialiseDialog();
    }
    
    private void initialiseDialog() {
        textLabel = new JLabel();
        textLabel.setVerticalAlignment(JLabel.NORTH);
        okButton = new JButton("OK");
        okButton.setBackground(Color.BLUE.brighter());

        labelHolder = new JPanel(new BorderLayout());
        buttonHolder = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 12));
        
        try {
            loadHTMLFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ee) {
                dispose();
            }
        });
        
        labelHolder.add(textLabel, BorderLayout.CENTER);
        buttonHolder.add(okButton);

        setLayout(new BorderLayout());
        add(labelHolder, BorderLayout.CENTER);
        add(buttonHolder, BorderLayout.SOUTH);

        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    private void loadHTMLFile() throws FileNotFoundException {
        StringBuffer text = new StringBuffer("");
        {
            File htmlFile = new File("AboutDailog.html");
            BufferedReader reader = new BufferedReader(new FileReader(htmlFile));
            
            String holdText = "";
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
