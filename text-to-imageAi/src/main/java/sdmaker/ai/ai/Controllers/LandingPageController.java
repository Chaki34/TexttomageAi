package sdmaker.ai.ai.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class LandingPageController {

    // 1. PUBLIC LANDING PAGE
    @GetMapping("/")
    public String landingPage() {
        return "landing"; // looks for landing.html
    }

    @GetMapping("/studio")
    public String dashboard(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        return "index"; // Your existing generation UI
    }

}
