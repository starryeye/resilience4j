package dev.practice.retry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
