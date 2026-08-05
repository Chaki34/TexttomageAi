package sdmaker.ai.ai.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class AIService {

    private final RestClient restClient;

    @Value("${pollinations.base-url}")
    private String baseUrl;

    @Value("${pollinations.model}")
    private String model;

    @Value("${pollinations.width}")
    private int width;

    @Value("${pollinations.height}")
    private int height;

    @Value("${pollinations.seed}")
    private int seed;

    public AIService(RestClient restClient) {
        this.restClient = restClient;
    }

    public byte[] generateImage(String prompt) {

        try {

            // Encode the prompt for a valid URL
            String encodedPrompt = URLEncoder.encode(prompt, StandardCharsets.UTF_8);

            // Build the Pollinations URL
            String imageUrl = String.format(
                    "%s%s?model=%s&width=%d&height=%d&seed=%d",
                    baseUrl,
                    encodedPrompt,
                    model,
                    width,
                    height,
                    seed
            );

            System.out.println("Generating image from:");
            System.out.println(imageUrl);

            return restClient.get()
                    .uri(imageUrl)
                    .retrieve()
                    .body(byte[].class);

        } catch (Exception e) {

            System.err.println("========== POLLINATIONS ERROR ==========");
            e.printStackTrace();
            System.err.println("========================================");

            throw new RuntimeException("Failed to generate image: " + e.getMessage());
        }
    }
}