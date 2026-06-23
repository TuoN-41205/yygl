package com.qypenguin.ai.domain;

import java.time.Instant;
import java.util.List;

public record UserProfile(
    String userId,
    Integer age,
    String tryingDuration,
    Boolean hasCondition,
    List<String> conditions,
    Instant updatedAt
) {}
