package com.qypenguin.ai.service;

import com.qypenguin.ai.domain.KnowledgeItem;
import com.qypenguin.ai.dto.KnowledgeUpsertRequest;
import com.qypenguin.ai.repository.InMemoryKnowledgeRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class KnowledgeService {
  private final InMemoryKnowledgeRepository repository;

  public KnowledgeService(InMemoryKnowledgeRepository repository) {
    this.repository = repository;
  }

  public List<KnowledgeItem> search(String q) {
    return repository.search(q, 8);
  }

  public KnowledgeItem add(KnowledgeUpsertRequest request) {
    return repository.save(request.type(), request.title(), request.content(), request.tags());
  }

  public List<KnowledgeItem> listAll() {
    return repository.findAll();
  }
}
