package com.gmail.mohitsainiknl2.mynotepad.initialvalues;

import java.awt.Font;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.gmail.mohitsainiknl2.mynotepad.MainLauncher;

public class InitialParameters {
	public static final String NEW_FILE = "Untitled";
    private static AtomicBoolean showStatusBar = new AtomicBoolean(true);
    private static AtomicBoolean wrapTheLine = new AtomicBoolean(false);
	private static AtomicInteger frameWidth = new AtomicInteger(600);
	private static AtomicInteger frameHeight = new AtomicInteger(450);
	private static Font editorFont = new Font("Consolas", Font.PLAIN, 14);
	private static String fileName = NEW_FILE;
	private static String filePath = null;
	private static AtomicInteger caretPosition = new AtomicInteger(0);

	public static Boolean getShowStatusBar() {
		return showStatusBar.get();
	}
	public static void setShowStatusBar(Boolean value) {
		showStatusBar.set(value);
	}


	public static Boolean getWrapTheLine() {
		return wrapTheLine.get();
	}
	public static void setWrapTheLine(Boolean value) {
		wrapTheLine.set(value);
	}
    

	public static int getFrameWidth() {
		return frameWidth.get();
	}
	public static void setFrameWidth(int value) {
		frameWidth.set(value);
	}


	public static int getFrameHeight() {
		return frameHeight.get();
	}
	public static void setFrameHeight(int value) {
		frameHeight.set(value);
	}


	public static Font getEditorFont() {
		return editorFont;
	}
	public static void setEditorFont(Font font) {
		editorFont = font;
		MainLauncher.getEditorTextArea().setFont(font);
	}


	public static String getFileName() {
		return fileName;
	}
	public static void setFileName(String Name) {
		fileName = Name;
	}


	public static String getFilePath() {
		return filePath;
	}
	public static void setFilePath(String path) {
		filePath = path;
	}


	public static int getCaretPosition() {
		return caretPosition.get();
	}
	public static void setCaretPosition(int value) {
		caretPosition.set(value);
	}

	//--------------  Writing Setting to File -----------------

	public static void writeToFile() {
		try {
			caretPosition.set(MainLauncher.getEditorTextArea().getCaretPosition());
			File file = new File("Settings.txt");
			FileWriter writer = new FileWriter(file);
			writer.write("Show-StatusBar : " + showStatusBar + "\n");
			writer.write("Wrap-The-Line : " + wrapTheLine + "\n");
			writer.write("Frame-Width : " + frameWidth + "\n");
			writer.write("Frame-Height : " + frameHeight + "\n");
			writer.write("Font-Family : " + editorFont.getFamily() + "\n");
			writer.write("Font-Style : " + editorFont.getStyle() + "\n");
			writer.write("Font-Size : " + editorFont.getSize() + "\n");
			writer.write("Opened-File-Name : " + fileName + "\n");
			writer.write("Opened-File-Path : " + filePath + "\n");
			writer.write("Caret-Position : " + caretPosition + "\n");
			writer.close();
		} catch (IOException ex) {
			System.out.println("Unable to write Setting.text file !");
		}
	}

	//--------------  Reading Setting to File -----------------

	public static void readFromFile() {
		try {
			File file = new File("Settings.txt");
			Scanner read = new Scanner(file);
			if(read.hasNextLine())
			{
				String value = read.nextLine();
				value = value.substring(value.indexOf(":") + 1);
				value = value.trim();
				showStatusBar.set(value.equals("true") ? true : false);
			}
			if(read.hasNextLine())
			{
				String value = read.nextLine();
				value = value.substring(value.indexOf(":") + 1);
				value = value.trim();
				wrapTheLine.set(value.equals("true") ? true : false);
			}
			if(read.hasNextLine())
			{
				String width = read.nextLine();
				width = width.substring(width.indexOf(":") + 1);
				width = width.trim();
				frameWidth.set(Integer.parseInt(width));
			}
			if(read.hasNextLine())
			{
				String height = read.nextLine();
				height = height.substring(height.indexOf(":") + 1);
				height = height.trim();
				frameHeight.set(Integer.parseInt(height));
			}
			if(read.hasNextLine())
			{
				String family = read.nextLine();
				family = family.substring(family.indexOf(":") + 1);
				family = family.trim();

				String style = read.nextLine();
				style = style.substring(style.indexOf(":") + 1);
				style = style.trim();

				String size = read.nextLine();
				size = size.substring(size.indexOf(":") + 1);
				size = size.trim();

				editorFont = new Font(family, Integer.parseInt(style), Integer.parseInt(size));
			}
			if(read.hasNextLine())
			{
				String name = read.nextLine();
				name = name.substring(name.indexOf(":") + 1);
				name = name.trim();
				
				String path = read.nextLine();
				path = path.substring(path.indexOf(":") + 1);
				path = path.trim();
				
				String position = read.nextLine();
				position = position.substring(position.indexOf(":") + 1);
				position = position.trim();
				
				fileName = name;
				caretPosition.set(Integer.parseInt(position));
				if(!path.equals("null")) {
					filePath = path;
				}
			}
			read.close();
		} catch (IOException ex) {
			System.out.println("default setting work!");
		}
	}
}
