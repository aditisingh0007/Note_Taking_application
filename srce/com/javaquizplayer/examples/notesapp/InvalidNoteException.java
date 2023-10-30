package com.javaquizplayer.examples.notesapp;

public class InvalidNoteException extends Exception {

	public InvalidNoteException() {
	}
	
	public InvalidNoteException(String message) {
	
		super(message);
	}
}