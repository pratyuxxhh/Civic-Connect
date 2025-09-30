package com.example.civic_issues.controller.contentController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui")
public class GetContent {
    @GetMapping("/issue")
    public String getDetails(){
        return "issues";
    }

}
