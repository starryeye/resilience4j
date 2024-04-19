package dev.practice.retry;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

@Slf4j
@Component
public class ImageClient {

    private static final String MY_RETRY_CONFIG = "myRetryConfig"; // application.yml 에서 사용 된다.

    // resilience4j @Retry
    @Retry(name = MY_RETRY_CONFIG, fallbackMethod = "fallback")
    public String requestThumbnailImageUrlByArticleId(String articleId) {
        // articleId 로 article thumbnail image 를 다른 서버에 요청

        // rest client 를 사용하여 발생하는 network exception 을 따로 처리하지 않으면,
        //      아래의 코드처럼 자연스럽게 던져지는 꼴이 될 것이다. 이 경우 retry 를 하도록 하자
        // restTemplate 의 경우 http status 값(4xx, 5xx)에 따른 exception 이 발생한다.
        //      http status 값에 따른 exception 은 retry 를 해봤자 의미가 없는 경우가 대부분 이므로
        //      config 에서 ignore-exceptions 처리를 해두던가, 설정을 해두지 않으면, retry 를 수행하지 않고 fallback 이 바로 수행된다.
        //      혹은, requestThumbnailImageUrlByArticleId 범위 aop 로 exception 이 던져지기전에 예외를 잡으면 retry, fallback 을 수행하지 않게도 할 수 있다.
        throw new ResourceAccessException("image server network error occur.. articleId = " + articleId);
    }

    private String fallback(String articleId, Exception ex) {

        log.info("fallback execute.. articleId = {}, exception = {}", articleId, ex.getMessage());

        return "https://default/thumbnail/image/url";
    }
}
