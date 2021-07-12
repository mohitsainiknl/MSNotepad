package msnotepad.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JTextArea;

import msnotepad.gui.GUIHandler;
import msnotepad.guihelper.FontDialog;
import msnotepad.init.InitialValues;

public class FormatMenuActions {
    
    public static class WordWrapFormatAction extends AbstractAction {
        public WordWrapFormatAction() {
            super();
            putValue(AbstractAction.NAME, "Word Wrap");
            putValue(MNEMONIC_KEY, KeyEvent.VK_W);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextArea area = GUIHandler.getEditorTextArea();

            if (InitialValues.getWrapTheLine() == true) {
                area.setLineWrap(false);
                InitialValues.setWrapTheLine(false);
            } else {
                area.setLineWrap(true);
                InitialValues.setWrapTheLine(true);
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
            FontDialog font = new FontDialog(GUIHandler.getFrame(), "Font", true);
            font.setVisible(true);
        }
    }
}
