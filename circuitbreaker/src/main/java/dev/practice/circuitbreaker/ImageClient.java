package dev.practice.circuitbreaker;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

@Slf4j
@Component
public class ImageClient {

    private static final String MY_CIRCUIT_BREAKER_CONFIG = "myCircuitBreakerConfig";


    // @Retry 와 annotation 사용법이 동일하다.
    @CircuitBreaker(name = MY_CIRCUIT_BREAKER_CONFIG, fallbackMethod = "fallback")
    public String requestThumbnailImageUrlByArticleId(String articleId) {

        throw new ResourceAccessException("image server network error occur.. articleId = " + articleId);
    }

    public String fallback(String articleId, Exception ex) {

        log.info("fallback execute.. articleId = {}, exception = {}", articleId, ex.getMessage());

        return "https://default/thumbnail/image/url";
    }
}
