package com.qypenguin.ai.dto;

import com.qypenguin.ai.domain.KnowledgeItem;
import java.util.List;

public record KnowledgeSearchResponse(List<KnowledgeItem> items) {}
