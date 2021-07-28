/*
 * Copyright (c) 2021 Mohit Saini, Under MIT License. Use is subject to license terms.
 * 
 */

package msnotepad.gui;

import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.event.CaretEvent;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import msnotepad.action.ClipboardActions;
import msnotepad.action.EditMenuActions;
import msnotepad.action.FileMenuActions;
import msnotepad.action.FormatMenuActions;
import msnotepad.action.HelpMenuActions;
import msnotepad.action.ViewMenuActions;
import msnotepad.gui.helper.StatusBar;
import msnotepad.gui.helper.OptionPane;
import msnotepad.init.InitialValues;

/**
 * GUIHandler class handle the main gui functioning of the MSNotepad, this is the point of
 * distribution of work to other classes.
 */
public class GUIHandler {
	private static JFrame frame;
	private static JScrollPane editorScrollPane;
	private static JMenuBar menuBar;
	private static JPanel mainPanel;
	private static JTextArea editorTextArea;
	private static StatusBar statusBar;
	private static AtomicBoolean isSaved = new AtomicBoolean(true);
	private static AtomicBoolean isLoadingFile = new AtomicBoolean(false);

	private static JMenu fileMenu, editMenu, formatMenu, viewMenu, helpMenu;
	private static JMenuItem newFile, newWindowFile, openFile, saveFile, saveAsFile, exitFile;
	private static JMenuItem undoEdit,cutEdit, copyEdit, pasteEdit,deleteEdit, findEdit, replaceEdit, selectAllEdit;

	private static JCheckBoxMenuItem wordWrapFormat;
	private static JMenuItem fontChangeFormat;

	private static JMenu zoomView;
	private static int zoomLevel = 100;
	private static JMenuItem zoomIn, zoomOut, defaultZoom;
	public static JCheckBoxMenuItem statusBarView;

	private static JMenuItem viewHelp, sendFeedback, aboutNotepad;

	/**
	 * handle method is help to setup the major components and getting the frame ready to
	 * make it visible on the user screen.
	 */
	public void handle() {
		frame = new JFrame() {
			@Override
			public void setTitle(String fileName) {
				fileName = fileName + " - ";
				String unSavedMark = isSaved.get() ? "" : "*";
				super.setTitle(unSavedMark + fileName + "MyNotepad");
			}
		};
		InitialValues.readFromFile();
		frame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				if(GUIHandler.getIsSaved() == false) {
					int value = OptionPane.showOptionPane();
					if(value == 1) {
						GUIHandler.getSaveAsMenuItem().doClick();
					} 
					else if(value == 2) {
						return;
					}
				}
				InitialValues.writeToFile();
			}
		});
		frame.setSize(InitialValues.getFrameWidth(), InitialValues.getFrameHeight());

		frame.setTitle(InitialValues.getFileName());
		mainPanel = (JPanel) frame.getContentPane();

		initialiseMenuBar();
		initialiseScrollPane();
		frame.setJMenuBar(menuBar);
		
		statusBar = new StatusBar();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(editorScrollPane, BorderLayout.CENTER);
		mainPanel.add(statusBar, BorderLayout.SOUTH);
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}


	/**
	 * getFrame is the getter of the frame.
	 * @return the frame.
	 */
	public static JFrame getFrame() {
		return frame;
	}


	/**
	 * getEditorTextArea method is the getter of main text-area.
	 * @return the editorTextArea.
	 */
	public static JTextArea getEditorTextArea() {
		return editorTextArea;
	}


	/**
	 * getStatusBar methoad is the getter of statusBar.
	 * @return the statusBar.
	 */
	public static JPanel getStatusBar() {
		return statusBar;
	}


	/**
	 * getSaveAsMenuItem method is the getter of saveAsFile.
	 * @return the saveAsFile.
	 */
	public static JMenuItem getSaveAsMenuItem() {
		return saveAsFile;
	}


	/**
	 * getIsSaved method is the getter of isSaved variable.
	 * @return the isSaved variable.
	 */
	public static boolean getIsSaved() {
		return isSaved.get();
	}


	/**
	 * setIsSave method is set the isSaved value the variable.
	 * @param value the value of isSaved.
	 */
	public static void setIsSaved(boolean value) {
		isSaved.set(value);
		updateFrameTitle();
	}


	/**
	 * setIsLoadingFile method is set the loading flag of the this app.
	 * @param value loading flag.
	 */
	public static void setIsLoadingFile(boolean value) {
		isLoadingFile.set(value);
	}


	/**
	 * getZoomValue method is help to the zoomValue of the textArea.
	 * @return the zoomLevel.
	 */
	public static int getZoomValue() {
		return zoomLevel;
	}


	/**
	 * setZoomValue method is help to set the zoom level of the editor textArea.
	 * @param value the zoom level.
	 */
	public static void setZoomValue(int value) {
		zoomLevel = value;
		editorTextArea.setFont(InitialValues.getEditorFont());
	}


	/**
	 * updateFrameTitle method is help to chanage the file name and unsaved mark
	 * of the opened file.
	 */
	public static void updateFrameTitle() {
		frame.setTitle(InitialValues.getFileName());
	}


	/**
	 * initialiseScrollPane method is help to setup the text editor of the MSNotepad.
	 */
	private void initialiseScrollPane() {
		editorTextArea = new JTextArea(){
			@Override
			public void setFont(Font font) {
				String family = font.getFamily();
				int style = font.getStyle();
				int size = font.getSize();

				int originalPix = InitialValues.getEditorFont().getSize() + 5;
				int zoomPix = (zoomLevel * originalPix) / 100 - originalPix;
				
				font = new Font(family, style, size + zoomPix + 5);
				super.setFont(font);
			}
		};
		editorScrollPane = new JScrollPane(editorTextArea);
		editorScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		editorScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		editorScrollPane.setBorder(new LineBorder(Color.WHITE, 0));
		Border outside = new MatteBorder(1, 0, 0, 0, mainPanel.getBackground());
		Border inside = new MatteBorder(0, 4, 0, 0, editorTextArea.getBackground());
		editorTextArea.setBorder(new CompoundBorder(outside, inside));

		Font font = InitialValues.getEditorFont();
		editorTextArea.setFont(font);
		editorTextArea.setLineWrap(InitialValues.getWrapTheLine());
		editorTextArea.setTabSize(4);

		if(InitialValues.getFilePath() != null) {
			loadFileToEditor();
		}

		doUpdateWork();
		editorTextArea.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				if(editorTextArea.getSelectedText() == null){
					cutEdit.setEnabled(false);
					copyEdit.setEnabled(false);
				}
				else {
					cutEdit.setEnabled(true);
					copyEdit.setEnabled(true);
				}
			}
		});
		editorTextArea.getDocument().addDocumentListener(new DocumentListener() {
	
			@Override
			public void removeUpdate(DocumentEvent e) {
				if(isLoadingFile.get() == false) {
					setIsSaved(false);
				}
				doUpdateWork();
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				if(isLoadingFile.get() == false) {
					setIsSaved(false);
				}
				doUpdateWork();
			}
			@Override
			public void changedUpdate(DocumentEvent arg0) {}
		});
		
		editorTextArea.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {

				JTextArea editorArea = (JTextArea) e.getSource();
				int lineNum = 1;
				int columnNum = 1;

				int caretPosition = 1;
				try {
					caretPosition = editorArea.getCaretPosition();
					lineNum = editorArea.getLineOfOffset(caretPosition);
					columnNum = caretPosition - editorArea.getLineStartOffset(lineNum);

				} catch (BadLocationException ee) {
					ee.printStackTrace();
				}	
				statusBar.setCaretPosition(lineNum, columnNum);				
			}
        });
	}


	/**
	 * doUpdateWork method is help to set enable/disable the edit action of the
	 * text editor.
	 */
	private void doUpdateWork() {
		undoEdit.setEnabled(false);
		if(editorTextArea.getText().equals("")) {
			findEdit.setEnabled(false);
			replaceEdit.setEnabled(false);
		}
		else {
			findEdit.setEnabled(true);
			replaceEdit.setEnabled(true);
		}
	}


	/**
	 * loadFileToEditor method is help to load/reload the file,
	 * whose name is saved in InitialValues class.
	 */
	private void loadFileToEditor() {
		setIsLoadingFile(true);
		String path = InitialValues.getFilePath();
		File file = new File(path);
		String fileText = "";
		try {
			Scanner fileReader = new Scanner(file);
			while(fileReader.hasNextLine()) {
				fileText = fileText + fileReader.nextLine() + "\n";
			}
			fileReader.close();
			setIsSaved(true);
		} catch (IOException ex) {
			System.out.println(path);
			System.out.println("File not found...");
			InitialValues.setFileName(InitialValues.NEW_FILE);
			InitialValues.setFilePath(null);
			InitialValues.writeToFile();
		}
		editorTextArea.setText(fileText.substring(0, fileText.length() - 1));
		try {
			editorTextArea.setCaretPosition(InitialValues.getCaretPosition());
		} catch (Exception e) {
			editorTextArea.setCaretPosition(0);
			InitialValues.setCaretPosition(0);
		}
		setIsLoadingFile(false);
	}


	/**
	 * initialiseMenuBar method is help to initialise and setup the menubar
	 * of this app, and also initialize the menus and their item in the menu.
	 */
	private void initialiseMenuBar() {
		menuBar = new JMenuBar();
		menuBar.setBorderPainted(false);
		
		fileMenu = new JMenu("File");
		editMenu = new JMenu("Edit");
		formatMenu = new JMenu("Format");
		viewMenu = new JMenu("View");
		helpMenu = new JMenu("Help");
		initialiseMenuItems();
		
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(formatMenu);
		menuBar.add(viewMenu);
		menuBar.add(helpMenu);
	}
	
	private void initialiseMenuItems() {
		newFile = new JMenuItem(new FileMenuActions.NewFileAction());
		newWindowFile = new JMenuItem(new FileMenuActions.NewWindowFileAction());
		openFile = new JMenuItem(new FileMenuActions.OpenFileAction());
		saveFile = new JMenuItem(new FileMenuActions.SaveFileAction());
		saveAsFile = new JMenuItem(new FileMenuActions.SaveAsFileAction());
		exitFile = new JMenuItem(new FileMenuActions.ExitFileAction());
		fileMenu.add(newFile);
		fileMenu.add(newWindowFile);
		fileMenu.addSeparator();
		fileMenu.add(openFile);
		fileMenu.add(saveFile);
		fileMenu.add(saveAsFile);
		fileMenu.addSeparator();
		fileMenu.add(exitFile);


		cutEdit = new JMenuItem(ClipboardActions.getCutAction());
		copyEdit = new JMenuItem(ClipboardActions.getCopyAction());
		pasteEdit = new JMenuItem(ClipboardActions.getPasteAction());
		undoEdit = new JMenuItem(new EditMenuActions.UndoEditAction());
		deleteEdit = new JMenuItem(new EditMenuActions.DeleteEditAction());
		findEdit = new JMenuItem(new EditMenuActions.FindEditAction());
		replaceEdit = new JMenuItem(new EditMenuActions.ReplaceEditAction());
		selectAllEdit = new JMenuItem(new EditMenuActions.SelectAllEditAction());
		editMenu.add(undoEdit);
		editMenu.addSeparator();
		editMenu.add(cutEdit);
		editMenu.add(copyEdit);
		editMenu.add(pasteEdit);
		editMenu.addSeparator();
		editMenu.add(findEdit);
		editMenu.add(replaceEdit);
		editMenu.addSeparator();
		editMenu.add(deleteEdit);
		editMenu.add(selectAllEdit);
		
		wordWrapFormat = new JCheckBoxMenuItem(new FormatMenuActions.WordWrapFormatAction());
		wordWrapFormat.setState(InitialValues.getWrapTheLine());
		fontChangeFormat = new JMenuItem(new FormatMenuActions.FontChangeFormatAction());
		formatMenu.add(wordWrapFormat);
		formatMenu.add(fontChangeFormat);
		
		zoomView = new JMenu("Zoom");
		statusBarView = new JCheckBoxMenuItem(new ViewMenuActions.StatusBarViewAction());
		statusBarView.setState(InitialValues.getShowStatusBar());
		viewMenu.add(zoomView);
		viewMenu.add(statusBarView);

		viewHelp = new JMenuItem(new HelpMenuActions.ViewHelpAction());
		sendFeedback = new JMenuItem(new HelpMenuActions.SendFeedbackAction());
		aboutNotepad = new JMenuItem(new HelpMenuActions.AboutNotepadAction());
		helpMenu.add(viewHelp);
		helpMenu.add(sendFeedback);
		helpMenu.addSeparator();
		helpMenu.add(aboutNotepad);
		
		zoomIn = new JMenuItem(new ViewMenuActions.ZoomInAction());
		zoomOut = new JMenuItem(new ViewMenuActions.ZoomOutAction());
		defaultZoom = new JMenuItem(new ViewMenuActions.DefaultZoomAction());
		zoomView.add(zoomIn);
		zoomView.add(zoomOut);
		zoomView.add(defaultZoom);

		setRemainingMnemonicAndAccelerator();
	}


	/**
	 * setRemainingMnemonicAndAccelerator method is help to set the MnemonicKeys and
	 * the Accelerator of the remaining items of the menus in the menubar.
	 */
	private void setRemainingMnemonicAndAccelerator() {

		fileMenu.setMnemonic(KeyEvent.VK_F);
		editMenu.setMnemonic(KeyEvent.VK_E);
		formatMenu.setMnemonic(KeyEvent.VK_O);
		viewMenu.setMnemonic(KeyEvent.VK_V);
		helpMenu.setMnemonic(KeyEvent.VK_H);

		cutEdit.setMnemonic(KeyEvent.VK_T);
		copyEdit.setMnemonic(KeyEvent.VK_C);
		pasteEdit.setMnemonic(KeyEvent.VK_P);

		zoomView.setMnemonic(KeyEvent.VK_Z);

		cutEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		copyEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		pasteEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
	}
}