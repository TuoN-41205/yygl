package com.qypenguin.ai.domain;

import java.util.List;

public record ReportAnalyzeResponse(
    String reportId,
    String summary,
    String riskLevel,
    List<String> indicators,
    List<String> nextActions,
    boolean needHumanHandoff,
    String handoffTarget
) {}
