package pl.dm.springsecurity291.secure;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecureController {

    @GetMapping("/login-form")
    private String initialForm() {
        return "loginForm";
    }

}
