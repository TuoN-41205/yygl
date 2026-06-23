package com.qypenguin.ai.dto;

import java.util.List;

public record KnowledgeUpsertRequest(
    String type,
    String title,
    String content,
    List<String> tags
) {}
