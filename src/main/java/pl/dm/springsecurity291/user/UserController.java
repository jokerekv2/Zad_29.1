package pl.dm.springsecurity291.user;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    private String login(Model model, Authentication authentication) {
        if (userService.hasRole("ROLE_ADMIN")) {
            return "redirect:/admin";
        } else {
            model.addAttribute("name", authentication.getName());
            return "hello";
        }
    }

    @GetMapping("/register")
    public String registrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registrationForm";
    }

    @PostMapping("/register")
    public String registerProcessing(User user) {
        String username = user.getUsername();
        String rawPassword = user.getPassword();
        userService.registerUser(username, rawPassword);

        return "successRegister";
    }

    @GetMapping("/data-change")
    public String dataChange() {
        return "dataChangeForm";
    }

    @PostMapping("/data-change")
    public String dataChange(@RequestParam String username,
                             @RequestParam String password) {

        userService.updateUserData(username, password);
        return "redirect:/logout";
    }
}
