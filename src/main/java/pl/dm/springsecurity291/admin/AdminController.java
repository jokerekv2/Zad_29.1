package pl.dm.springsecurity291.admin;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.dm.springsecurity291.user.User;
import pl.dm.springsecurity291.user.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String manageUsersPanel(Model model, Authentication authentication) {
        List<User> users = userService.findAllWithout();
        model.addAttribute("users", users);
        model.addAttribute("name", authentication.getName());
        return "adminPanel";
    }

    @GetMapping("admins-panel")
    public String manageAdminsPanel(Model model) {
        List<User> users = userService.findAllWithout();
        model.addAttribute("users", users);
        return "adminPanel";
    }

    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/promote-degrade")
    public String promoteOrDegrade(@PathVariable Long id) {
        userService.promoteOrDegradeById(id);
        return "redirect:/admin";
    }
}
