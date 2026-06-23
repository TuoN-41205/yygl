package com.qypenguin.ai.dto;

public record ReportAnalyzeRequest(
    String reportId,
    String userId,
    String imageText,
    String imageUrl
) {}
