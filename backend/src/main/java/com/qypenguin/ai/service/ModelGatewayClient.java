package com.qypenguin.ai.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qypenguin.ai.config.AppProperties;
import com.qypenguin.ai.domain.ChatMessage;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ModelGatewayClient {
  private final AppProperties properties;
  private final ObjectMapper objectMapper;
  private final HttpClient httpClient;

  public ModelGatewayClient(AppProperties properties, ObjectMapper objectMapper) {
    this.properties = properties;
    this.objectMapper = objectMapper;
    this.httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(Math.max(5, properties.getAi().getTimeoutSeconds())))
        .build();
  }

  public String generate(List<ChatMessage> messages) {
    if (!properties.getAi().isEnabled() || isBlank(properties.getAi().getBaseUrl())) {
      return fallback(messages);
    }
    try {
      ChatCompletionsRequest body = new ChatCompletionsRequest(
          properties.getAi().getModel(),
          messages.stream().map(m -> new ChatCompletionsMessage(m.role(), m.content())).toList(),
          0.2
      );
      HttpRequest.Builder builder = HttpRequest.newBuilder()
          .uri(URI.create(trimSlash(properties.getAi().getBaseUrl()) + properties.getAi().getChatPath()))
          .timeout(Duration.ofSeconds(Math.max(5, properties.getAi().getTimeoutSeconds())))
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(body)));
      if (!isBlank(properties.getAi().getApiKey())) {
        builder.header("Authorization", "Bearer " + properties.getAi().getApiKey());
      }
      HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() >= 200 && response.statusCode() < 300) {
        ChatCompletionsResponse parsed = objectMapper.readValue(response.body(), ChatCompletionsResponse.class);
        if (parsed.choices != null && !parsed.choices.isEmpty() && parsed.choices.get(0).message != null) {
          return parsed.choices.get(0).message.content;
        }
      }
      System.err.println("AI gateway returned non-success status: " + response.statusCode());
      return fallback(messages);
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
      System.err.println("AI gateway interrupted: " + ex.getMessage());
      return fallback(messages);
    } catch (IOException ex) {
      System.err.println("AI gateway IO error: " + ex.getMessage());
      return fallback(messages);
    }
  }

  private String fallback(List<ChatMessage> messages) {
    String userQuestion = messages == null || messages.isEmpty() ? "你的问题" : messages.get(messages.size() - 1).content();
    return "我先根据你提到的“" + summarize(userQuestion) + "”帮你做一个温和、清晰的说明。"
        + "如果你愿意，先补齐基础信息，再结合备孕阶段和症状标签，后面的判断会更贴近你的情况。"
        + "如果问题涉及出血、腹痛、用药或妊娠异常，建议尽快转医生或互联网医院进一步确认。"
        + "你也可以继续补充档案、拍照查报告，或者查看更匹配的医生与门诊服务。";
  }

  private String summarize(String text) {
    if (text == null || text.isBlank()) return "当前问题";
    return text.length() > 20 ? text.substring(0, 20) + "…" : text;
  }

  private String trimSlash(String baseUrl) {
    return baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
  }

  private boolean isBlank(String value) {
    return value == null || value.isBlank();
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record ChatCompletionsRequest(
      String model,
      List<ChatCompletionsMessage> messages,
      double temperature
  ) {}

  public record ChatCompletionsMessage(String role, String content) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ChatCompletionsResponse {
    public List<Choice> choices;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Choice {
    public ChatCompletionsMessageContent message;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ChatCompletionsMessageContent {
    public String role;
    public String content;
  }
}
