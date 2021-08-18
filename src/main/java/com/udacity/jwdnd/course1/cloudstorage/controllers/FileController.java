package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.UploadedFile;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Controller
public class FileController {
    private UserMapper userMapper;
    private FileMapper fileMapper;

    public FileController(UserMapper userMapper, FileMapper fileMapper) {
        this.userMapper = userMapper;
        this.fileMapper = fileMapper;
    }

    @PostMapping("/file-upload")
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication authentication, Model model) {
        try (InputStream fileInputStream = fileUpload.getInputStream()) {
            UploadedFile file = new UploadedFile();
            if (null == fileMapper.getFileByName(fileUpload.getOriginalFilename())) {
                file.setFilename(fileUpload.getOriginalFilename());
                file.setContenttype(fileUpload.getContentType());
                file.setFilesize(String.valueOf(fileUpload.getSize()));
                String username = authentication.getName();
                User user = userMapper.getUser(username);
                file.setUserid(user.getUserid());
                file.setFiledata(fileUpload.getBytes());
                if (file.getFiledata().length > 0) {
                    fileMapper.insert(file);
                } else {
                    model.addAttribute("error", true);
                }
            } else {
                model.addAttribute("error", true);
            }
        } catch (IOException exception) {
            model.addAttribute("error", true);
        }
        return "result";
    }

    // This should be a DeleteMapping or at least a PostMapping since this method call is not idempotent.
    // We use a GetMapping because the provided template implements the buttons as
    // anchor tags instead of using forms. Anchor tags do not allow for using any other method than get.
    @GetMapping("/delete-file/{id}")
    public String deleteFile(@PathVariable String id, Authentication authentication, Model model) {
        Integer fileId = Integer.parseInt(id);
        String username = authentication.getName();
        User user = userMapper.getUser(username);
        UploadedFile file = fileMapper.getFileById(fileId);
        if (file.getUserid() == user.getUserid()) {
            fileMapper.delete(fileId);
        } else {
            model.addAttribute("error", true);
        }

        return "result";
    }

    @GetMapping("/download-file/{id}")
    public void downloadFile(@PathVariable String id, HttpServletResponse response, Authentication authentication) {
        Integer fileId = Integer.parseInt(id);
        String username = authentication.getName();
        User user = userMapper.getUser(username);
        UploadedFile file = fileMapper.getFileById(fileId);
        if (file.getUserid() == user.getUserid()) {
            response.setContentType(file.getContenttype());
            try (OutputStream out = response.getOutputStream()) {
                out.write(file.getFiledata());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    ;


}
