package com.qypenguin.ai.bootstrap;

import com.qypenguin.ai.domain.DoctorCard;
import com.qypenguin.ai.dto.KnowledgeUpsertRequest;
import com.qypenguin.ai.service.DoctorService;
import com.qypenguin.ai.service.KnowledgeService;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SeedDataRunner implements CommandLineRunner {
  private final DoctorService doctorService;
  private final KnowledgeService knowledgeService;

  public SeedDataRunner(DoctorService doctorService, KnowledgeService knowledgeService) {
    this.doctorService = doctorService;
    this.knowledgeService = knowledgeService;
  }

  @Override
  public void run(String... args) {
    if (doctorService.listAll().isEmpty()) {
      doctorService.add(new DoctorCard(
          "d-1",
          "刘淑平",
          "doctor-3.png",
          "主治医师",
          "深圳生殖健康多学科MDT分会委员、国家二级心理咨询师",
          "深圳市南山妇幼保健院妇科",
          "妇科",
          "从业xx余年",
          "深圳生殖健康多学科MDT分会委员、国家二级心理咨询师",
          "曾就职深圳市南山妇幼保健院妇科，关注复发性流产、不孕不育、输卵管不通和反复种植失败。",
          "曾就职深圳市南山妇幼保健院妇科，师从南方医科大学教授、深圳市医学会副会长吴若松教授。研究和临床关注复发性流产、不孕不育、输卵管不通和反复种植失败。",
          "深圳生殖健康多学科MDT分会委员；国家二级心理咨询师；曾就职深圳市南山妇幼保健院妇科。",
          "复发性流产、不孕不育、输卵管不通、反复种植失败",
          List.of("深圳生殖健康多学科MDT分会委员", "国家二级心理咨询师"),
          List.of("曾就职深圳市南山妇幼保健院妇科，师从南方医科大学教授、深圳市医学会副会长吴若松教授。"),
          List.of("复发性流产", "不孕不育", "输卵管不通", "反复种植失败")
      ));
      doctorService.add(new DoctorCard(
          "d-2",
          "任洪樟",
          "doctor-2.png",
          "主任中医师",
          "省级名中医、教授，从医50余年",
          "广东省名中医",
          "中医科",
          "从业50余年",
          "脑血管、神经、内分泌代谢、泌尿",
          "广东省名老中医专家学术经验传承工作指导老师，2002年评为黑龙江省名中医，临床经验丰富，疗效突出。",
          "毕业于黑龙江中医药大学，广东省名老中医专家学术经验传承工作指导老师，2002年评为黑龙江省名中医，从医五十余年，精通经方的临床应用，具有坚实的中医功底，临床经验丰富，疗效突出，善于研究总结并形成自己的学术思路用药风格。",
          "省级先进个人，专业技术拔尖人材，享受政府科技人才津贴；生于三世中医世家，自幼承传家技，一九六六年从师学徒毕业后，从事内科临床工作；八十年代进修于中国中医研究院西苑医院，为北京市名中医王占玺先生薪传弟子；九十年代进修于黑龙江省中医研究院，得国医大师张琪培养，受益良多。",
          "呼吸、消化、心脑血管、神经、内分泌代谢、泌尿、妇科、男性不育、肿瘤术后调治",
          List.of("省级先进个人", "三世中医世家", "专业技术拔尖人才"),
          List.of("广东省名老中医专家学术经验传承工作指导老师，2002年评为黑龙江省名中医，临床经验丰富，疗效突出。"),
          List.of("脑血管", "神经", "内分泌代谢", "泌尿", "疑难杂症")
      ));
      doctorService.add(new DoctorCard(
          "d-3",
          "王俊玲",
          "doctor-4.png",
          "主任中医师",
          "妇科医教研30余年、医学博士、广东省名中医",
          "省级名中医",
          "中医科",
          "从业30余年",
          "多囊卵巢综合征、子宫内膜异位症、异常子宫出血",
          "广东省首批名中医师承项目指导老师、硕士研究生导师，师从当代妇科八大家之一罗元恺教授以及国医大师刘敏如教授。",
          "广东省名中医，主任中医师，博士研究生学历，硕士研究生导师，毕业于广州中医药大学，从事中医妇产科临床、教学、科研工作30余年，擅长采用中西医结合的方法治疗妇产科疑难疾病，是广东省名中医，广东省首批名中医学术继承指导老师。",
          "广东省中西医结合学会综合医院中医专委会常务委员；广东省中医药学会妇科专委会委员；广东省中西医结合学会妇科专委会委员；深圳市中医药学会及中西医结合学会常务理事；深圳市中西医结合学会妇产科专委会副主任委员",
          "多囊卵巢综合征、子宫内膜异位症、异常子宫出血、不孕不育、反复自然流产、月经不调、卵巢早衰、高泌乳素血症、急慢性盆腔炎、黄褐斑、更年期综合征、失眠、亚健康、延缓衰老、孕期健康管理",
          List.of("中国妇幼保健协会", "广东省中医药学会妇科专委会委员"),
          List.of("广东省首批名中医师承项目指导老师，硕士研究生导师。"),
          List.of("多囊卵巢", "子宫内膜异位症", "母儿血型不合溶血病")
      ));
      doctorService.add(new DoctorCard(
          "d-4",
          "杨会生",
          "doctor-1.png",
          "主治医师",
          "医学博士 针灸生殖专家、调经促孕十三针传承人",
          "罗湖妇幼医院",
          "中医针灸科",
          "从业xx年",
          "调经促孕十三针、房氏火龙灸、调泌疏肝穴位埋线",
          "师从国家中医药领军人才岐黄学者房繄恭首席研究员，年门诊量1.8万人次，累计服务人群超过5万人次。",
          "目前任职于深圳市妇幼保健院针灸科，毕业于中国中医科学院，师从“调经促孕十三针”创始人、针灸助孕名家、国家中医药领军人才岐黄学者、首席研究员房繄恭教授。研究方向为针灸生殖效应的循证评价及效应机制研究，从事生殖障碍疾病的临床与科研工作，提倡“壮精调经、夫妻同治”，开设针灸生殖门诊和卵巢早衰针灸专病门诊，年门诊量1.5万人次。",
          "中国针灸学会妇科生殖专业委员会会员；中国针灸学会治未病专业委员会委员；中国针灸学会与世界针灸联合会卵巢早衰联盟核心成员；广东省中医药学会生殖医学专委会委员",
          "生殖障碍、卵巢功能障碍、盆底功能障碍疾病，采用调经促孕十三针、房氏火龙灸、三调助孕针法、益精助阳针法、盆底针法、调泌疏肝穴位埋线等疗法治疗生殖内分泌疾病",
          List.of("调经促孕十三针和房氏火龙灸传承人", "年度专家"),
          List.of("专注于备孕期中医针灸调理与生活方式指导。"),
          List.of("反复失败", "高龄备孕", "多囊卵巢")
      ));
    }

    if (knowledgeService.listAll().isEmpty()) {
      knowledgeService.add(new KnowledgeUpsertRequest(
          "brand",
          "回复结构",
          "统一采用先解释、再建议、再补充就诊建议、最后轻度导流的四段式输出。",
          List.of("回答结构", "品牌口径")
      ));
      knowledgeService.add(new KnowledgeUpsertRequest(
          "policy",
          "安全边界",
          "涉及出血、腹痛、妊娠异常、用药、手术、复诊、急诊等内容时，必须建议转医生或互联网医院，不输出诊断或处方。",
          List.of("风险拦截", "高风险问题")
      ));
      knowledgeService.add(new KnowledgeUpsertRequest(
          "service",
          "导流动作",
          "AI 回复后应自然引导用户补充档案、拍照查报告、查看医生推荐或进一步就诊。",
          List.of("导流", "服务闭环")
      ));
    }
  }
}
