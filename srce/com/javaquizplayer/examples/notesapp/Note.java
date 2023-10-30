package com.javaquizplayer.examples.notesapp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;


@Entity
@Table	
public class Note implements Comparable<Note> {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String name;
	private String text;

	public Note() {
	}
	
	public Note(String n, String t) {
	
		name = n;
		text = t;
	}
	
	public void setId(int i) {
	
		id = i;
	}
	public int getId() {
	
		return id;
	}
	
	public void setName(String n) {
	
		name = n;
	}
	public String getName() {
	
		return name;
	}
	
	public void setText(String t) {
	
		text = t;
	}
	public String getText() {
	
		return text;
	}

	@Override
	public String toString() {
	
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
	
		if ((obj instanceof Note) &&
				(((Note) obj).getId() == id)) {
		
			return true;
		}
		
		return false;
	}
	
	@Override
	public int compareTo(Note other) {
	
		return this.name.compareTo(other.getName());
	}
}