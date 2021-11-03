package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.*;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;

@Controller
public class CredentialController {

    UserService userService;
    CredentialService credentialService;

    public CredentialController(UserService userService, CredentialService credentialService) {
        this.userService = userService;
        this.credentialService = credentialService;
    }

    @PostMapping("/submit-credential")
    public String handleCredentialSubmission(@ModelAttribute("newCredential") Credential newCredential, Authentication authentication, Model model) {
        User user = getUser(authentication);

        if (null == newCredential.getCredentialid()) {
            newCredential.setUserid(user.getUserid());
            credentialService.createCredential(newCredential);
        } else {
            if (userOwnsCredential(user, newCredential.getCredentialid())) {
                credentialService.updateCredential(newCredential);
            } else {
                setErrorMessage(model, "You do not have permission to change this credential");
            }
        }


        return "result";
    }

    @GetMapping(value = "/retrieve-password/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> retrievePassword(@PathVariable String id, Authentication authentication) {
        User user = getUser(authentication);
        Integer credentialid = Integer.parseInt(id);
        Credential credential = credentialService.getCredentialById(credentialid);
        if (credential.getUserid() == user.getUserid()) {
            String decryptedPassword = credentialService.getDecryptedPassword(credential);
            String password = "{ \"password\": \"" + decryptedPassword + "\"}";
            return new ResponseEntity<String>(password, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("{\"error\": \"Unauthorized access\"}", HttpStatus.FORBIDDEN);
        }

    }

    @PostMapping("/delete-credential/{id}")
    public String handleCredentialDeletion(@PathVariable String id, Authentication authentication, Model model) {
        Integer credentialid = Integer.parseInt(id);
        User user = getUser(authentication);
        if (userOwnsCredential(user, credentialid)) {
            credentialService.deleteCredential(credentialid);
        } else {
            setErrorMessage(model, "You do not have permission to delete this credential");
        }

        return "result";
    }

    private boolean userOwnsCredential(User user, Integer credentialid) {
        Credential storedCredential = credentialService.getCredentialById(credentialid);
        return storedCredential != null && storedCredential.getUserid() == user.getUserid();
    }

    private User getUser(Authentication authentication) {
        String username = authentication.getName();
        return userService.getUser(username);
    }

    private void setErrorMessage(Model model, String errorMessage) {
        model.addAttribute("error", true);
        model.addAttribute("errorMessage", errorMessage);
    }
}
