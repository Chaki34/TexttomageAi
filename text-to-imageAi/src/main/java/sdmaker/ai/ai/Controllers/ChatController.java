package sdmaker.ai.ai.Controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Add this
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sdmaker.ai.ai.Services.AIService;

import java.security.Principal; // Add this

@Controller
public class ChatController {

    private final AIService aiService;

    public ChatController(AIService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        // This gets the username of the person logged in
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        } else {
            model.addAttribute("username", "Guest");
        }
        return "index";
    }

    @PostMapping("/chat")
    public ResponseEntity<byte[]> chat(@RequestParam String prompt) {
        try {
            byte[] imageBytes = aiService.generateImage(prompt);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageBytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}