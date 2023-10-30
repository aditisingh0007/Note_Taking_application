package com.javaquizplayer.examples.notesapp;

import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;

import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;


public class HelpDialog {


	private static final String HELP_TEXT_FILE_PATH = "resources/help.txt";
	private static final Font HELP_TEXT_FONT = new Font("Georgia", Font.PLAIN, 16);


	public HelpDialog(JFrame frame) {
	
		JDialog dialog = new JDialog(frame, "Help", true);
		JScrollPane scroller = getTextAreaForHelp();
		dialog.add(scroller);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		Dimension size = new Dimension(600, 350); // w, h
		dialog.setSize(size); 	
		Point location = getDialogLocationCenteredToFrame(frame, size);
		dialog.setLocation(location); // x, y			
		dialog.setResizable(false);
		dialog.setVisible(true);	
	}

	private Point getDialogLocationCenteredToFrame(JFrame frame, Dimension dialogSize) {

		Point frameLocation = frame.getLocationOnScreen();
		int fh = frame.getHeight();
		int fw = frame.getWidth();			
		int dx = frameLocation.x + ((fw - dialogSize.width) / 2);
		int dy = frameLocation.y + ((fh - dialogSize.height) / 2);
		
		return new Point(dx, dy);
	}
	
	private JScrollPane getTextAreaForHelp() {
			
		JTextArea text = new JTextArea(loadHelpText());
		text.setFont(HELP_TEXT_FONT);
		text.setEditable(false);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setMargin(new Insets(5, 5, 5, 5)); // top, left, bottom, rt

		JScrollPane scroller = new JScrollPane(text);
		scroller.setVerticalScrollBarPolicy(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		return scroller;	
	}

	private String loadHelpText() {
		
		byte [] bytes = null;
			
		try {
			bytes = Files.readAllBytes(Paths.get(HELP_TEXT_FILE_PATH));
		}
		catch (IOException ex) {
			
			bytes = ("There was an error loading the Help file: " +
						HELP_TEXT_FILE_PATH).getBytes();
		}
			
		return new String(bytes);
	}
}