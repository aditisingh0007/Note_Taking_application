import org.junit.Assert;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.javaquizplayer.examples.notesapp.*;


public class NoteListModelTest {

	
	private List<Note> noteData;
	private List<Note> emptyData;
	private List<Note> unsortedData;
	
	@Before
	public void createSomeNoteDataForTesting() {
	
		Note n1 = new Note("Note 1", "");
		n1.setId(1);
		Note n2 = new Note("Note 2", "");
		n2.setId(2);
		Note n3 = new Note("Note 3", "");
		n3.setId(3);
		noteData =
			new ArrayList<Note>(Arrays.asList(new Note[] {n1, n2, n3}));
		emptyData = new ArrayList<Note>();
		unsortedData =
			new ArrayList<Note>(Arrays.asList(new Note[] {n2, n1, n3}));
	}

	@Test
	public void testConstructionWithDataInput() {
		
		Assert.assertNotNull(new NoteListModel(emptyData));
		Assert.assertTrue(new NoteListModel(emptyData).isEmpty());	
		Assert.assertNotNull(new NoteListModel(noteData));
	}

	@Test(expected = java.lang.NullPointerException.class)
	public void testConstructionWithNullInputParameter() {
	
		new NoteListModel(null);
	}
	
	@Test
	public void testGetSize() {

		Assert.assertEquals(0, new NoteListModel(emptyData).getSize());
		Assert.assertEquals(3, new NoteListModel(noteData).getSize());
	}	

	@Test
	public void testGetElementAtIndex() {
	
		Assert.assertEquals(noteData.get(0),
				new NoteListModel(noteData).getElementAt(0));
		Assert.assertEquals(null, new NoteListModel(emptyData).getElementAt(0));
	}
	
	@Test(expected = java.lang.ArrayIndexOutOfBoundsException.class)
	public void testGetElementAtWithNegativeIndex() {

		new NoteListModel(noteData).getElementAt(-1);
	}
	
	@Test
	public void testIndexOfNote() {
	
		Note exists = new Note("Note 1", "");
		exists.setId(1);
		Assert.assertEquals(0, new NoteListModel(noteData).indexOf(exists));
		
		Note notExists = new Note("Note none", "");
		notExists.setId(999);
		Assert.assertEquals(-1, new NoteListModel(noteData).indexOf(notExists));
	}

	@Test
	public void testInputNoteContainsName() {
	
		Note exists = new Note("Note 1", "");
		exists.setId(1);
		Assert.assertTrue(new NoteListModel(noteData).containsName(exists));
		
		Note notExists = new Note("Note none", "");
		notExists.setId(999);
		Assert.assertFalse(new NoteListModel(noteData).containsName(notExists));
	}
	
	@Test(expected = java.lang.NullPointerException.class)
	public void testInputNullContainsName() {	
	
		new NoteListModel(noteData).containsName(null);
	}
	
	@Test
	public void testAddNote() {
	
		Note newNote = new Note("Note 9", "");
		newNote.setId(9);
		NoteListModel model = new NoteListModel(emptyData);
		model.add(newNote);
		Assert.assertEquals(1, model.getSize());
		Assert.assertEquals(newNote, model.getElementAt(0));
		Assert.assertSame(newNote, model.getElementAt(0));
		Assert.assertTrue(model.containsName(newNote));
	}
	
	@Test
	public void testDeleteNote() {
	
		Note newNote = new Note("Note 9", "");
		newNote.setId(9);
		NoteListModel model = new NoteListModel(emptyData);
		model.add(newNote);	
		Assert.assertSame(newNote, model.getElementAt(0));
		
		model.delete(newNote);
		Assert.assertNull(model.getElementAt(0));
		Assert.assertTrue(model.isEmpty());
	}
	
	@Test
	public void testUpdateNote() {
	
		String name = "Note-9";
		Note note = new Note(name, "");
		note.setId(9);
		NoteListModel model = new NoteListModel(emptyData);
		model.add(note);
		
		Assert.assertEquals(1, model.getSize());
		Assert.assertSame(note, model.getElementAt(0));
		Assert.assertTrue(model.containsName(note));
		
		String updatedName = "new-Note";
		note.setName(updatedName);
		model.update(note);
		
		Assert.assertEquals(1, model.getSize());
		Assert.assertSame(note, model.getElementAt(0));
		Assert.assertTrue(model.containsName(note));
		Assert.assertEquals(updatedName, model.getElementAt(0).getName());	
	}
	
	@Test
	public void testDataIsSortedAfterContruction() {
	
		String unsortedNotesStr = "[Note 2, Note 1, Note 3]";
		String sortedNotesStr = "[Note 1, Note 2, Note 3]";
		Assert.assertEquals(unsortedNotesStr, unsortedData.toString());
		Assert.assertEquals(sortedNotesStr, noteData.toString());
		Assert.assertNotEquals(unsortedNotesStr, sortedNotesStr);
		Assert.assertFalse(Arrays.equals(unsortedData.toArray(new Note[0]),
				noteData.toArray(new Note[0])));
		
		NoteListModel model = new NoteListModel(unsortedData);
		Assert.assertEquals(sortedNotesStr, model.getData().toString());
		Assert.assertNotEquals(unsortedNotesStr, model.getData().toString());
	}

	@Test
	public void testDataIsSortedAfterAddNote() {

		String sortedNotesStr = "[Note 1, Note 2, Note 3]";
		Assert.assertEquals(sortedNotesStr, noteData.toString());
			
		NoteListModel model = new NoteListModel(noteData);
		Assert.assertEquals(sortedNotesStr, model.getData().toString());
		
		Note newNote = new Note("Note 11", "");
		newNote.setId(11);
		model.add(newNote);
		
		String sortedNotesStrAfterAdd = "[Note 1, Note 11, Note 2, Note 3]";
		Assert.assertEquals(sortedNotesStrAfterAdd, model.getData().toString());
	}

	@Test
	public void testDataIsSortedAfterUpdateNote() {

		String sortedNotesStr = "[Note 1, Note 2, Note 3]";
		Assert.assertEquals(sortedNotesStr, noteData.toString());
			
		NoteListModel model = new NoteListModel(noteData);
		Assert.assertEquals(sortedNotesStr, model.getData().toString());

		Note note = model.getElementAt(1);
		Assert.assertEquals("Note 2", note.getName());
		
		String updatedName = "Note-99";
		note.setName(updatedName);

		model.update(note);
		
		String sortedNotesStrAfterUpdate = "[Note 1, Note 3, Note-99]";
		Assert.assertEquals(sortedNotesStrAfterUpdate, model.getData().toString());
	}
}