package com.gmail.mohitsainiknl2.mynotepad;

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
import javax.swing.SwingUtilities;
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
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import com.gmail.mohitsainiknl2.mynotepad.editorcontrols.ActionHendling;
import com.gmail.mohitsainiknl2.mynotepad.guihelper.NotepadStatusBar;
import com.gmail.mohitsainiknl2.mynotepad.guihelper.OptionPane;
import com.gmail.mohitsainiknl2.mynotepad.initialvalues.InitialParameters;

public class MainLauncher {
	private static JFrame frame;
	private static JScrollPane editorScrollPane;
	private static JMenuBar menuBar;
	private static JPanel mainPanel;
	private static JTextArea editorTextArea;
	private static NotepadStatusBar statusBar;
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

	public MainLauncher() {
		frame = new JFrame() {
			@Override
			public void setTitle(String fileName) {
				fileName = fileName + " - ";
				String unSavedMark = isSaved.get() ? "" : "*";
				super.setTitle(unSavedMark + fileName + "MyNotepad");
			}
		};
		InitialParameters.readFromFile();
		frame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				if(MainLauncher.getIsSaved() == false) {
					int value = OptionPane.showOptionPane();
					if(value == 1) {
						MainLauncher.getSaveAsMenuItem().doClick();
					} 
					else if(value == 2) {
						return;
					}
				}
				InitialParameters.writeToFile();
			}
		});
		frame.setSize(InitialParameters.getFrameWidth(), InitialParameters.getFrameHeight());

		frame.setTitle(InitialParameters.getFileName());
		mainPanel = (JPanel) frame.getContentPane();

		initialiseMenuBar();
		initialiseScrollPane();
		frame.setJMenuBar(menuBar);
		
		statusBar = new NotepadStatusBar();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(editorScrollPane, BorderLayout.CENTER);
		mainPanel.add(statusBar, BorderLayout.SOUTH);
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {	//<-------- Main Mathod
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				try{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception ee) {
					ee.printStackTrace();
				}
				new MainLauncher();
			}
		});
	}
	
	public static JFrame getFrame() {
		return frame;
	}
	
	public static JTextArea getEditorTextArea() {
		return editorTextArea;
	}

	public static JPanel getStatusBar() {
		return statusBar;
	}

	public static JMenuItem getSaveAsMenuItem() {
		return saveAsFile;
	}

	public static boolean getIsSaved() {
		return isSaved.get();
	}

	public static void setIsSaved(boolean value) {
		isSaved.set(value);
		updateFrameTitle();
	}

	public static void setIsLoadingFile(boolean value) {
		isLoadingFile.set(value);
	}

	public static int getZoomValue() {
		return zoomLevel;
	}

	public static void setZoomValue(int value) {
		zoomLevel = value;
		editorTextArea.setFont(InitialParameters.getEditorFont());
	}

	public static void updateFrameTitle() {
		frame.setTitle(InitialParameters.getFileName());
	}

	private void initialiseScrollPane() {
		editorTextArea = new JTextArea(){
			@Override
			public void setFont(Font font) {
				String family = font.getFamily();
				int style = font.getStyle();
				int size = font.getSize();

				int originalPix = InitialParameters.getEditorFont().getSize() + 5;
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

		Font font = InitialParameters.getEditorFont();
		editorTextArea.setFont(font);
		editorTextArea.setLineWrap(InitialParameters.getWrapTheLine());
		editorTextArea.setTabSize(4);

		if(InitialParameters.getFilePath() != null) {
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

	private void loadFileToEditor() {
		setIsLoadingFile(true);
		String path = InitialParameters.getFilePath();
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
			InitialParameters.setFileName(InitialParameters.NEW_FILE);
			InitialParameters.setFilePath(null);
			InitialParameters.writeToFile();
		}
		editorTextArea.setText(fileText.substring(0, fileText.length() - 1));
		try {
			editorTextArea.setCaretPosition(InitialParameters.getCaretPosition());
		} catch (Exception e) {
			editorTextArea.setCaretPosition(0);
			InitialParameters.setCaretPosition(0);
		}
		setIsLoadingFile(false);
	}

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
		newFile = new JMenuItem(new ActionHendling.NewFileAction());
		newWindowFile = new JMenuItem(new ActionHendling.NewWindowFileAction());
		openFile = new JMenuItem(new ActionHendling.OpenFileAction());
		saveFile = new JMenuItem(new ActionHendling.SaveFileAction());
		saveAsFile = new JMenuItem(new ActionHendling.SaveAsFileAction());
		exitFile = new JMenuItem(new ActionHendling.ExitFileAction());
		fileMenu.add(newFile);
		fileMenu.add(newWindowFile);
		fileMenu.addSeparator();
		fileMenu.add(openFile);
		fileMenu.add(saveFile);
		fileMenu.add(saveAsFile);
		fileMenu.addSeparator();
		fileMenu.add(exitFile);


		undoEdit = new JMenuItem(new ActionHendling.UndoEditAction());
		cutEdit = new JMenuItem(ActionHendling.getCutAction());
		copyEdit = new JMenuItem(ActionHendling.getCopyAction());
		pasteEdit = new JMenuItem(ActionHendling.getPasteAction());
		deleteEdit = new JMenuItem(new ActionHendling.DeleteEditAction());
		findEdit = new JMenuItem(new ActionHendling.FindEditAction());
		replaceEdit = new JMenuItem(new ActionHendling.ReplaceEditAction());
		selectAllEdit = new JMenuItem(new ActionHendling.SelectAllEditAction());
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
		
		wordWrapFormat = new JCheckBoxMenuItem(new ActionHendling.WordWrapFormatAction());
		wordWrapFormat.setState(InitialParameters.getWrapTheLine());
		fontChangeFormat = new JMenuItem(new ActionHendling.FontChangeFormatAction());
		formatMenu.add(wordWrapFormat);
		formatMenu.add(fontChangeFormat);
		
		zoomView = new JMenu("Zoom");
		statusBarView = new JCheckBoxMenuItem(new ActionHendling.StatusBarViewAction());
		statusBarView.setState(InitialParameters.getShowStatusBar());
		viewMenu.add(zoomView);
		viewMenu.add(statusBarView);
		
		viewHelp = new JMenuItem(new ActionHendling.ViewHelpAction());
		sendFeedback = new JMenuItem(new ActionHendling.SendFeedbackAction());
		aboutNotepad = new JMenuItem(new ActionHendling.AboutNotepadAction());
		helpMenu.add(viewHelp);
		helpMenu.add(sendFeedback);
		helpMenu.addSeparator();
		helpMenu.add(aboutNotepad);
		
		zoomIn = new JMenuItem(new ActionHendling.ZoomInAction());
		zoomOut = new JMenuItem(new ActionHendling.ZoomOutAction());
		defaultZoom = new JMenuItem(new ActionHendling.DefaultZoomAction());
		zoomView.add(zoomIn);
		zoomView.add(zoomOut);
		zoomView.add(defaultZoom);

		setRemainingMnemonicAndAccelerator();
	}
	
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