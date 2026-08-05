package sdmaker.ai.ai.Controllers;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sdmaker.ai.ai.Services.UserService;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/register")
    public String register() { return "register"; }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password) {
        userService.registerUser(username, email, password);
        return "redirect:/login?success";
    }
}
