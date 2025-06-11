package com.doubleo.logservice.domain.log.producer;

import com.doubleo.logservice.domain.log.dto.request.CreateAreaEnterLogRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AreaEnterLogStreamProducer {

    private final RedisTemplate<String, String> redisTemplate;

    public void sendAreaEnterLogToStream(CreateAreaEnterLogRequest request) {
        Map<String, String> message = new HashMap<>();
        message.put("tenantId", request.tenantId());
        message.put("areaId", request.areaId().toString());
        message.put("memberId", request.memberId().toString());
        message.put("memberName", request.memberName());
        message.put("passId", request.passId().toString());
        message.put("visitCategory", request.visitCategory().name());
        message.put("timestamp", LocalDateTime.now().toString());

        redisTemplate.opsForStream().add("area:enter:stream", message);
    }
}
