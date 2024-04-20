package dev.practice.circuitbreaker.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class Resilience4jCircuitBreakerConfig {

    @Bean
    public RegistryEventConsumer<CircuitBreaker> myRegistryEventConsumer() {

        // https://resilience4j.readme.io/docs/circuitbreaker#consume-emitted-circuitbreakerevents

        return new RegistryEventConsumer<>() {
            @Override
            public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) {
                log.info("RegistryEventConsumer::onEntryAddedEvent");
                CircuitBreaker.EventPublisher eventPublisher = entryAddedEvent.getAddedEntry().getEventPublisher();

                eventPublisher.onEvent( // 서킷 브레이커의 모든 이벤트에 대해 수행할 consumer 등록
                        event -> log.info("onEvent : {}", event)
                );

                eventPublisher.onSuccess( // 서킷 브레이커로 수행한 요청이 성공했을 때 수행할 consumer 등록
                        event -> log.info("onSuccess : {}", event)
                );

                eventPublisher.onCallNotPermitted( // open 상태의 서킷 브레이커로 수행하여 요청이 차단되었을 때 수행할 consumer 등록
                        event -> log.info("onCallNotPermitted : {}", event)
                );

                eventPublisher.onError( // record exception 이 발생했을 때, 수행할 consumer 등록
                        event -> log.info("onError : {}", event)
                );

                eventPublisher.onIgnoredError( // ignore exception 이 발생했을 때, 수행할 consumer 등록
                        event -> log.info("onIgnoredError : {}", event)
                );

                eventPublisher.onStateTransition( // 서킷 브레이커의 상태가 변경될 때 수행할 consumer 등록
                        event -> log.info("onStateTransition : {}", event)
                );



                eventPublisher.onFailureRateExceeded( // 서킷 브레이커가 실패(record exception)율을 초과하면 수행할 consumer 등록
                        event -> log.info("onFailureRateExceeded : {}", event)
                );

                eventPublisher.onSlowCallRateExceeded( // 서킷 브레이커가 실패(timeout)율을 초과하면 수행할 consumer 등록
                        event -> log.info("onSlowCallRateExceeded : {}", event)
                );

            }

            @Override
            public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {
                log.info("RegistryEventConsumer::onEntryRemovedEvent");
            }

            @Override
            public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {
                log.info("RegistryEventConsumer::onEntryReplacedEvent");
            }
        };
    }
}
