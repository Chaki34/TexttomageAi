package sdmaker.ai.ai.Configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfig {

    @Value("${pollinations.connect-timeout}")
    private int connectTimeout;

    @Value("${pollinations.read-timeout}")
    private int readTimeout;

    @Bean
    public RestClient restClient() {

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        // Values are in seconds in application.properties
        factory.setConnectTimeout(connectTimeout * 1000);
        factory.setReadTimeout(readTimeout * 1000);

        return RestClient.builder()
                .requestFactory(factory)
                .build();
    }
}