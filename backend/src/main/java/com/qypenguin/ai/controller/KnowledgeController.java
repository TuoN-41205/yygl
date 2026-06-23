package com.qypenguin.ai.controller;

import com.qypenguin.ai.domain.ApiResponse;
import com.qypenguin.ai.domain.KnowledgeItem;
import com.qypenguin.ai.dto.KnowledgeSearchResponse;
import com.qypenguin.ai.dto.KnowledgeUpsertRequest;
import com.qypenguin.ai.service.KnowledgeService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/knowledge")
public class KnowledgeController {
  private final KnowledgeService knowledgeService;

  public KnowledgeController(KnowledgeService knowledgeService) {
    this.knowledgeService = knowledgeService;
  }

  @GetMapping("/search")
  public ApiResponse<KnowledgeSearchResponse> search(@RequestParam(defaultValue = "") String q) {
    return ApiResponse.ok(new KnowledgeSearchResponse(knowledgeService.search(q)));
  }

  @GetMapping
  public ApiResponse<List<KnowledgeItem>> list() {
    return ApiResponse.ok(knowledgeService.listAll());
  }

  @PostMapping
  public ApiResponse<KnowledgeItem> add(@Valid @RequestBody KnowledgeUpsertRequest request) {
    return ApiResponse.ok(knowledgeService.add(request));
  }
}
