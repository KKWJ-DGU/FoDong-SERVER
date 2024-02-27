package Fodong.serverdong.global.auth.controller;

import Fodong.serverdong.global.auth.oauth.AppleSocialLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequiredArgsConstructor
public class appleTestController {
    private final AppleSocialLogin appleSocialLogin;

    @RequestMapping(value="/apple/home", method= RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("appleUrl", appleSocialLogin.getAppleLogin());

        return "index";
    }
}
