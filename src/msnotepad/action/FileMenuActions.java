/*
 * Copyright (c) 2021 Mohit Saini, Under MIT License. Use is subject to license terms.
 * 
 */

package msnotepad.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;

import msnotepad.gui.GUIHandler;
import msnotepad.gui.helper.OptionPane;
import msnotepad.init.InitialValues;

public class FileMenuActions {
    
    public static class NewFileAction extends AbstractAction {
        public NewFileAction() {
            super();
            putValue(AbstractAction.NAME, "New File");
            putValue(MNEMONIC_KEY, KeyEvent.VK_N);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if(!GUIHandler.getIsSaved()) {
                int value = OptionPane.showOptionPane();

                if(value == 1) {
                    GUIHandler.getSaveAsMenuItem().doClick();
                } 
                else if(value == 2) {
                    return;
                }
            }
            GUIHandler.setIsLoadingFile(true);

            InitialValues.setFileName(InitialValues.NEW_FILE);
            InitialValues.setFilePath(null);
            GUIHandler.setIsSaved(true);
            GUIHandler.getEditorTextArea().setText("");
            GUIHandler.setIsLoadingFile(false);
        }
    }

    public static class NewWindowFileAction extends AbstractAction {
        public NewWindowFileAction() {
            super();
            putValue(AbstractAction.NAME, "New Window");
            putValue(MNEMONIC_KEY, KeyEvent.VK_W);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            String fileName = InitialValues.getFileName();
            String filePath = InitialValues.getFilePath();
            InitialValues.setFileName(InitialValues.NEW_FILE);
            InitialValues.setFilePath(null);
            InitialValues.writeToFile(); // <---- this is because new window should be empty
            try {
                Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"cd bin && java com.gmail.mohitsainiknl2.mynotepad.MainLauncher && exit\"");
            } catch (Exception ex) {
                System.out.println("Your are doing something worng...");
            }
            InitialValues.setFileName(fileName);
            InitialValues.setFilePath(filePath);
            InitialValues.writeToFile();
        }
    }

    public static class OpenFileAction extends AbstractAction {
        public OpenFileAction() {
            super();
            putValue(AbstractAction.NAME, "Open");
            putValue(MNEMONIC_KEY, KeyEvent.VK_O);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
//            FileSystemView fsv = fileChooser.getFileSystemView();
            int value = fileChooser.showOpenDialog(GUIHandler.getFrame());
            if(value == JFileChooser.APPROVE_OPTION) {
                GUIHandler.setIsLoadingFile(true);
                File file = fileChooser.getSelectedFile();
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                String fileName = fileChooser.getSelectedFile().getName();
                InitialValues.setFileName(fileName);
                InitialValues.setFilePath(filePath);

                String fileText = "";
                try {
                    Scanner read = new Scanner(file);
                    while(read.hasNextLine()) {
                        fileText = fileText + read.nextLine() + "\n";
                    }
                    read.close();
                    GUIHandler.setIsSaved(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                GUIHandler.getEditorTextArea().setText(fileText.substring(0, fileText.length() - 1));
                GUIHandler.getEditorTextArea().setCaretPosition(0);
                GUIHandler.setIsLoadingFile(false);
            }
        }
    }
    

    public static class SaveFileAction extends AbstractAction {
        public SaveFileAction() {
            super();
            putValue(AbstractAction.NAME, "Save");
            putValue(MNEMONIC_KEY, KeyEvent.VK_S);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (InitialValues.getFilePath() == null) {
                JMenuItem saveAs = GUIHandler.getSaveAsMenuItem();
                saveAs.doClick();
            } else {
                File file = new File(InitialValues.getFilePath());

                try {
                    FileWriter writer = new FileWriter(file);
                    writer.write(GUIHandler.getEditorTextArea().getText());
                    writer.close();
                    GUIHandler.setIsSaved(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                GUIHandler.updateFrameTitle();
            }
        }
    }
    
    
    public static class SaveAsFileAction extends AbstractAction {
        public SaveAsFileAction() {
            super();
            putValue(AbstractAction.NAME, "Save As");
            putValue(MNEMONIC_KEY, KeyEvent.VK_A);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
//            FileSystemView fsv = fileChooser.getFileSystemView();
            int value = fileChooser.showSaveDialog(GUIHandler.getFrame());
            if(value == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                String fileName = fileChooser.getSelectedFile().getName();
                InitialValues.setFileName(fileName);
                InitialValues.setFilePath(filePath);
                
                File file = new File(filePath);

                try {
                    FileWriter writer = new FileWriter(file);
                    writer.write(GUIHandler.getEditorTextArea().getText());
                    writer.close();
                    GUIHandler.setIsSaved(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                GUIHandler.updateFrameTitle();
            }
        }
    }
    

    public static class ExitFileAction extends AbstractAction {
        public ExitFileAction() {
            super();
            putValue(AbstractAction.NAME, "Exit");
            putValue(MNEMONIC_KEY, KeyEvent.VK_X);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
            
        }
    }
}
