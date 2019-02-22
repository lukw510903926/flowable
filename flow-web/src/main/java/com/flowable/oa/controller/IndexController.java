package com.flowable.oa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/office")
public class IndexController {

    @RequestMapping("/index")
    public String index(Model model) {

        return "index";
    }


}
