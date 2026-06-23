package com.qypenguin.ai.dto;

import java.util.List;

public record UserProfileDto(
    Integer age,
    String tryingDuration,
    Boolean hasCondition,
    List<String> conditions
) {}
