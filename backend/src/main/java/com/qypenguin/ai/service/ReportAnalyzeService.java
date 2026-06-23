package com.qypenguin.ai.service;

import com.qypenguin.ai.domain.ReportAnalyzeResponse;
import com.qypenguin.ai.dto.ReportAnalyzeRequest;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReportAnalyzeService {
  private final RiskPolicyService riskPolicyService;

  public ReportAnalyzeService(RiskPolicyService riskPolicyService) {
    this.riskPolicyService = riskPolicyService;
  }

  public ReportAnalyzeResponse analyze(ReportAnalyzeRequest request) {
    String text = request.imageText() == null ? "" : request.imageText();
    boolean highRisk = riskPolicyService.isHighRisk(text);
    return new ReportAnalyzeResponse(
        request.reportId() == null || request.reportId().isBlank() ? "report-" + System.currentTimeMillis() : request.reportId(),
        highRisk
            ? "报告中存在需要尽快医生确认的内容，建议优先转人工复核。"
            : "已完成初步识别，当前结果仅供信息参考，后续可结合档案与医生建议继续判断。",
        highRisk ? "HIGH" : "NORMAL",
        List.of("指标解释待接入 OCR 结果", "重点字段待接入规则引擎", "可后续补充报告解析知识库"),
        highRisk ? List.of("尽快转医生复核", "上传更多报告信息") : List.of("补充基础信息", "继续查看专家建议"),
        highRisk,
        highRisk ? "互联网医院问诊小程序 / 线下门诊" : "启孕企鹅备孕AI"
    );
  }
}
