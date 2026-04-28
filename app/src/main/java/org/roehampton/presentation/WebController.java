package org.roehampton.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebController {

    private final IUserInterface userInterface;


    public WebController(IUserInterface userInterface) {

        this.userInterface = userInterface;
    }


    @GetMapping("/")
    @ResponseBody
    public String loadHome() {

        return userInterface.renderHomePage();

    }

}
