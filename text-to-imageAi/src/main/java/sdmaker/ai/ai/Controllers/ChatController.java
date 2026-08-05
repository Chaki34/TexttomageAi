package sdmaker.ai.ai.Controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sdmaker.ai.ai.Services.AIService;

@Controller
public class ChatController {

    private final AIService aiService;

    public ChatController(AIService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/chat")
    public ResponseEntity<byte[]> chat(@RequestParam String prompt) {

        try {

            System.out.println("Prompt Received: " + prompt);

            byte[] imageBytes = aiService.generateImage(prompt);

            System.out.println("Image Generated Successfully.");

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageBytes);

        } catch (Exception e) {

            System.err.println("========== CONTROLLER ERROR ==========");
            e.printStackTrace();
            System.err.println("======================================");

            return ResponseEntity.internalServerError().build();
        }
    }
}