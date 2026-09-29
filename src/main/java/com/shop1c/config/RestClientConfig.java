package com.shop1c.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient oneCRestClient() {
        return RestClient.builder()
                .baseUrl("http://192.168.31.41/1CTradeEntrUkr/hs/java-shop")
                .defaultHeaders(headers ->
                        headers.setBasicAuth("JavaIntegration", "1"))
                .build();
    }
}
