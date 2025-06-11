package com.doubleo.logservice.domain.log.consumer;

import com.doubleo.logservice.domain.log.domain.EnterLog;
import com.doubleo.logservice.domain.log.repository.EnterLogRepository;
import com.doubleo.logservice.global.enums.VisitCategory;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AreaEnterLogConsumer {

    private final RedisTemplate<String, String> redisTemplate;
    private final EnterLogRepository enterLogRepository;

    private static final String STREAM_KEY = "area:enter:stream";
    private static final String GROUP = "area:enter:group";
    private static final String CONSUMER_NAME = "area-enter-consumer-1";

    @PostConstruct
    public void initGroup() {
        try {
            redisTemplate.opsForStream().createGroup(STREAM_KEY, GROUP);
        } catch (Exception e) {
            if (!e.getMessage().contains("BUSYGROUP")) {
                log.warn("Stream group 생성 실패: {}", e.getMessage());
            }
        }
    }

    @Scheduled(fixedDelay = 10000)
    public void consumeMessages() {
        List<MapRecord<String, Object, Object>> messages =
                redisTemplate
                        .opsForStream()
                        .read(
                                Consumer.from(GROUP, CONSUMER_NAME),
                                StreamReadOptions.empty().count(100),
                                StreamOffset.create(STREAM_KEY, ReadOffset.lastConsumed()));

        for (MapRecord<String, Object, Object> msg : messages) {
            Map<Object, Object> data = msg.getValue();

            EnterLog enterLog =
                    EnterLog.createEnterLog(
                            (String) data.get("tenantId"),
                            Long.parseLong((String) data.get("areaId")),
                            Long.parseLong((String) data.get("memberId")),
                            (String) data.get("memberName"),
                            Long.parseLong((String) data.get("passId")),
                            VisitCategory.valueOf((String) data.get("visitCategory")));

            enterLogRepository.save(enterLog);
            redisTemplate.opsForStream().acknowledge(GROUP, msg);

            log.info(
                    "AreaEnterLog 저장 완료: memberId={}, areaId={}, tenantId={}",
                    enterLog.getMemberId(),
                    enterLog.getAreaId(),
                    enterLog.getTenantId());
        }
    }
}
