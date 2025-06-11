package com.doubleo.logservice.domain.log.consumer;

import com.doubleo.logservice.domain.log.domain.BuildingEnterLog;
import com.doubleo.logservice.domain.log.domain.Direction;
import com.doubleo.logservice.domain.log.repository.BuildingEnterLogRepository;
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
public class BuildingEnterLogConsumer {

    private final RedisTemplate<String, String> redisTemplate;
    private final BuildingEnterLogRepository buildingEnterLogRepository;

    private static final String STREAM_KEY = "building:enter:stream";
    private static final String GROUP = "building:enter:group";
    private static final String CONSUMER_NAME = "building-enter-consumer-1";

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

            BuildingEnterLog buildingEnterLog =
                    BuildingEnterLog.createBuildingEnterLog(
                            (String) data.get("tenantId"),
                            Long.parseLong((String) data.get("buildingId")),
                            Long.parseLong((String) data.get("memberId")),
                            (String) data.get("memberName"),
                            Long.parseLong((String) data.get("passId")),
                            Direction.valueOf(((String) data.get("direction")).toUpperCase()),
                            VisitCategory.valueOf(
                                    ((String) data.get("visitCategory")).toUpperCase()));
            buildingEnterLogRepository.save(buildingEnterLog);
            redisTemplate.opsForStream().acknowledge(GROUP, msg);

            log.info(
                    "BuildingEnterLog 저장 완료: memberId={}, buildingId={}, tenantId={}",
                    buildingEnterLog.getMemberId(),
                    buildingEnterLog.getBuildingId(),
                    buildingEnterLog.getTenantId());
        }
    }
}
