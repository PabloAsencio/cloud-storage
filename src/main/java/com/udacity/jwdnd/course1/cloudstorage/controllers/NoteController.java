package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NoteController {

    private UserMapper userMapper;
    private NoteMapper noteMapper;

    public NoteController(UserMapper userMapper, NoteMapper noteMapper) {
        this.userMapper = userMapper;
        this.noteMapper = noteMapper;
    }

    @PostMapping("/submit-note")
    public String handleNoteSubmission(@ModelAttribute("newNote") Note newNote, Authentication authentication, Model model) {
        User user = getUser(authentication);

        if (null == newNote.getNoteid()) {
            if (!userHasNoteWithSameTitle(newNote, user)) {
                newNote.setUserid(user.getUserid());
                noteMapper.insertNote(newNote);
            } else {
                setErrorMessage(model, "You already have a note with the title \"" + newNote.getNotetitle() + "\"");
            }
        } else {
            // TODO: Check that the user owns the note
            noteMapper.updateNote(newNote);
        }

        return "result";
    }

    @PostMapping("/delete-note/{id}")
    public String handleNoteDeletion(@PathVariable String id, Authentication authentication, Model model) {
        Integer noteId = Integer.parseInt(id);
        User user = getUser(authentication);
        Note note = noteMapper.getNoteById(noteId);
        if (userOwnsNote(note, user)) {
            noteMapper.deleteNote(noteId);
        } else {
            setErrorMessage(model, "You do not have permission to delete this note");
        }

        return "result";
    }

    private boolean userOwnsNote(Note note, User user) {
        return null != note && note.getUserid() == user.getUserid();
    }

    private boolean userHasNoteWithSameTitle(Note newNote, User user) {
        String noteTitle = newNote.getNotetitle();
        Integer userId = user.getUserid();
        return !(null == noteMapper.getNoteByName(noteTitle, userId));
    }

    private User getUser(Authentication authentication) {
        String username = authentication.getName();
        return userMapper.getUser(username);
    }

    private void setErrorMessage(Model model, String errorMessage) {
        model.addAttribute("error", true);
        model.addAttribute("errorMessage", errorMessage);
    }
}
