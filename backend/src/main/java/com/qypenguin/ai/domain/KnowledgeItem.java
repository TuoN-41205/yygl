package com.qypenguin.ai.domain;

import java.time.Instant;
import java.util.List;

public record KnowledgeItem(
    String id,
    String type,
    String title,
    String content,
    List<String> tags,
    Instant updatedAt
) {}
