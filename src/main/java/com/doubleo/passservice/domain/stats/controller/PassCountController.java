package com.doubleo.passservice.domain.stats.controller;

import com.doubleo.passservice.domain.stats.dto.request.PassCountInfoRequest;
import com.doubleo.passservice.domain.stats.dto.response.PassCountInfoResponse;
import com.doubleo.passservice.domain.stats.service.PassCountService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pass-logs")
@RequiredArgsConstructor
public class PassCountController {

    private final PassCountService passCountService;

    @PostMapping("/search")
    public List<PassCountInfoResponse> passCountListGet(
            @RequestBody @Valid PassCountInfoRequest request) {
        return passCountService.getPassCount(request);
    }
}
