package pl.sdacademy.eventaggregation.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(path = {"/", "/home"})
public class IndexController {

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public String getHomePage() {
        return "index";
    }
}
