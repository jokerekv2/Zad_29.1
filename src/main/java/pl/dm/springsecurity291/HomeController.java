package pl.dm.springsecurity291;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.dm.springsecurity291.user.User;

@Controller
public class HomeController {

    @GetMapping("/")
    private String homepage() {
        return "welcomePage";
    }

}
