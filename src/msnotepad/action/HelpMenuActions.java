package msnotepad.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import javax.swing.AbstractAction;

import msnotepad.gui.GUIHandler;
import msnotepad.guihelper.AboutDialog;


public class HelpMenuActions {

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
            AboutDialog dialog = new AboutDialog(GUIHandler.getFrame(), "About MyNotepad", true);
            dialog.setVisible(true);
        }
    }
}
