package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.model.UploadedFile;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
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
import java.io.OutputStream;

@Controller
public class FileController {

    private UserService userService;
    private FileService fileService;

    public FileController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @PostMapping("/upload-file")
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication authentication, Model model) {
        User user = getUser(authentication);
        if (!userHasFileWithSameName(fileUpload, user)) {
            try {
                UploadedFile file = createFile(fileUpload, user);
                saveFile(file, model);
            } catch (IOException exception) {
                setErrorMessage(model, "An error occurred while uploading your file.");
            }
        } else {
            setErrorMessage(model, "You already have a file named " + fileUpload.getOriginalFilename() + ". Please choose a different name.");
        }

        return "result";
    }


    @PostMapping("/delete-file/{id}")
    public String handleFileDeletion(@PathVariable String id, Authentication authentication, Model model) {
        Integer fileId = Integer.parseInt(id);
        User user = getUser(authentication);
        UploadedFile file = fileService.getFileById(fileId);
        if (userOwnsFile(file, user)) {
            fileService.deleteFile(fileId);
        } else {
            setErrorMessage(model, "You do not have permission to delete this file");
        }

        return "result";
    }

    @GetMapping("/download-file/{id}")
    public void handleFileDownload(@PathVariable String id, HttpServletResponse response, Authentication authentication) {
        Integer fileId = Integer.parseInt(id);
        User user = getUser(authentication);
        UploadedFile file = fileService.getFileById(fileId);
        if (userOwnsFile(file, user)) {
            sendFile(response, file);
        } else {
            sendError(response, "You do not have permission to view this file.");
        }
    }

    private User getUser(Authentication authentication) {
        String username = authentication.getName();
        return userService.getUser(username);
    }

    private boolean userHasFileWithSameName(MultipartFile file, User user) {
        final String originalFilename = file.getOriginalFilename();
        final Integer userid = user.getUserid();
        return null != fileService.getFileByName(originalFilename, userid);
    }

    private boolean userOwnsFile(UploadedFile file, User user) {
        return null != file && file.getUserid() == user.getUserid();
    }

    private void saveFile(UploadedFile file, Model model) {
        if (file.getFiledata().length > 0) {
            fileService.createFile(file);
        } else {
            setErrorMessage(model, "No file was selected. Please select a file to upload.");
        }
    }

    private UploadedFile createFile(MultipartFile fileUpload, User user) throws IOException {
        UploadedFile file = new UploadedFile();
        file.setFilename(fileUpload.getOriginalFilename());
        file.setContenttype(fileUpload.getContentType());
        file.setFilesize(String.valueOf(fileUpload.getSize()));
        file.setUserid(user.getUserid());
        file.setFiledata(fileUpload.getBytes());
        return file;
    }

    private void sendFile(HttpServletResponse response, UploadedFile file) {
        response.setContentType(file.getContenttype());
        try (OutputStream out = response.getOutputStream()) {
            out.write(file.getFiledata());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setErrorMessage(Model model, String errorMessage) {
        model.addAttribute("error", true);
        model.addAttribute("errorMessage", errorMessage);
    }

    private void sendError(HttpServletResponse response, String errorMessage) {
        try {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, errorMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
