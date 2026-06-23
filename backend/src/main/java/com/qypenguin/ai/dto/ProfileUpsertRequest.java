package com.qypenguin.ai.dto;

import java.util.List;

public record ProfileUpsertRequest(
    Integer age,
    String tryingDuration,
    Boolean hasCondition,
    List<String> conditions
) {}
