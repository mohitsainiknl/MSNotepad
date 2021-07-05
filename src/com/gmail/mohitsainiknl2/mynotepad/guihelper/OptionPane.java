package com.gmail.mohitsainiknl2.mynotepad.guihelper;

import java.awt.Font;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.gmail.mohitsainiknl2.mynotepad.MainLauncher;
import com.gmail.mohitsainiknl2.mynotepad.initialvalues.InitialParameters;

public class OptionPane extends JOptionPane {
    private static String[] options;
    private static String message;
    private static JLabel label;
    
	public static int showOptionPane() {
		options = new String[] {"Don't Save", "Save", "Cancel"};
		message = "Do you want to save changes to \"" + InitialParameters.getFileName() + "\" ?";

		label = new JLabel(message);
		label.setBackground(Color.WHITE);
		label.setFont(new Font("", Font.PLAIN, 14 + 2));
		label.setForeground(new Color(0, 51, 153));

        return showOptionDialog(
			MainLauncher.getFrame(),
			label,
			"MyNotepad",
			YES_NO_CANCEL_OPTION,
			PLAIN_MESSAGE,
			null,
            options,
            options[0]
		);
	}
}
