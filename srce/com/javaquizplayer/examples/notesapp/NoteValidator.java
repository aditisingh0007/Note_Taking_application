package com.javaquizplayer.examples.notesapp;

public class NoteValidator {


	private static final int MIN_LENGTH = 5;
	private static final int MAX_LENGTH = 25;

	
	public void validate(Note originalN, Note newN, NoteListModel model,
							boolean updateFlag)
			throws InvalidNoteException {
	
		String name = newN.getName();
		int len = name.length();
		
		if ((len < MIN_LENGTH) || (len > MAX_LENGTH)) {
		
			throw new InvalidNoteException("Name must be 5 to 25 characters long!");
		}
		
		if (! nameHasValidCharacters(name)) {
		
			throw new InvalidNoteException("Name can only have (a-z A-Z 0-9 space and _) characters!");
		}
		
		if ((updateFlag) && (originalN.getName().equals(name))) {
		
			return;
		}
		
		if (model.containsName(newN)) {
		
			throw new InvalidNoteException("Name already exists!");
		}
	}
	
	private boolean nameHasValidCharacters(String input) {
			
		char [] cc = input.toCharArray();
			
		for (char c : cc) {
			
			if ((Character.isLetterOrDigit(c)) ||
					(Character.isSpaceChar(c)) ||
					(c == '_')){
				
				continue;
			}
			else {
				return false;
			}
		}
		
		return true;
	}
}