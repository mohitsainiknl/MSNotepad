package msnotepad.action;

import javax.swing.Action;
import javax.swing.text.DefaultEditorKit;

public class ClipboardActions {
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
}
