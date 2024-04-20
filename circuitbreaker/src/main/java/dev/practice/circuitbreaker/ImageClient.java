package dev.practice.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

@Slf4j
@Component
public class ImageClient {

    private static final String MY_CIRCUIT_BREAKER_CONFIG = "myCircuitBreakerConfig";


    @SneakyThrows
    // @Retry 와 annotation 사용법이 동일하다.
    @CircuitBreaker(name = MY_CIRCUIT_BREAKER_CONFIG, fallbackMethod = "fallback")
    public String requestThumbnailImageUrlByArticleId(String articleId) {

        if(articleId.equals("1")) {
            throw new ResourceAccessException("[Record Exception] image server network error occur.. articleId = " + articleId);
        } else if (articleId.equals("2")) {
            throw new ArithmeticException("[Ignore Exception] ..... articleId = " + articleId);
        } else if (articleId.equals("3")) {
            Thread.sleep(4000); // 3000ms 가 slow call 임계값으로 설정되어있음
        }

        return "https://real/thumbnail/image/url/" + articleId;
    }

    /**
     * fallback 메서드가 실행되는 경우
     * 1. record exception 이 발생
     * 2. ignore exception 이 발생
     * 3. 서킷 브레이커가 open 상태
     *
     * 1, 2, 3 모두 아우르는 하나의 fallback 메서드만 두어도 되지만
     * 3 개의 fallback 메서드를 오버로딩을 통해 각각 둘 수 도 있다.
     *
     * 참고
     * 1.
     * Open 상태에서 요청을 수행하면 CallNotPermittedException 예외가 발생한다.
     * CallNotPermittedException 을 처리할 수 있는 fallback 메서드를 꼭 만들어야한다.
     *
     * 2.
     * slow call 과는 상관이 없다는게 특징, slow call 임계값을 넘겨도 연결을 끊고 fallback 을 수행하는게 아니다.
     */
//    public String fallback(String articleId, Exception ex) {
//
//        log.info("fallback execute.. articleId = {}, exception = {}", articleId, ex.getMessage());
//
//        return "https://default/thumbnail/image/url";
//    }

    public String fallback(String articleId, ResourceAccessException ex) { // record exception 이 발생하면 수행할 fallback 메서드

        log.info("fallback execute.. articleId = {}, record exception = {}", articleId, ex.getMessage());

        return "https://default/thumbnail/image/url";
    }

    public String fallback(String articleId, ArithmeticException ex) { // ignore exception 이 발생하면 수행할 fallback 메서드

        log.info("fallback execute.. articleId = {}, ignore exception = {}", articleId, ex.getMessage());

        return "https://default/thumbnail/image/url";
    }

    public String fallback(String articleId, CallNotPermittedException ex) { // open 상태에서 던져지는 예외이다. open 상태일때 수행할 fallback 메서드

        log.info("fallback execute.. articleId = {}, open state exception = {}", articleId, ex.getMessage());

        return "https://default/thumbnail/image/url";
    }
}
