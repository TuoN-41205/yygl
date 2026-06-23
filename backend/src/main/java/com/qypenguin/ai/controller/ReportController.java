package com.qypenguin.ai.controller;

import com.qypenguin.ai.domain.ApiResponse;
import com.qypenguin.ai.domain.ReportAnalyzeResponse;
import com.qypenguin.ai.dto.ReportAnalyzeRequest;
import com.qypenguin.ai.service.ReportAnalyzeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/report")
public class ReportController {
  private final ReportAnalyzeService reportAnalyzeService;

  public ReportController(ReportAnalyzeService reportAnalyzeService) {
    this.reportAnalyzeService = reportAnalyzeService;
  }

  @PostMapping("/analyze")
  public ApiResponse<ReportAnalyzeResponse> analyze(@Valid @RequestBody ReportAnalyzeRequest request) {
    return ApiResponse.ok(reportAnalyzeService.analyze(request));
  }
}
