/*
 * Copyright (c) 2021 Mohit Saini, Under MIT License. Use is subject to license terms.
 * 
 */

package msnotepad.action;

import javax.swing.Action;
import javax.swing.text.DefaultEditorKit;

/**
 * ClipboardActions class handle the clipboard actions like- cut, copy, paste.
 */
public class ClipboardActions {
    private static Action cut = new DefaultEditorKit.CutAction();
    private static Action copy = new DefaultEditorKit.CopyAction();
    private static Action paste = new DefaultEditorKit.PasteAction();

    /**
     * getCutAction method help to cut the text on the clipboard.
     * @return the cut action.
     */
    public static Action getCutAction() {
        cut.putValue(Action.NAME, "Cut");
        return cut;
    }

    /**
     * getCopyAction method help to copy the text on the clipboard.
     * @return the copy action.
     */
    public static Action getCopyAction() {
        copy.putValue(Action.NAME, "Copy");
        return copy;
    }

    /**
     * getPasteAction method help to paste the text on the clipboard.
     * @return the paste action.
     */
    public static Action getPasteAction() {
        paste.putValue(Action.NAME, "Paste");
        return paste;
    }
}
