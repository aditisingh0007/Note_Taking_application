package com.javaquizplayer.examples.notesapp;

import javax.swing.SwingUtilities;

public class ApplicationStarter {


	public static void main(String [] args) {
	
		SwingUtilities.invokeLater(new Runnable() {
    
			public void run() {
        
				new NotesAppGui(new NotePersistence());
			}
		});
	}
}