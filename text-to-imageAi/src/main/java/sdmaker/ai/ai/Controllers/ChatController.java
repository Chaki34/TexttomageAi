package sdmaker.ai.ai.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Add this
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sdmaker.ai.ai.Entites.User;
import sdmaker.ai.ai.Repos.UserRepository;
import sdmaker.ai.ai.Services.AIService;

import java.security.Principal; // Add this

@Controller
public class ChatController {

    private final AIService aiService;


    private final UserRepository userRepository;

    public ChatController(AIService aiService, UserRepository userRepository) {
        this.aiService = aiService;
        this.userRepository = userRepository;
    }



    @PostMapping("/chat")
    public ResponseEntity<byte[]> chat(@RequestParam String prompt, Principal principal) {
        try {
            User user = userRepository.findByUsername(principal.getName()).get();

            if (user.getTokens() <= 0) {
                return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).build();
            }

            byte[] imageBytes = aiService.generateImage(prompt);

            // Deduct token
            user.setTokens(user.getTokens() - 20);
            userRepository.save(user);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .header("X-Tokens-Remaining", String.valueOf(user.getTokens())) // Send new count to UI
                    .body(imageBytes);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}