package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.UploadedFile;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/file-upload")
public class FileUploadController {
    private UserMapper userMapper;
    private FileMapper fileMapper;

    public FileUploadController(UserMapper userMapper, FileMapper fileMapper) {
        this.userMapper = userMapper;
        this.fileMapper = fileMapper;
    }

    @PostMapping
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication authentication, Model model) {
        try (InputStream fileInputStream = fileUpload.getInputStream()) {
            UploadedFile file = new UploadedFile();
            file.setFilename(fileUpload.getName());
            file.setContenttype(fileUpload.getContentType());
            file.setFilesize(String.valueOf(fileUpload.getSize())); // TODO: Format it nicely
            String username = authentication.getName();
            User user = userMapper.getUser(username);
            file.setUserid(user.getUserid());
            file.setFiledata(fileUpload.getBytes());
            fileMapper.insert(file);
        } catch (IOException exception) {
            model.addAttribute("error", true);
        }
        return "result";
    }
}
