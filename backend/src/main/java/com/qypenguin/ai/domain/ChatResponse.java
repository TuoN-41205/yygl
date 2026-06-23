package com.qypenguin.ai.domain;

import java.util.List;

public record ChatResponse(
    String conversationId,
    String replyType,
    String riskLevel,
    String answer,
    boolean needHumanHandoff,
    String handoffTarget,
    List<String> nextActions,
    List<String> sourceTitles
) {}
