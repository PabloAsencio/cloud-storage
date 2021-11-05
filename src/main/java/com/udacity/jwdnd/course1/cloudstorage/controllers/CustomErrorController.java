package com.udacity.jwdnd.course1.cloudstorage.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * Based on the example in https://www.baeldung.com/spring-boot-custom-error-page
 * */
@Controller
public class CustomErrorController implements ErrorController {
    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object errorStatusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (null != errorStatusCode) {
            HttpStatus status = HttpStatus.resolve(Integer.valueOf(errorStatusCode.toString()));
            model.addAttribute("hasStatusCode", true);
            model.addAttribute("statusCode", status.value());
            model.addAttribute("statusPhrase", status.getReasonPhrase());
        }

        return "error";
    }
}
