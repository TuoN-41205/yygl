package com.qypenguin.ai.controller;

import com.qypenguin.ai.domain.ApiResponse;
import com.qypenguin.ai.domain.ChatResponse;
import com.qypenguin.ai.dto.ChatSendRequest;
import com.qypenguin.ai.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {
  private final ChatService chatService;

  public ChatController(ChatService chatService) {
    this.chatService = chatService;
  }

  @PostMapping("/send")
  public ApiResponse<ChatResponse> send(@Valid @RequestBody ChatSendRequest request) {
    return ApiResponse.ok(chatService.send(request));
  }
}
