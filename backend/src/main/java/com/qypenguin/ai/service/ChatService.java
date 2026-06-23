package com.qypenguin.ai.service;

import com.qypenguin.ai.domain.ApiResponse;
import com.qypenguin.ai.domain.ChatMessage;
import com.qypenguin.ai.domain.ChatResponse;
import com.qypenguin.ai.domain.KnowledgeItem;
import com.qypenguin.ai.domain.UserProfile;
import com.qypenguin.ai.dto.ChatSendRequest;
import com.qypenguin.ai.dto.ConversationTurnDto;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
  private final ProfileService profileService;
  private final KnowledgeService knowledgeService;
  private final RiskPolicyService riskPolicyService;
  private final PromptBuilder promptBuilder;
  private final ModelGatewayClient modelGatewayClient;

  public ChatService(
      ProfileService profileService,
      KnowledgeService knowledgeService,
      RiskPolicyService riskPolicyService,
      PromptBuilder promptBuilder,
      ModelGatewayClient modelGatewayClient
  ) {
    this.profileService = profileService;
    this.knowledgeService = knowledgeService;
    this.riskPolicyService = riskPolicyService;
    this.promptBuilder = promptBuilder;
    this.modelGatewayClient = modelGatewayClient;
  }

  public ChatResponse send(ChatSendRequest request) {
    String conversationId = request.conversationId() == null || request.conversationId().isBlank()
        ? UUID.randomUUID().toString()
        : request.conversationId();
    UserProfile profile = request.profile() != null
        ? profileService.upsert(request.userId(), toProfileRequest(request))
        : profileService.get(request.userId());

    boolean highRisk = riskPolicyService.isHighRisk(request.message());
    List<KnowledgeItem> knowledge = knowledgeService.search(request.message());
    List<ChatMessage> messages = new ArrayList<>();
    messages.addAll(promptBuilder.buildSystemMessages(profile, knowledge, toMessages(request.history()), request.message()));
    if (request.history() != null) {
      for (ConversationTurnDto turn : request.history()) {
        if (turn.role() != null && turn.content() != null) {
          messages.add(new ChatMessage(turn.role(), turn.content()));
        }
      }
    }
    messages.add(ChatMessage.user(request.message()));

    String answer;
    boolean needHandoff = highRisk;
    String handoffTarget = highRisk ? "互联网医院问诊小程序 / 线下门诊" : "启孕企鹅备孕AI";
    String replyType = highRisk ? "HIGH_RISK" : "NORMAL";
    String riskLevel = riskPolicyService.riskLevel(request.message());

    if (highRisk) {
      answer = "你这个问题里包含高风险或需要医生判断的内容，AI 不适合直接给出诊断或用药结论。"
          + "建议你先补充当前症状、持续时间和相关报告，再由医生进一步确认。"
          + "如果有出血、腹痛、发热、妊娠异常或正在用药，请尽快转医生/互联网医院。"
          + "你也可以继续补充档案，或者直接进入专家团队、报告分析，我们帮你尽快进入下一步。";
    } else {
      answer = modelGatewayClient.generate(messages);
    }

    return new ChatResponse(
        conversationId,
        replyType,
        riskLevel,
        answer,
        needHandoff,
        handoffTarget,
        List.of("补充基础信息", "拍照查报告", "查看专家团队"),
        knowledge.stream().map(KnowledgeItem::title).toList()
    );
  }

  private List<ChatMessage> toMessages(List<ConversationTurnDto> history) {
    if (history == null) return List.of();
    return history.stream()
        .filter(t -> t.role() != null && t.content() != null)
        .map(t -> new ChatMessage(t.role(), t.content()))
        .toList();
  }

  private com.qypenguin.ai.dto.ProfileUpsertRequest toProfileRequest(ChatSendRequest request) {
    return new com.qypenguin.ai.dto.ProfileUpsertRequest(
        request.profile().age(),
        request.profile().tryingDuration(),
        request.profile().hasCondition(),
        request.profile().conditions()
    );
  }
}
