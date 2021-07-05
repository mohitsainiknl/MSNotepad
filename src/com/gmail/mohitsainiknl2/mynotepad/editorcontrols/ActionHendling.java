package com.gmail.mohitsainiknl2.mynotepad.editorcontrols;

import com.gmail.mohitsainiknl2.mynotepad.MainLauncher;
import com.gmail.mohitsainiknl2.mynotepad.guihelper.AboutDialog;
import com.gmail.mohitsainiknl2.mynotepad.guihelper.FindAndReplaceDialog;
import com.gmail.mohitsainiknl2.mynotepad.guihelper.DialogType;
import com.gmail.mohitsainiknl2.mynotepad.guihelper.FontDialog;
import com.gmail.mohitsainiknl2.mynotepad.guihelper.NotepadStatusBar;
import com.gmail.mohitsainiknl2.mynotepad.guihelper.OptionPane;
import com.gmail.mohitsainiknl2.mynotepad.initialvalues.InitialParameters;

import java.net.*;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
//import javax.swing.filechooser.FileSystemView;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;

import javax.swing.AbstractAction;
import javax.swing.text.DefaultEditorKit;
import javax.swing.Action;
import javax.swing.KeyStroke;


public class ActionHendling {
    private static Action cut = new DefaultEditorKit.CutAction();
    private static Action copy = new DefaultEditorKit.CopyAction();
    private static Action paste = new DefaultEditorKit.PasteAction();

    public static Action getCutAction() {
        cut.putValue(Action.NAME, "Cut");
        return cut;
    }
    public static Action getCopyAction() {
        copy.putValue(Action.NAME, "Copy");
        return copy;
    }
    public static Action getPasteAction() {
        paste.putValue(Action.NAME, "Paste");
        return paste;
    }
    
    
    //****************************  File Menu Actions  ****************************************

    public static class NewFileAction extends AbstractAction {
        public NewFileAction() {
            super();
            putValue(AbstractAction.NAME, "New File");
            putValue(MNEMONIC_KEY, KeyEvent.VK_N);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if(MainLauncher.getIsSaved() == false) {
                int value = OptionPane.showOptionPane();

                if(value == 1) {
                    MainLauncher.getSaveAsMenuItem().doClick();
                } 
                else if(value == 2) {
                    return;
                }
                else {
                    // This is don't save option block
                }
            }
            MainLauncher.setIsLoadingFile(true);

            InitialParameters.setFileName(InitialParameters.NEW_FILE);
            InitialParameters.setFilePath(null);
            MainLauncher.setIsSaved(true);
            MainLauncher.getEditorTextArea().setText("");
            MainLauncher.setIsLoadingFile(false);
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
            String fileName = InitialParameters.getFileName();
            String filePath = InitialParameters.getFilePath();
            InitialParameters.setFileName(InitialParameters.NEW_FILE);
            InitialParameters.setFilePath(null);
            InitialParameters.writeToFile(); // <---- this is because new window should be empty
            try {
                Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"cd bin && java com.gmail.mohitsainiknl2.mynotepad.MainLauncher && exit\"");
            } catch (Exception ex) {
                System.out.println("Your are doing something worng...");
            }
            InitialParameters.setFileName(fileName);
            InitialParameters.setFilePath(filePath);
            InitialParameters.writeToFile();
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
            int value = fileChooser.showOpenDialog(MainLauncher.getFrame());
            if(value == JFileChooser.APPROVE_OPTION) {
                MainLauncher.setIsLoadingFile(true);
                File file = fileChooser.getSelectedFile();
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                String fileName = fileChooser.getSelectedFile().getName();
                InitialParameters.setFileName(fileName);
                InitialParameters.setFilePath(filePath);

                String fileText = "";
                try {
                    Scanner read = new Scanner(file);
                    while(read.hasNextLine()) {
                        fileText = fileText + read.nextLine() + "\n";
                    }
                    read.close();
                    MainLauncher.setIsSaved(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                MainLauncher.getEditorTextArea().setText(fileText.substring(0, fileText.length() - 1));
                MainLauncher.getEditorTextArea().setCaretPosition(0);
                MainLauncher.setIsLoadingFile(false);
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
            if (InitialParameters.getFilePath() == null) {
                JMenuItem saveAs = MainLauncher.getSaveAsMenuItem();
                saveAs.doClick();
            } else {
                File file = new File(InitialParameters.getFilePath());

                try {
                    FileWriter writer = new FileWriter(file);
                    writer.write(MainLauncher.getEditorTextArea().getText());
                    writer.close();
                    MainLauncher.setIsSaved(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                MainLauncher.updateFrameTitle();
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
            int value = fileChooser.showSaveDialog(MainLauncher.getFrame());
            if(value == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                String fileName = fileChooser.getSelectedFile().getName();
                InitialParameters.setFileName(fileName);
                InitialParameters.setFilePath(filePath);
                
                File file = new File(filePath);

                try {
                    FileWriter writer = new FileWriter(file);
                    writer.write(MainLauncher.getEditorTextArea().getText());
                    writer.close();
                    MainLauncher.setIsSaved(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                MainLauncher.updateFrameTitle();
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
    
    //****************************  Edit Menu Actions  ****************************************

    public static class UndoEditAction extends AbstractAction {
        public UndoEditAction() {
            super();
            putValue(AbstractAction.NAME, "Undo");
            putValue(MNEMONIC_KEY, KeyEvent.VK_U);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            
        }
    }

    public static class DeleteEditAction extends AbstractAction {
        public DeleteEditAction() {
            super();
            putValue(AbstractAction.NAME, "Delete");
            putValue(MNEMONIC_KEY, KeyEvent.VK_L);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, ActionEvent.CTRL_MASK));
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            MainLauncher.getEditorTextArea().replaceSelection("");
        }
    }

    public static class FindEditAction extends AbstractAction {
        public FindEditAction() {
            super();
            putValue(AbstractAction.NAME, "Find");
            putValue(MNEMONIC_KEY, KeyEvent.VK_F);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            new FindAndReplaceDialog(MainLauncher.getFrame(), DialogType.FIND_ONLY, true);
        }
    }

    public static class ReplaceEditAction extends AbstractAction {
        public ReplaceEditAction() {
            super();
            putValue(AbstractAction.NAME, "Replace");
            putValue(MNEMONIC_KEY, KeyEvent.VK_R);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            new FindAndReplaceDialog(MainLauncher.getFrame(), DialogType.FIND_AND_REPLACE, true);
        }
    }

    public static class SelectAllEditAction extends AbstractAction {
        public SelectAllEditAction() {
            super();
            putValue(AbstractAction.NAME, "Select All");
            putValue(MNEMONIC_KEY, KeyEvent.VK_A);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextArea area = MainLauncher.getEditorTextArea();
            area.selectAll();
        }
    }

    //****************************  Format Menu Actions  ****************************************

    public static class WordWrapFormatAction extends AbstractAction {
        public WordWrapFormatAction() {
            super();
            putValue(AbstractAction.NAME, "Word Wrap");
            putValue(MNEMONIC_KEY, KeyEvent.VK_W);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextArea area = MainLauncher.getEditorTextArea();

            if (InitialParameters.getWrapTheLine() == true) {
                area.setLineWrap(false);
                InitialParameters.setWrapTheLine(false);
            } else {
                area.setLineWrap(true);
                InitialParameters.setWrapTheLine(true);
            }
        }
    }

    public static class FontChangeFormatAction extends AbstractAction {
        public FontChangeFormatAction() {
            super();
            putValue(AbstractAction.NAME, "Font...");
            putValue(MNEMONIC_KEY, KeyEvent.VK_F);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            FontDialog font = new FontDialog(MainLauncher.getFrame(), "Font", true);
            font.setVisible(true);
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
            int zoomLevel = MainLauncher.getZoomValue();
            zoomLevel = zoomLevel + 10;
            MainLauncher.setZoomValue(zoomLevel);
            NotepadStatusBar.setZoomLevel(zoomLevel);
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
            int zoomLevel = MainLauncher.getZoomValue();
            zoomLevel = zoomLevel - 10;
            MainLauncher.setZoomValue(zoomLevel);
            NotepadStatusBar.setZoomLevel(zoomLevel);
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
            MainLauncher.setZoomValue(100);
            NotepadStatusBar.setZoomLevel(100);
        }
    }

    //****************************  View Menu Actions  ****************************************

    public static class StatusBarViewAction extends AbstractAction {
        public StatusBarViewAction() {
            super();
            putValue(AbstractAction.NAME, "Status Bar");
            putValue(MNEMONIC_KEY, KeyEvent.VK_S);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            JPanel statusBar = MainLauncher.getStatusBar();
          
            if (InitialParameters.getShowStatusBar() == true) {
                statusBar.setVisible(false);
                InitialParameters.setShowStatusBar(false);
            } else {
                statusBar.setVisible(true);
                InitialParameters.setShowStatusBar(true);
            }
        }
    }

    //****************************  Help Menu Actions  ****************************************

    public static class ViewHelpAction extends AbstractAction {
        public ViewHelpAction() {
            super();
            putValue(AbstractAction.NAME, "View Help");
            putValue(MNEMONIC_KEY, KeyEvent.VK_H);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                java.awt.Desktop.getDesktop().browse(new URL("https://en.wikipedia.org/wiki/Microsoft_Notepad").toURI());
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }

    public static class SendFeedbackAction extends AbstractAction {
        public SendFeedbackAction() {
            super();
            putValue(AbstractAction.NAME, "Send Feedback");
            putValue(MNEMONIC_KEY, KeyEvent.VK_F);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                java.awt.Desktop.getDesktop().mail(new URL("https://mohitsainiknl2@gmail.com").toURI());
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }

    public static class AboutNotepadAction extends AbstractAction {
        public AboutNotepadAction() {
            super();
            putValue(AbstractAction.NAME, "About MyNotepad");
            putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            AboutDialog dialog = new AboutDialog(MainLauncher.getFrame(), "About MyNotepad", true);
            dialog.setVisible(true);
        }
    }
}
