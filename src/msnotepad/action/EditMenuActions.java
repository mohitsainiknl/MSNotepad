package msnotepad.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.JTextArea;

import msnotepad.gui.GUIHandler;
import msnotepad.guihelper.FindAndReplaceDialog;
import msnotepad.guihelper.DialogType;

public class EditMenuActions {
    
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
            GUIHandler.getEditorTextArea().replaceSelection("");
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
            new FindAndReplaceDialog(GUIHandler.getFrame(), DialogType.FIND_ONLY, true);
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
            new FindAndReplaceDialog(GUIHandler.getFrame(), DialogType.FIND_AND_REPLACE, true);
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
            JTextArea area = GUIHandler.getEditorTextArea();
            area.selectAll();
        }
    }
}
