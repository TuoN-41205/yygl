package com.qypenguin.ai.service;

import com.qypenguin.ai.domain.ChatMessage;
import com.qypenguin.ai.domain.KnowledgeItem;
import com.qypenguin.ai.domain.UserProfile;
import java.util.List;
import java.util.StringJoiner;
import org.springframework.stereotype.Component;

@Component
public class PromptBuilder {
  public List<ChatMessage> buildSystemMessages(UserProfile profile, List<KnowledgeItem> contexts, List<ChatMessage> history, String userMessage) {
    StringBuilder sys = new StringBuilder();
    sys.append("你是启孕企鹅小程序的AI助手，服务22-50岁备孕人群。");
    sys.append("回答风格必须温和、专业、品牌化、少术语。");
    sys.append("所有普通问题统一采用自然的四步思路：先回应用户问题本身，再给可执行建议，再提示何时需要医生确认，最后轻度导流。");
    sys.append("不要输出诊断结论、处方、用药方案或替代医生诊疗。");
    sys.append("如涉及高风险问题（出血、腹痛、用药、妊娠异常、手术、急诊等），必须提示尽快转医生或互联网医院。");
    sys.append("如果信息不足，优先提示用户补档案，但仍要先给出可以理解的解释。");
    sys.append("\n\n用户档案：");
    sys.append(profile == null ? "空" : profileToText(profile));
    sys.append("\n\n知识库检索结果：");
    if (contexts == null || contexts.isEmpty()) {
      sys.append("无");
    } else {
      for (KnowledgeItem item : contexts) {
        sys.append("\n- [").append(item.type()).append("] ").append(item.title()).append("：").append(item.content());
      }
    }
    sys.append("\n\n输出要求：");
    sys.append("请用简洁自然的中文回复，不要输出固定标签式标题，不要让用户看到生成规则。");
    sys.append("内容上保持：先回应问题、再给建议、再补充就诊建议、最后轻度导流，但写成正常对话，不要显式写出规则标题。");
    sys.append("导流要轻度、自然，优先推荐补档案、拍照查报告、匹配医生或门诊服务。");

    return List.of(ChatMessage.system(sys.toString()));
  }

  private String profileToText(UserProfile profile) {
    StringJoiner joiner = new StringJoiner("，");
    if (profile.age() != null) joiner.add("年龄=" + profile.age());
    if (profile.tryingDuration() != null) joiner.add("备孕时长=" + profile.tryingDuration());
    if (profile.hasCondition() != null) joiner.add("有无病症=" + (profile.hasCondition() ? "有" : "无"));
    if (profile.conditions() != null && !profile.conditions().isEmpty()) joiner.add("病症标签=" + String.join("、", profile.conditions()));
    return joiner.toString();
  }
}
