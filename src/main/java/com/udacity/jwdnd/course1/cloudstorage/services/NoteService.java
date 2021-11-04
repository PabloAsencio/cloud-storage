package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public Note getNoteByName(String noteTitle, Integer userId) {
        return noteMapper.getNoteByName(noteTitle, userId);
    }

    public Note getNoteById(Integer id) {
        return noteMapper.getNoteById(id);
    }

    public void insertNote(Note note) {
        noteMapper.insertNote(note);
    }

    public void updateNote(Note note) {
        noteMapper.updateNote(note);
    }

    public void deleteNote(Integer id) {
        noteMapper.deleteNote(id);
    }
}
