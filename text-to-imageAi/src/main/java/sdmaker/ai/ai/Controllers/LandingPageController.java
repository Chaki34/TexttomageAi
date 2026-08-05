package sdmaker.ai.ai.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sdmaker.ai.ai.Entites.User;
import sdmaker.ai.ai.Services.UserService;

import java.security.Principal;

@Controller
public class LandingPageController {

    @Autowired
    private UserService userService;

    // 1. PUBLIC LANDING PAGE
    @GetMapping("/")
    public String landingPage() {
        return "landing"; // looks for landing.html
    }

    @GetMapping("/studio")
    public String dashboard(Model model, Principal principal) {
        if (principal != null) {
            // Refresh tokens if it's a new day and get current balance
            User user = userService.refreshAndGetTokens(principal.getName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("tokens", user.getTokens());
        }
        return "index";
    }
}
