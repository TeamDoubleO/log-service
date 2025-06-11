package com.doubleo.logservice.domain.log.consumer;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RetainedCountConsumer {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String STREAM_KEY = "building:enter:stream";
    private static final String GROUP = "retained-counter-group";
    private static final String CONSUMER = "retained-counter-consumer";

    @PostConstruct
    public void createGroup() {
        try {
            redisTemplate.opsForStream().createGroup(STREAM_KEY, ReadOffset.latest(), GROUP);
        } catch (Exception e) {
            if (!e.getMessage().contains("BUSYGROUP")) {
                log.error("Failed to create Redis Stream Group", e);
            }
        }
    }

    @Scheduled(fixedDelay = 10000)
    public void consume() {
        List<MapRecord<String, Object, Object>> records =
                redisTemplate
                        .opsForStream()
                        .read(
                                Consumer.from(GROUP, CONSUMER),
                                StreamReadOptions.empty().count(10).block(Duration.ofSeconds(1)),
                                StreamOffset.create(STREAM_KEY, ReadOffset.lastConsumed()));

        if (records == null) return;

        for (MapRecord<String, Object, Object> record : records) {
            try {
                Map<String, String> map =
                        record.getValue().entrySet().stream()
                                .collect(
                                        Collectors.toMap(
                                                e -> e.getKey().toString(),
                                                e -> e.getValue().toString()));

                String tenantId = map.get("tenantId");
                String direction = map.get("direction");
                String visitCategory = map.get("visitCategory");
                String timestamp = map.get("timestamp");

                String date = timestamp.substring(0, 10);
                String redisKey =
                        String.format(
                                "visit:count:%s:%s:%s:%s",
                                tenantId, date, visitCategory, direction);

                redisTemplate.opsForValue().increment(redisKey);

                redisTemplate.expire(redisKey, Duration.ofDays(1));

                redisTemplate.opsForStream().acknowledge(STREAM_KEY, GROUP, record.getId());

            } catch (Exception e) {
                log.error("Error processing stream message", e);
            }
        }
    }
}
