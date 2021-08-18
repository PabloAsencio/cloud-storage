package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.UploadedFile;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private UserMapper userMapper;
    private FileMapper fileMapper;

    public HomeController(UserMapper userMapper, FileMapper fileMapper) {
        this.userMapper = userMapper;
        this.fileMapper = fileMapper;
    }

    @GetMapping
    public String homeView(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userMapper.getUser(username);
        if (null != user) {
            List<UploadedFile> files = fileMapper.getUserFiles(user.getUserid());
            model.addAttribute("files", files);
        }
        return "home";
    }
}
