package sdmaker.ai.ai.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.Map;

@Service
public class AIService {

    private final RestClient restClient;

    @Value("${huggingface.api.key}")
    private String apiKey;

    @Value("${huggingface.model}")
    private String model;

    @Value("${huggingface.base-url}")
    private String baseUrl;

    public AIService(RestClient restClient) {
        this.restClient = restClient;
    }

    public byte[] generateImage(String prompt) {
        // Clean URL construction
        String endpoint = baseUrl.endsWith("/") ? baseUrl + model : baseUrl + "/" + model;

        try {
            return restClient.post()
                    .uri(endpoint)
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    // Using a Map ensures the JSON is formatted perfectly
                    .body(Map.of("inputs", prompt))
                    .retrieve()
                    .onStatus(status -> status.value() == 503, (request, response) -> {
                        throw new RuntimeException("Model is loading. Try again in 20 seconds.");
                    })
                    .body(byte[].class);
        } catch (Exception e) {
            // This will print the exact error to your IntelliJ console
            System.err.println("CRITICAL ERROR: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("AI failed: " + e.getMessage());
        }
    }
}