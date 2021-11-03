package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.UploadedFile;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private UserMapper userMapper;
    private FileMapper fileMapper;
    private NoteMapper noteMapper;

    public HomeController(UserMapper userMapper, FileMapper fileMapper, NoteMapper noteMapper) {
        this.userMapper = userMapper;
        this.fileMapper = fileMapper;
        this.noteMapper = noteMapper;
    }

    @GetMapping
    public String homeView(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userMapper.getUser(username);
        if (null != user) {
            List<UploadedFile> files = fileMapper.getUserFiles(user.getUserid());
            files = null == files ? new ArrayList<UploadedFile>() : files;
            List<Note> notes = noteMapper.getUserNotes(user.getUserid());
            notes = null == notes ? new ArrayList<Note>() : notes;
            model.addAttribute("files", files);
            model.addAttribute("notes", notes);
        }
        return "home";
    }
}
