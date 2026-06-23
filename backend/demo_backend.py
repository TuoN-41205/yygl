from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
import json
import re
from urllib.parse import urlparse, parse_qs

PORT = 8081

PROFILE = {
    "demo-user": {
        "age": None,
        "tryingDuration": None,
        "hasCondition": None,
        "conditions": []
    }
}

KNOWLEDGE = [
    {
        "type": "brand",
        "title": "品牌特点",
        "content": "启孕企鹅AI的品牌特点是温和、专业、清晰、克制，优先帮助用户把问题梳理明白，再给可执行的下一步。回答要像备孕健康管理助手，而不是医生诊断助手；输出要自然导向补档案、拍照查报告、查看专家团队、预约门诊或互联网医院复核。",
        "tags": ["品牌特点", "品牌定位", "首页AI", "导诊转化"]
    },
    {
        "type": "policy",
        "title": "话术边界",
        "content": "首页AI只做健康咨询、科普问答、远程健康管理和导诊推荐，不输出诊断结论、处方、用药方案、是否患病判断或替代医生诊疗的内容。涉及出血、腹痛、发热、妊娠异常、用药、手术方案、是否继续用药、是否换方案时，必须提示尽快转医生或互联网医院。",
        "tags": ["话术边界", "高风险拦截", "医生复核", "合规"]
    }
]

EXPERTS = [
    {
        "id": "d-4",
        "name": "杨会生",
        "avatar": "./doctor-1.png",
        "title": "主治医师",
        "headline": "医学博士 · 针灸生殖专家 · 调经促孕十三针传承人",
        "hospital": "深圳市妇幼保健院针灸科",
        "department": "中医针灸科",
        "fromYears": "从业xx年",
        "cardSpecialty": "调经促孕十三针、房氏火龙灸、调泌疏肝穴位埋线",
        "cardIntro": "师从国家中医药领军人才岐黄学者房繄恭首席研究员，年门诊量1.8万人次，累计服务人群超过5万人次。",
        "detailIntro": "目前任职于深圳市妇幼保健院针灸科，毕业于中国中医科学院，师从“调经促孕十三针”创始人、针灸助孕名家、国家中医药领军人才岐黄学者、首席研究员房繄恭教授。研究方向为针灸生殖效应的循证评价及效应机制研究，从事生殖障碍疾病的临床与科研工作，提倡“壮精调经、夫妻同治”，开设针灸生殖门诊和卵巢早衰针灸专病门诊，年门诊量1.5万人次。",
        "detailSocial": "中国针灸学会妇科生殖专业委员会会员；中国针灸学会治未病专业委员会委员；中国针灸学会与世界针灸联合会卵巢早衰联盟核心成员；广东省中医药学会生殖医学专委会委员",
        "detailSpecialty": "生殖障碍、卵巢功能障碍、盆底功能障碍疾病；调经促孕十三针、房氏火龙灸、三调助孕针法、益精助阳针法、盆底针法、调泌疏肝穴位埋线。"
    }
]


def json_response(handler, data, code=200):
    payload = json.dumps({"success": True, "data": data}, ensure_ascii=False).encode("utf-8")
    handler.send_response(code)
    handler.send_header("Content-Type", "application/json; charset=utf-8")
    handler.send_header("Access-Control-Allow-Origin", "*")
    handler.send_header("Access-Control-Allow-Headers", "Content-Type")
    handler.end_headers()
    handler.wfile.write(payload)


def plain_response(handler, text, code=200):
    payload = text.encode("utf-8")
    handler.send_response(code)
    handler.send_header("Content-Type", "text/plain; charset=utf-8")
    handler.send_header("Access-Control-Allow-Origin", "*")
    handler.end_headers()
    handler.wfile.write(payload)


def html_response(handler, html, code=200):
    payload = html.encode("utf-8")
    handler.send_response(code)
    handler.send_header("Content-Type", "text/html; charset=utf-8")
    handler.send_header("Access-Control-Allow-Origin", "*")
    handler.end_headers()
    handler.wfile.write(payload)


def extract_age(profile):
    if not profile:
        return None
    age = profile.get("age")
    if age is None:
        return None
    try:
        return int(age)
    except Exception:
        return None


def summarize_profile(profile):
    age = extract_age(profile)
    duration = (profile or {}).get("tryingDuration") or ""
    has_condition = (profile or {}).get("hasCondition")
    conditions = (profile or {}).get("conditions") or []
    parts = []
    if age is not None:
        parts.append(f"{age}岁")
    if duration:
        parts.append(duration)
    if has_condition is True:
        parts.append("有病状：" + "、".join(conditions) if conditions else "有病状")
    elif has_condition is False:
        parts.append("暂无明显病状")
    return "，".join(parts) if parts else "档案未填写"


def pick_knowledge_hint(message, knowledge):
    text = (message or "").lower()
    if "叶酸" in text:
        return "叶酸补充属于备孕基础管理，通常会结合备孕阶段、饮食和既往情况一起看。"
    if "amh" in text:
        return "AMH 偏低更适合从备孕时长、卵巢功能和既往检查一起综合判断，不建议单看一个指标下结论。"
    if "月经" in text:
        return "月经不规律常常会影响排卵判断，建议把周期、经期长度和是否有痛经一起补充。"
    if "促排" in text:
        return "促排前通常要先看基础检查和卵巢情况，再决定下一步。"
    if "报告" in text:
        return "报告解读更适合结合指标、症状和既往病史一起看。"
    return knowledge[0]["title"] if knowledge else "品牌口径"


def build_answer(message, profile, knowledge):
    age = extract_age(profile)
    duration = (profile or {}).get("tryingDuration") or "未填写"
    has_condition = (profile or {}).get("hasCondition")
    conditions = (profile or {}).get("conditions") or []
    condition_text = "、".join(conditions) if conditions else "暂无"
    knowledge_hint = pick_knowledge_hint(message, knowledge)

    opener = [
        f"你问的是「{message[:24]}」",
        f"我先按备孕管理帮你捋清楚"
    ]
    if age is not None:
        opener.append(f"你现在{age}岁")
    if duration != "未填写":
        opener.append(f"备孕时长是{duration}")

    body = []
    if "叶酸" in message:
        body.append("叶酸一般属于备孕基础补充，重点不是“越多越好”，而是剂量、开始时间和你是否有特殊情况。")
        body.append("如果没有特殊医嘱，通常先把备孕节奏、饮食和既往用药一起看，再决定怎么补更合适。")
    elif "amh" in message.lower():
        body.append("AMH 偏低不等于一定没机会，更重要的是结合月经情况、卵巢储备和你备孕了多久一起看。")
        body.append("如果你已经备孕一段时间，建议把最近的性激素、B 超或既往促排情况也补上，判断会更稳。")
    elif "月经" in message:
        body.append("月经不规律会让排卵时间更难判断，所以先把周期、经期天数、痛经情况和是否有明显波动说清楚很重要。")
        body.append("如果你本身还有多囊、体重波动或熬夜压力大，这些也会影响判断。")
    elif "促排" in message:
        body.append("促排前不建议只盯着一个检查，通常要结合卵巢功能、激素水平、超声和既往情况一起看。")
        body.append("你可以继续把既往检查结果发我，我帮你整理出下一步更该看什么。")
    else:
        body.append(f"我会结合你现在的档案和知识库里的「{knowledge_hint}」给你更贴近实际的建议。")
        body.append("如果你愿意继续补充月经、检查报告或既往治疗情况，我能帮你把下一步梳理得更细。")

    if has_condition is True:
        body.append(f"你现在勾选的相关情况是：{condition_text}，这会影响我对问题的判断顺序。")

    tail = []
    tail.append("如果你的情况涉及出血、腹痛、发热、妊娠异常、正在用药或考虑调整方案，这类内容要尽快转医生确认。")
    tail.append("你也可以继续看专家团队，或者把报告拍给我，我帮你先做一版整理。")

    first = "；".join(opener) + "。"
    second = " ".join(body)
    third = " ".join(tail[:1])
    fourth = tail[1]
    return f"{first}\n\n{second}\n\n{third}\n\n{fourth}"


class Handler(BaseHTTPRequestHandler):
    def do_OPTIONS(self):
        self.send_response(204)
        self.send_header("Access-Control-Allow-Origin", "*")
        self.send_header("Access-Control-Allow-Headers", "Content-Type")
        self.send_header("Access-Control-Allow-Methods", "GET,POST,OPTIONS")
        self.end_headers()

    def do_GET(self):
        parsed = urlparse(self.path)
        if parsed.path in ("/", "/api/v1"):
            return html_response(
                self,
                "<!doctype html><html><head><meta charset='utf-8'><title>启孕企鹅 Demo API</title>"
                "<style>body{font-family:-apple-system,BlinkMacSystemFont,'PingFang SC',sans-serif;padding:24px;color:#234}h1{margin:0 0 12px}code{background:#f3f6f8;padding:2px 6px;border-radius:6px}</style>"
                "</head><body><h1>启孕企鹅 Demo API</h1><p>后端已运行。</p><ul>"
                "<li><code>GET /api/v1/health</code></li>"
                "<li><code>GET /api/v1/knowledge</code></li>"
                "<li><code>POST /api/v1/knowledge</code></li>"
                "<li><code>GET /api/v1/profile/demo-user</code></li>"
                "<li><code>POST /api/v1/profile/demo-user</code></li>"
                "<li><code>GET /api/v1/experts</code></li>"
                "<li><code>POST /api/v1/chat/send</code></li>"
                "<li><code>POST /api/v1/report/analyze</code></li>"
                "</ul></body></html>",
            )
        if parsed.path == "/api/v1/health":
            return json_response(self, {"ok": True})
        if parsed.path == "/api/v1/knowledge":
            return json_response(self, KNOWLEDGE)
        if parsed.path == "/api/v1/knowledge/search":
            q = (parse_qs(parsed.query).get("q") or [""])[0].lower()
            result = [item for item in KNOWLEDGE if q in (item["title"] + item["content"]).lower()]
            return json_response(self, result)
        if parsed.path == "/api/v1/profile/demo-user":
            return json_response(self, PROFILE["demo-user"])
        if parsed.path == "/api/v1/experts":
            return json_response(self, EXPERTS)
        return plain_response(self, "not found", 404)

    def do_POST(self):
        length = int(self.headers.get("Content-Length", "0"))
        body = self.rfile.read(length).decode("utf-8") if length else "{}"
        try:
            data = json.loads(body)
        except Exception:
            data = {}
        parsed = urlparse(self.path)

        if parsed.path == "/api/v1/profile/demo-user":
            PROFILE["demo-user"] = {
                "age": data.get("age"),
                "tryingDuration": data.get("tryingDuration"),
                "hasCondition": data.get("hasCondition"),
                "conditions": data.get("conditions") or []
            }
            return json_response(self, PROFILE["demo-user"])

        if parsed.path == "/api/v1/knowledge":
            KNOWLEDGE.append({
                "type": data.get("type"),
                "title": data.get("title"),
                "content": data.get("content"),
                "tags": data.get("tags") or []
            })
            return json_response(self, KNOWLEDGE[-1])

        if parsed.path == "/api/v1/chat/send":
            user_id = data.get("userId") or "demo-user"
            profile = data.get("profile")
            if profile is not None:
                PROFILE[user_id] = {
                    "age": profile.get("age"),
                    "tryingDuration": profile.get("tryingDuration"),
                    "hasCondition": profile.get("hasCondition"),
                    "conditions": profile.get("conditions") or []
                }
            combined_profile = PROFILE.get(user_id, PROFILE["demo-user"])
            message = data.get("message") or ""
            answer = build_answer(message, combined_profile, KNOWLEDGE)
            return json_response(self, {
                "conversationId": data.get("conversationId") or "demo-conv-1",
                "replyType": "NORMAL",
                "riskLevel": "LOW",
                "answer": answer,
                "needHandoff": False,
                "handoffTarget": "启孕企鹅备孕AI",
                "quickReplies": ["补充基础信息", "拍照查报告", "查看专家团队"],
                "knowledgeTitles": [item["title"] for item in KNOWLEDGE[:5]]
            })

        if parsed.path == "/api/v1/report/analyze":
            return json_response(self, {
                "summary": "已完成初步识别，当前结果仅供信息参考。",
                "nextActions": ["补充基础信息", "继续查看专家建议"],
                "needHumanHandoff": False,
                "handoffTarget": "启孕企鹅备孕AI"
            })

        return plain_response(self, "not found", 404)


if __name__ == "__main__":
    server = ThreadingHTTPServer(("127.0.0.1", PORT), Handler)
    print(f"demo backend listening on {PORT}", flush=True)
    server.serve_forever()
