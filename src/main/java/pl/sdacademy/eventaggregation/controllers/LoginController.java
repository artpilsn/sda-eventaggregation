package pl.sdacademy.eventaggregation.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.sdacademy.eventaggregation.domain.User;
import pl.sdacademy.eventaggregation.domain.UserLogin;
import pl.sdacademy.eventaggregation.services.UserLoginService;
import pl.sdacademy.eventaggregation.services.UserService;

@Controller
@RequestMapping(value = "/login-user")
public class LoginController {
    private final UserService userService;
    private static final String MODEL_ATTR_USER = "user";
    private final UserLoginService userLoginService;

    public LoginController(UserService userService, UserLoginService userLoginService) {
        this.userService = userService;
        this.userLoginService = userLoginService;
    }

    @GetMapping
    public String showLoginPage(final ModelMap modelMap) {
        modelMap.addAttribute(MODEL_ATTR_USER, new User());
        return "login-user";
    }

    @PostMapping
    public String handleLoginUser(@ModelAttribute(name = MODEL_ATTR_USER) final UserLogin user,
                                  final ModelMap modelMap) {

        modelMap.addAttribute("username", user.getUsername());
        modelMap.addAttribute("password", user.getPassword());

        if(userLoginService.checkIfPasswordMatches(userService.getByUsername(user.getUsername()),user)){
            return "index";
        }
        else{
            return "login-user";
        }
    }
}
