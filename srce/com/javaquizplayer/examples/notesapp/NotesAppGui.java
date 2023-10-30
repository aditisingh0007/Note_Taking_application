package com.javaquizplayer.examples.notesapp;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Insets;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.Image;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.net.URL;
import java.util.List;
import java.util.ArrayList;


public class NotesAppGui {

		
	private JFrame frame;
	private JList<Note> list;
	private JTextField textFld;
	private JTextArea textArea;	
	private JButton newButton;
	private JButton deleteButton;
	private JButton saveButton;
	private JButton cancelButton;
	private JLabel messageLabel;
	private boolean updateFlag;
	
	enum MessageType {INFO, WARN, NONE};
	enum ActionType {LOAD, CREATE, UPDATE, DELETE};
	
	private NotePersistence noteDAO;
	
	private static final String APP_ICON = "note.png";
	private static final Font FONT_FOR_WIDGETS =
			new Font("SansSerif", Font.PLAIN, 16);
	private static final Font FONT_FOR_EDITOR =
			new Font("Comic Sans MS", Font.PLAIN, 16);


	public NotesAppGui(NotePersistence dao) {
	
		frame = new JFrame("Notes App");
		addWidgetsToFrame(frame);
			
		frame.setIconImage(getAppIcon());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(350, 50); // x, y
		frame.setResizable(false);
		frame.pack();		
		frame.setVisible(true);
		
		noteDAO = dao;
		
		initDataAndUpdateUI();
	}

	private void addWidgetsToFrame(JFrame frame) {
	
		Container pane = frame.getContentPane();
		pane.setLayout(new GridBagLayout());
	
		pane.add(getListInScrollPane(), getConstraintsForList());		
		pane.add(getTextField(), getConstraintsForTextFld());		
		pane.add(getTextAreaInScrollPane(), getConstraintsForTextArea());	
		pane.add(getToolBarWithButtons(), getConstraintsForButtonToolBar());
		pane.add(getMessageLabel(), getConstraintsForMessageLabel());
	}
	
	private Image getAppIcon() {
	
		return new ImageIcon(ClassLoader.getSystemResource(APP_ICON)).getImage();
	}

	private void initDataAndUpdateUI() {
	
		list.setModel(new NoteListModel(getNotesData()));
		
		if (getNoteListModel().isEmpty()) {
			
			deleteButton.setEnabled(false);
		}
		
		list.setSelectedIndex(0);
		displayMessage("Welcome to Notes app!", MessageType.INFO);
	}
	
	private List<Note> getNotesData() {
	
		List<Note> notes = null;
		
		try {
			notes = noteDAO.getAll();
		}
		catch (AppPersistenceException ex) {
				
			doExceptionRoutine(ActionType.LOAD);
		}
		
		return notes;
	}
	
	private void doExceptionRoutine(ActionType action) {
				
		String s = "";
	
		switch(action) {
		
			case CREATE: s = " creating ";
					break;
			case UPDATE: s = " updating ";
					break;
			case DELETE: s = " deleting ";
					break;
			case LOAD: s = " loading ";
					break;
			default: 
				throw new IllegalArgumentException("Invalid action: " + action);
		}

		String msg = "<html><center>" +
					"An database exception has occurred<br>" +
					"while " + s + " the Note(s). Please check<br>" +
					"the log to view the technical details.<br>" + 
					"Exiting the app." + "</center></html>";
	
		JOptionPane.showMessageDialog(frame, new JLabel(msg),
				"Error",
				JOptionPane.PLAIN_MESSAGE);
					
		System.exit(0);
	}
	
	private NoteListModel getNoteListModel() {
	
		return (NoteListModel) list.getModel();
	}

	private JScrollPane getListInScrollPane() {
	
		list = new JList<Note>();
		list.setToolTipText("Notes list: double-click a Note to edit");
		list.setBorder(new EmptyBorder(5, 5, 5, 5));
		list.setFixedCellHeight(26);
		list.setFont(FONT_FOR_WIDGETS);		
		list.addMouseListener(new ListMouseListener());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(new ListSelectListener());
		JScrollPane scroller = new JScrollPane(list);
		scroller.setPreferredSize(new Dimension(250, 350)); // wxh
		scroller.setVerticalScrollBarPolicy(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		return scroller;
	}

	private JTextField getTextField() {
	
		textFld = new JTextField("", 25);
		textFld.setToolTipText(
				"Note name must be unique and 5 to 25 characters (a-z A-Z 0-9 space and an underscore (_))");
		textFld.setFont(FONT_FOR_WIDGETS);
		textFld.setEditable(false);
		textFld.setMargin(new Insets(5, 5, 5, 5)); // top, left, bottom, rt
		return textFld;
	}
	
	private JScrollPane getTextAreaInScrollPane() {
	
		textArea = new JTextArea("");
		textArea.setToolTipText("Note text (optional)");
		textArea.setFont(FONT_FOR_EDITOR);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setMargin(new Insets(5, 5, 5, 5)); // top, left, bottom, rt

		JScrollPane scroller = new JScrollPane(textArea);
		scroller.setPreferredSize(new Dimension(550, 0)); // wxh
		scroller.setVerticalScrollBarPolicy(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
	
		return scroller;
	}

	private JToolBar getToolBarWithButtons() {

		newButton = getButton("New");	
		saveButton = getButton("Save");		
		deleteButton = getButton("Delete");
		cancelButton = getButton("Cancel");
		
		JToolBar toolBar = getToolBarForButtons();
		toolBar.add(newButton);
		toolBar.addSeparator(new Dimension(2, 0));
		toolBar.add(saveButton);
		toolBar.addSeparator(new Dimension(2, 0));
		toolBar.add(deleteButton);
		toolBar.addSeparator(new Dimension(2, 0));		
		toolBar.add(cancelButton);
		toolBar.addSeparator(new Dimension(5, 0));				
		toolBar.add(getButton("Help"));
		
		return toolBar;
	}
	
	private JToolBar getToolBarForButtons() {
	
		JToolBar toolBar = new JToolBar();
		toolBar.setBorderPainted(false);
		toolBar.setFloatable(false);
		return toolBar;
	}
	
	private JButton getButton(String label__) {
	
		JButton button = new JButton();
		button.setBorderPainted(false);
		
		switch (label__) {
			case "New":
				button.setIcon(getIconForButton("New24.gif"));
				button.addActionListener(new NewActionListener());
				break;
			case "Delete":
				button.setIcon(getIconForButton("Delete24.gif"));
				button.addActionListener(new DeleteActionListener());
				break;
			case "Save":
				button.setEnabled(false);
				button.setIcon(getIconForButton("Save24.gif"));
				button.addActionListener(new SaveActionListener());
				break;
			case "Cancel":
				button.setEnabled(false);
				button.setIcon(getIconForButton("Undo24.gif"));
				button.addActionListener(new CancelActionListener());
				break;
			case "Help":				
				button.setIcon(getIconForButton("Help24.gif"));
				button.addActionListener(new HelpActionListener());
				break;
			default:
				throw new IllegalArgumentException("*** Invalid button label ***");
		}
		
		button.setToolTipText(label__);
		return button;
	}
	
	private ImageIcon getIconForButton(String iconName) {

		String urlString = "/toolbarButtonGraphics/general/" + iconName;
		URL url = this.getClass().getResource(urlString);
		return new ImageIcon(url);
	}
	
	private JLabel getMessageLabel() {
	
		messageLabel = new JLabel("");
		messageLabel.setFont(FONT_FOR_WIDGETS);
		return messageLabel;
	}
	
	private void displayMessage(String msg, MessageType type) {
		
		if (type == MessageType.WARN) {
		
			messageLabel.setText("<html><font color=red>" + msg + "</font></html>");
		}
		else if (type == MessageType.INFO) {
		
			messageLabel.setText("<html><font color=blue>" + msg + "</font></html>");
		}
		else {
			messageLabel.setText("");
		}
	}
	
	private GridBagConstraints getConstraintsForList() {
	
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		c.insets = new Insets(12, 12, 11, 11); // top, left, bottom, right
		return c;
	}
	private GridBagConstraints getConstraintsForTextFld() {
	
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(12, 0, 11, 11);
		return c;
	}
	private GridBagConstraints getConstraintsForTextArea() {
	
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 0, 11, 11);
		return c;
	}

	private GridBagConstraints getConstraintsForButtonToolBar() {
	
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(0, 12, 11, 11);
		return c;
	}
	
	private GridBagConstraints getConstraintsForMessageLabel() {
	
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(0, 11, 11, 11);
		c.anchor = GridBagConstraints.WEST;
		return c;
	}
	
	private class ListSelectListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
					
			if (e.getValueIsAdjusting()) {

				return;
			}
			
			if (getNoteListModel().isEmpty()) {
			
				deleteButton.setEnabled(false);
				textFld.setText("");
				textArea.setText("");
			}
			else {
				deleteButton.setEnabled(true);
				Note n = list.getSelectedValue();
				textFld.setText(n.getName());
				textArea.setText(n.getText());
				textArea.setCaretPosition(0);
			}
			
			displayMessage("", MessageType.NONE);
			textFld.setEditable(false);
			textArea.setEditable(false);
			newButton.setEnabled(true);
			saveButton.setEnabled(false);			
			cancelButton.setEnabled(false);
			updateFlag = false;			
		}
	}
		
	private class NewActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			newButton.setEnabled(false);
			saveButton.setEnabled(true);
			deleteButton.setEnabled(false);
			cancelButton.setEnabled(true);
			textFld.setEditable(true);
			textArea.setEditable(true);
			textFld.setText("");
			textArea.setText("");
			displayMessage("Enter new Note", MessageType.INFO);
			textFld.requestFocusInWindow();	
		}
	}
	
	private class SaveActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			String name = textFld.getText();
			String text = textArea.getText();
			
			name = (name == null) ? "" : name.trim();
			
			Note newN = new Note(name, text);
			Note originalN = list.getSelectedValue();
			
			try {
				new NoteValidator().validate(
					originalN, newN, getNoteListModel(), updateFlag);
			}
			catch (InvalidNoteException ex) {
			
				Toolkit.getDefaultToolkit().beep();
				displayMessage(ex.getMessage(), MessageType.WARN);
				textFld.requestFocusInWindow();	
				return;
			}
	
			if (updateFlag) {

				newN.setId(originalN.getId());
	
				try {
					noteDAO.update(newN);
				}
				catch (AppPersistenceException ex) {
				
					doExceptionRoutine(ActionType.UPDATE);
				}
				
				getNoteListModel().update(newN);
			}
			else {				
				try {
					newN = noteDAO.create(newN);
				}
				catch (AppPersistenceException ex) {
				
					doExceptionRoutine(ActionType.CREATE);
				}
				
				getNoteListModel().add(newN);
			}
			
			list.updateUI();

			int ix = getNoteListModel().indexOf(newN);
			list.setSelectedIndex(ix);
			list.ensureIndexIsVisible(ix);
			
			textFld.setEditable(false);
			textArea.setEditable(false);		
			newButton.setEnabled(true);
			saveButton.setEnabled(false);
			deleteButton.setEnabled(true);
			cancelButton.setEnabled(false);
			updateFlag = false;			
			displayMessage("Note is saved", MessageType.INFO);
		}
	}
	
	private class DeleteActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			int ix = list.getSelectedIndex();
			
			if (ix < 0) {
			
				return;
			}
			
			Note n = list.getSelectedValue();
			
			int optionSelected = JOptionPane.showConfirmDialog(frame,
					"Delete the selected Note ?", "Delete",
					JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
									
			if (optionSelected != JOptionPane.YES_OPTION) {
			
				return;
			}
			
			try {
				noteDAO.delete(n.getId());
			}
			catch (AppPersistenceException ex) {
				
				doExceptionRoutine(ActionType.DELETE);
			}

			getNoteListModel().delete(n);
			list.updateUI();
												
			if (getNoteListModel().isEmpty()) {
			
				textFld.setText("");
				textArea.setText("");
				deleteButton.setEnabled(false);
				displayMessage(n + " is deleted. No Notes.", MessageType.INFO);
				return;
			}
			
			if (ix > 0) {
			
				--ix;
			}

			list.setSelectedIndex(ix);						
			displayMessage(n + " is deleted", MessageType.INFO);
						
			if (ix == 0) {
			
				Note nn = list.getSelectedValue();
				textFld.setText(nn.getName());
				textArea.setText(nn.getText());
				textArea.setCaretPosition(0);
			}
		}
	}
	
	private class CancelActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
		
			if (updateFlag) {
			
				displayMessage("Note edit is cancelled", MessageType.INFO);
			}
			else {
				displayMessage("New Note is cancelled", MessageType.INFO);
			}

			if (getNoteListModel().isEmpty()) {
			
				textFld.setText("");
				textArea.setText("");
				deleteButton.setEnabled(false);
			}
			else {
				Note n = list.getSelectedValue();			
				textFld.setText(n.getName());
				textArea.setText(n.getText());
				deleteButton.setEnabled(true);
			}

			textFld.setEditable(false);
			textArea.setEditable(false);
			newButton.setEnabled(true);
			saveButton.setEnabled(false);
			cancelButton.setEnabled(false);
			updateFlag = false;
		}
	}
	
	private class HelpActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			new HelpDialog(frame);
		}
	}
	
	private class ListMouseListener extends MouseAdapter {

		@Override	
		public void mouseClicked(MouseEvent e) {
		
			if ((e.getButton() == MouseEvent.BUTTON1) &&
					(e.getClickCount() == 2)) { // double click on left button
			
				Rectangle r = list.getCellBounds(0,
						list.getLastVisibleIndex());
				
				if ((r == null) || (! r.contains(e.getPoint()))) { 

					// Double clicked on empty space
					list.requestFocusInWindow();				
					return;
				}

				doubleClickActionRoutine();
			}
		}
	}

	private void doubleClickActionRoutine() {

		displayMessage("Note is being edited", MessageType.INFO);
		newButton.setEnabled(false);
		saveButton.setEnabled(true);
		deleteButton.setEnabled(false);
		cancelButton.setEnabled(true);
		updateFlag = true;
		textFld.setEditable(true);
		textArea.setEditable(true);
		textFld.setCaretPosition(0);
		textFld.requestFocusInWindow();
	}
}