package com.seris.bassein.service.serve.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * MyErrorController
 *
 * @author Баярхүү.Лув 2022.04.20 14:09
 */
@Controller
public class MyErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError() {
        return "error";
    }
}
