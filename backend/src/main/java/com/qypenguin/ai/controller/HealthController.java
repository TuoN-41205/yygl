package com.qypenguin.ai.controller;

import com.qypenguin.ai.config.AppProperties;
import com.qypenguin.ai.domain.ApiResponse;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HealthController {
  private final AppProperties properties;

  public HealthController(AppProperties properties) {
    this.properties = properties;
  }

  @GetMapping("/health")
  public ApiResponse<Map<String, Object>> health() {
    Map<String, Object> data = new LinkedHashMap<>();
    data.put("status", "UP");
    data.put("service", "qingyun-penguin-ai-backend");
    data.put("modelAlias", properties.getAi().getModel());
    data.put("aiEnabled", properties.getAi().isEnabled());
    return ApiResponse.ok(data);
  }
}
