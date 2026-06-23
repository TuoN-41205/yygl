package com.qypenguin.ai.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record ChatSendRequest(
    String conversationId,
    @NotBlank String userId,
    @NotBlank String message,
    UserProfileDto profile,
    List<ConversationTurnDto> history
) {}
