package msnotepad.guihelper;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.Color;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import msnotepad.gui.GUIHandler;

public class FindAndReplaceDialog {
    public FindAndReplaceDialog(JFrame frame, DialogType type, boolean mobality) {
        if(type == DialogType.FIND_ONLY) {
            new FindDialog(frame, mobality);
        }
        else {
            new ReplaceDialog(frame, mobality);
        }

    }

    //////////////////////////////////////////////////////////////////////

    private static class FindDialog extends JDialog implements ActionListener {
        private JLabel findLabel;
        private JTextField findField;
        private JButton findButton, cancelButton;
        private ButtonGroup directionGroup;
        private JRadioButton upRadioButton, downRadioButton;
        private JCheckBox caseCheckBox, wrapCheckBox;
        private int lastLoc = GUIHandler.getEditorTextArea().getCaretPosition();
    
        public FindDialog(JFrame frame, boolean mobality) {
            super(frame,"Find", mobality);
            initializeFindDialog();
            addListeners();
    
            pack();
            setLocationRelativeTo(null);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setResizable(false);
            setVisible(true);
        }
        
        private void initializeFindDialog() {
            findLabel = new JLabel("Find What :");
            findField = new JTextField(19);
            findLabel.setLabelFor(findField);
            {
                JTextArea textArea = GUIHandler.getEditorTextArea();
                if(textArea.getSelectedText() != null) {
                    findField.setText(textArea.getSelectedText());
                    findField.requestFocus();
                    findField.selectAll();
                    lastLoc = textArea.getSelectionStart();
                }
            }
    
            findButton = new JButton("Find Next");
            cancelButton = new JButton("Cancel");
    
            directionGroup = new ButtonGroup();
            upRadioButton = new JRadioButton("Up");
            downRadioButton = new JRadioButton("Down");
            directionGroup.add(upRadioButton);
            directionGroup.add(downRadioButton);
            downRadioButton.setSelected(true);

            caseCheckBox = new JCheckBox("Match Case");
            wrapCheckBox = new JCheckBox("Wrap Around");
    
            //----------- Setting Up Layout ----------------
    
            GridBagLayout gbl = new GridBagLayout();
            GridBagConstraints gbc = new GridBagConstraints();
            getContentPane().setLayout(gbl);
    
            gbc.insets = new Insets(11, 8, 5, 5);
            gbc.fill = GridBagConstraints.BOTH;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.weightx = 0;
            gbc.weighty = 0;
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gbl.setConstraints(findLabel, gbc);
            add(findLabel);
    
            gbc.insets = new Insets(11, 5, 5, 6);
            gbc.fill = GridBagConstraints.BOTH;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.weightx = 1.0;
            gbc.weighty = 0;
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.gridheight = 1;
            gbl.setConstraints(findField, gbc);
            add(findField);
    
            gbc.insets = new Insets(10, 5, 35, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.NORTH;
            gbc.weightx = 0;
            gbc.weighty = 1.0;
            gbc.gridx = 3;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.gridheight = 3;
            {
                JPanel buttonBag = new JPanel(new GridLayout(3, 1, 5, 5));
                buttonBag.add(findButton);
                buttonBag.add(cancelButton);
                gbl.setConstraints(buttonBag, gbc);
                add(buttonBag);
            }
            
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.SOUTHWEST;
            gbc.weightx = 0;
            gbc.weighty = 1.0;
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.gridheight = 1;
            {
                JPanel buttonBag = new JPanel(new GridLayout(2, 1, 5, 5));
                buttonBag.add(caseCheckBox);
                buttonBag.add(wrapCheckBox);
                gbl.setConstraints(buttonBag, gbc);
                add(buttonBag);
            }
    
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.NORTHEAST;
            gbc.weightx = 0;
            gbc.weighty = 1.0;
            gbc.gridx = 2;
            gbc.gridy = 2;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            {
                JPanel buttonBag = new JPanel();
                buttonBag.setBorder(new TitledBorder(new LineBorder(new Color(220, 220, 220), 1), "Direction"));
                buttonBag.add(upRadioButton);
                buttonBag.add(downRadioButton);
                gbl.setConstraints(buttonBag, gbc);
                add(buttonBag);
            }
        }
    
        private void addListeners() {
            findField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    enableButtons();
                }
            });
            findField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    enableButtons();
                }
            });
            findButton.addActionListener(this);
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
        }

    
        private void enableButtons() {  //<-------------  Enable and Disable Required Buttons
            if(findField.getText().equals("")) {
                findButton.setEnabled(false);
            }
            else {
                findButton.setEnabled(true);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int value = findNext(this,lastLoc, findField.getText(), caseCheckBox.isSelected(), downRadioButton.isSelected(), true);
            if(value != -1) {
                lastLoc = value;
            }
        }

        public static int findNext(JDialog parent, int lastLoc, String text, boolean matchCase, boolean isDownDirection, boolean showOptPane) {
            JTextArea textArea = GUIHandler.getEditorTextArea();
            String file = textArea.getText();
            int fileLength = file.length();
            boolean isFind = false;
            
            if(isDownDirection == true)
            {
                if(textArea.getSelectedText() != null) {
                    lastLoc = textArea.getSelectionEnd();
                }
                for(int i = lastLoc; i <= fileLength; ++i) {
                    if((i + text.length()) <= fileLength) {
                        if(matchCase == true) {
                            if(file.substring(i, i + text.length()) .equals(text)) {
                                isFind = true;
                                lastLoc = i;
                                textArea.select(i, i + text.length());
                                break;
                            }
                        }
                        else {
                            if(file.substring(i, i + text.length()) .equalsIgnoreCase(text)) {
                                isFind = true;
                                lastLoc = i;
                                textArea.select(i, i + text.length());
                                break;
                            }
                        }
                    }
                    //i = file.lastIndexOf(text, lastLoc);  //<---- WE CAN ALSO USE THIS
                    else {
                        isFind = false;
                        break;
                    }
                }
            }
            else {
                for(int i = lastLoc; i >= 0; --i) {
                    if((i - text.length()) >= 0) {
                        if(matchCase == true) {
                            if(file.substring(i - text.length(), i) .equals(text)) {
                                isFind = true;
                                lastLoc = i - text.length();
                                textArea.select(i - text.length(), i);
                                break;
                            }
                        }
                        else {
                            if(file.substring(i - text.length(), i) .equalsIgnoreCase(text)) {
                                isFind = true;
                                lastLoc = i - text.length();
                                textArea.select(i - text.length(), i);
                                break;
                            }
                        }
                    }
                    //i = file.lastIndexOf(text, lastLoc);  //<---- WE CAN ALSO USE THIS
                    else {
                        isFind = false;
                        break;
                    }
                }
            }
            if(isFind == true) {
                return lastLoc;
            }
            else {
                if(showOptPane == true) {
                    String message = "<HTML>Can not find <b>\"" + text + "\"</b> in the file.";
                    JOptionPane.showMessageDialog(parent, message, "MyNotepad", JOptionPane.INFORMATION_MESSAGE);
                }
                return -1;
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////

    private class ReplaceDialog extends JDialog implements ActionListener {
        private JLabel findLabel, replaceLabel;
        private JTextField findField, replaceField;
        private JButton findButton,replaceButton, replaceAllButton, cancelButton;
        private JCheckBox caseCheckBox, wrapCheckBox;
        private int lastLoc = GUIHandler.getEditorTextArea().getCaretPosition();
    
    
        public ReplaceDialog(JFrame frame, boolean mobality) {
            super(frame, "Replace", mobality);

            initializeFindDialog();
            addListeners();
    
            pack();
            setLocationRelativeTo(null);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setResizable(false);
            setVisible(true);
        }
    
        private void initializeFindDialog() {
            findLabel = new JLabel("Find What :");
            findField = new JTextField(19);
            findLabel.setLabelFor(findField);
            {
                JTextArea textArea = GUIHandler.getEditorTextArea();
                if(textArea.getSelectedText() != null) {
                    findField.setText(textArea.getSelectedText());
                    findField.requestFocus();
                    findField.selectAll();
                    lastLoc = textArea.getSelectionStart();
                }
            }

            replaceLabel = new JLabel("Replace With :");
            replaceField = new JTextField(19);
            replaceLabel.setLabelFor(replaceField);

            findButton = new JButton("Find Next");
            cancelButton = new JButton("Cancel");
            replaceButton = new JButton("Replace");
            replaceAllButton = new JButton("Replace All");
    
            caseCheckBox = new JCheckBox("Match Case");
            wrapCheckBox = new JCheckBox("Wrap Around");
    
            //----------- Setting Up Layout ----------------
    
            GridBagLayout gbl = new GridBagLayout();
            GridBagConstraints gbc = new GridBagConstraints();
            getContentPane().setLayout(gbl);
    
            gbc.insets = new Insets(11, 8, 5, 5);
            gbc.fill = GridBagConstraints.BOTH;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.weightx = 0;
            gbc.weighty = 0;
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gbl.setConstraints(findLabel, gbc);
            add(findLabel);
    
            gbc.insets = new Insets(11, 5, 5, 6);
            gbc.fill = GridBagConstraints.BOTH;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.weightx = 1.0;
            gbc.weighty = 0;
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.gridheight = 1;
            gbl.setConstraints(findField, gbc);
            add(findField);
    
            gbc.insets = new Insets(10, 5, 35, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.NORTH;
            gbc.weightx = 0;
            gbc.weighty = 1.0;
            gbc.gridx = 3;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.gridheight = 3;
            {
                JPanel buttonBag = new JPanel(new GridLayout(4, 1, 5, 5));
                buttonBag.add(findButton);
                buttonBag.add(replaceButton);
                buttonBag.add(replaceAllButton);
                buttonBag.add(cancelButton);
                gbl.setConstraints(buttonBag, gbc);
                add(buttonBag);
            }
            gbc.insets = new Insets(5, 8, 5, 5);
            gbc.fill = GridBagConstraints.BOTH;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.weightx = 0;
            gbc.weighty = 0;
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gbl.setConstraints(replaceLabel, gbc);
            add(replaceLabel);
    
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.BOTH;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.weightx = 1.0;
            gbc.weighty = 0;
            gbc.gridx = 1;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbc.gridheight = 1;
            gbl.setConstraints(replaceField, gbc);
            add(replaceField);
            
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.SOUTHWEST;
            gbc.weightx = 0;
            gbc.weighty = 1.0;
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.gridheight = 1;
            {
                JPanel buttonBag = new JPanel(new GridLayout(2, 1, 5, 5));
                buttonBag.add(caseCheckBox);
                buttonBag.add(wrapCheckBox);
                gbl.setConstraints(buttonBag, gbc);
                add(buttonBag);
            }
        }
    
        private void addListeners() {
            findField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    enableButtons();
                }
            });
            findField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    enableButtons();
                }
            });
            findButton.addActionListener(this);
            replaceButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    replaceAction();
                }
            });
            replaceAllButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    replaceAllAction();
                }
            });
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
        }
        private void enableButtons() {  //<-------------  Enable and Disable Required Buttons
            if(findField.getText().equals("")) {
                findButton.setEnabled(false);
                replaceButton.setEnabled(false);
                replaceAllButton.setEnabled(false);
            }
            else {
                findButton.setEnabled(true);
                replaceButton.setEnabled(true);
                replaceAllButton.setEnabled(true);
            }
        }
        private void replaceAction() {
            JTextArea textArea = GUIHandler.getEditorTextArea();  //TODO
            if(textArea.getSelectedText() != null && textArea.getSelectedText().equalsIgnoreCase(findField.getText())) {
                textArea.replaceSelection(replaceField.getText());
                textArea.select(textArea.getCaretPosition() - replaceField.getText().length(), textArea.getCaretPosition());
            }
            else {
                int value = FindDialog.findNext(this, lastLoc, findField.getText(), caseCheckBox.isSelected(), true, true);
                if(value != -1) {
                    lastLoc = value;
                    textArea.replaceSelection(replaceField.getText());
                    textArea.select(textArea.getCaretPosition() - replaceField.getText().length(), textArea.getCaretPosition());
                }
            }
        }
        private void replaceAllAction() {
            JTextArea textArea = GUIHandler.getEditorTextArea();
            int count = 0;
            int value = 0;

            while(value != -1) {
                textArea.replaceSelection(replaceField.getText());
                ++count;
                value = FindDialog.findNext(this, lastLoc, findField.getText(), caseCheckBox.isSelected(), true, false);
            }
            String message = "<HTML>Number of replacements : <b>" + count + "</b>";
            JOptionPane.showMessageDialog(this, message, "MyNotepad", JOptionPane.INFORMATION_MESSAGE);
        }
    
        @Override
        public void actionPerformed(ActionEvent e) {
            int value = FindDialog.findNext(this, lastLoc, findField.getText(), caseCheckBox.isSelected(), true, true);
            if(value != -1) {
                lastLoc = value;
            }
        }
    }
}