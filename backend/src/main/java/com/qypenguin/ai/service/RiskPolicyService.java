package com.qypenguin.ai.service;

import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;

@Service
public class RiskPolicyService {
  private static final List<String> HIGH_RISK_KEYWORDS = List.of(
      "出血", "腹痛", "宫外孕", "流产", "先兆流产", "破水", "胎动", "发热",
      "急诊", "住院", "手术", "用药", "药物", "处方", "复诊", "治疗方案",
      "阴道出血", "怀孕", "妊娠", "孕周", "麻醉", "切除", "昏迷"
  );

  public boolean isHighRisk(String message) {
    if (message == null) {
      return false;
    }
    String normalized = message.toLowerCase(Locale.ROOT);
    for (String keyword : HIGH_RISK_KEYWORDS) {
      if (normalized.contains(keyword.toLowerCase(Locale.ROOT))) {
        return true;
      }
    }
    return false;
  }

  public String riskLevel(String message) {
    return isHighRisk(message) ? "HIGH" : "NORMAL";
  }
}
