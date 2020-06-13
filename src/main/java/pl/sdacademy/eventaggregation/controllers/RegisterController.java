package pl.sdacademy.eventaggregation.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.sdacademy.eventaggregation.domain.User;
import pl.sdacademy.eventaggregation.services.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/register")
public class RegisterController {
    private static final String MODEL_ATTR_USER = "user";
    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showUserPage(final ModelMap modelMap) {
        modelMap.addAttribute(MODEL_ATTR_USER, new User());

        return "register";
    }


    @PostMapping
    public String handleSaveUser(@Valid @ModelAttribute(name = MODEL_ATTR_USER) final User user, final BindingResult bindingResult,
                                 final ModelMap modelMap) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        else{
            modelMap.addAttribute("firstname", user.getFirstName());
            modelMap.addAttribute("lastname", user.getLastName());
            modelMap.addAttribute("username", user.getUsername());
            modelMap.addAttribute("email", user.getEmail());
            modelMap.addAttribute("password", user.getPassword());
            userService.createUser(user);
            return "user_page";
        }
    }
}