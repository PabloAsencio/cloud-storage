package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CredentialController {

    UserService userService;
    CredentialService credentialService;

    public CredentialController(UserService userService, CredentialService credentialService) {
        this.userService = userService;
        this.credentialService = credentialService;
    }

    @PostMapping("/submit-credential")
    public String handleCredentialSubmission(@ModelAttribute("newCredential") Credential newCredential, Authentication authentication) {
        User user = getUser(authentication);
        newCredential.setUserid(user.getUserid());
        credentialService.createCredential(newCredential);

        return "result";
    }

    private User getUser(Authentication authentication) {
        String username = authentication.getName();
        return userService.getUser(username);
    }
}
