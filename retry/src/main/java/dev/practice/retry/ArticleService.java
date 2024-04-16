package dev.practice.retry;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

@Slf4j
@Service
public class ArticleService {

    private static final String MY_RETRY_CONFIG = "myRetryConfig"; // application.yml 에서 사용 된다.

    // resilience4j @Retry
    @Retry(name = MY_RETRY_CONFIG, fallbackMethod = "fallback")
    public Article getArticle(String articleId) {

        return imageRequest(articleId);
    }


    private Article imageRequest(String articleId) {
        // articleId 로 article thumbnail image 를 다른 서버에 요청

        // rest client 를 사용하여 발생하는 network exception 을 따로 처리하지 않으면,
        // 아래의 코드처럼 자연스럽게 던져지는 꼴이 될 것이다.
        // 비즈니스 에러 (400, 500) 는 retry 할 필요가 대부분 없을 것이다.
        throw new ResourceAccessException("image server network error occur.. articleId = " + articleId);
    }


    private Article fallback(String articleId) {

        return new Article(
                articleId,
                "title",
                "https://default/thumbnail/image/url"
        );
    }
}
