package sdmaker.ai.ai.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sdmaker.ai.ai.Services.AIService;

@Controller
public class ChatController {

    private final AIService aiService;

    public ChatController(AIService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/")
    public String home(Model model) {
        return "index"; // Looks for index.html in templates
    }

    @PostMapping(value = "/chat")
    public ResponseEntity<byte[]> chat(@RequestParam String prompt) {
        try {
            // Call the service to get the image bytes
            byte[] imageBytes = aiService.generateImage(prompt);

            // Return the bytes with the correct Content-Type header
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageBytes);

        } catch (RuntimeException e) {
            // If the model is loading or there's an error,
            // return a 503 Service Unavailable or 500 Internal Error
            System.err.println("Controller Error: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(null);
        }
    }
}