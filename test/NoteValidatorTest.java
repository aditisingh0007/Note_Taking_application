import org.junit.Assert;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.javaquizplayer.examples.notesapp.*;


public class NoteValidatorTest {


	private static NoteListModel model;

	
	@BeforeClass
	public static void init() {
	
		Note n1 = new Note("Note 1", "");
		n1.setId(1);
		Note n2 = new Note("Note 2", "");
		n2.setId(2);
		Note n3 = new Note("Note 3", "");
		n3.setId(3);
		Note n4 = new Note("Note 9", "");
		n4.setId(9);
		List<Note> data =
			new ArrayList<Note>(Arrays.asList(n1, n2, n3, n4));
		model = new NoteListModel(data);
	}
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	
	@Test
	public void testConstruction() {
		
		Assert.assertNotNull(new NoteValidator());
	}
	
	/*** Tests for Add Note function ***/
	
	@Test
	public void testValidateNewNoteNoExceptionExpected()
			throws InvalidNoteException {
		
		Note n1 = new Note("NOTE 91", "valid note 91");
		n1.setId(91);
		Note n2 = new Note("note_92", "");
		n2.setId(92);
		
		new NoteValidator().validate(null, n1, model, false);
		new NoteValidator().validate(null, n2, model, false);
	}
	
	@Test
	public void testValidateNewNoteInvalidNameLengthExceptionExpected()
			throws InvalidNoteException {
		
		Note n1 = new Note("NOTE", "");
		n1.setId(91);
		Note n2 = new Note("This is a Note with a long length", "");
		n2.setId(92);
		Note n3 = new Note("", "");
		n1.setId(93);
				
		expectedException.expect(InvalidNoteException.class);
		expectedException.expectMessage("Name must be 5 to 25 characters long!");
		new NoteValidator().validate(null, n1, model, false);
		new NoteValidator().validate(null, n2, model, false);
		new NoteValidator().validate(null, n3, model, false);
	}
	
	@Test
	public void testValidateNewNoteWithInvalidCharactersExceptionExpected()
			throws InvalidNoteException {
		
		Note n1 = new Note("NOTE-1", "");
		n1.setId(91);
		Note n2 = new Note("Note.", "");
		n2.setId(92);
				
		expectedException.expect(InvalidNoteException.class);
		expectedException.expectMessage("Name can only have (a-z A-Z 0-9 space and _) characters!");
		new NoteValidator().validate(null, n1, model, false);
		new NoteValidator().validate(null, n2, model, false);
	}
	
	@Test
	public void testValidateNewNoteNameAlreadyExistsExceptionExpected()
			throws InvalidNoteException {
		
		Note n = new Note("Note 1", "");
		n.setId(91);
				
		expectedException.expect(InvalidNoteException.class);
		expectedException.expectMessage("Name already exists!");
		new NoteValidator().validate(null, n, model, false);
	}
	
	/*** Tests for Update Note function ***/
	
	@Test
	public void testValidateUpdateNoteNoExceptionExpected()
			throws InvalidNoteException {
	
		Note n1 = model.getElementAt(2);
		Note n2 = model.getElementAt(2);
		n2.setName("Note 3 updated");
		
		new NoteValidator().validate(n1, n2, model, true);
	}
	
	@Test
	public void testValidateUpdateNoteInvalidNameLengthExceptionExpected()
			throws InvalidNoteException {
		
		Note n1 = model.getElementAt(1);
		Note n2 = model.getElementAt(1);
		n2.setName("Not1");
				
		expectedException.expect(InvalidNoteException.class);
		expectedException.expectMessage("Name must be 5 to 25 characters long!");
		
		new NoteValidator().validate(n1, n2, model, true);
		
		n2.setName("Note name with long length 99");
		new NoteValidator().validate(n1, n2, model, true);
		
		n2.setName("");
		new NoteValidator().validate(n1, n2, model, true);
	}
	
	@Test
	public void testValidateUpdateNoteWithInvalidCharactersExceptionExpected()
			throws InvalidNoteException {
		
		Note n1 = model.getElementAt(1);
		Note n2 = model.getElementAt(1);
		n2.setName("Not-invalid");
				
		expectedException.expect(InvalidNoteException.class);
		expectedException.expectMessage("Name can only have (a-z A-Z 0-9 space and _) characters!");
		new NoteValidator().validate(n1, n2, model, true);
	}

	@Test
	public void testValidateUpdateNoteNameAlreadyExistsExceptionExpected()
			throws InvalidNoteException {
		
		Note n1 = model.getElementAt(3);
		Note n2 = new Note("Note 1", "A Test Note");				
		expectedException.expect(InvalidNoteException.class);
		expectedException.expectMessage("Name already exists!");
		new NoteValidator().validate(n1, n2, model, true);
	}
}