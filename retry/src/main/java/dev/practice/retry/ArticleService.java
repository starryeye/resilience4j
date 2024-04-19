package dev.practice.retry;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ImageClient imageClient;

    public Article getArticle(String articleId) {

        String thumbnailImageUrl = imageClient.requestThumbnailImageUrlByArticleId(articleId);

        return new Article(
                articleId,
                "title",
                thumbnailImageUrl
        );
    }
}
