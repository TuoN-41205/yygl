package com.qypenguin.ai.service;

import com.qypenguin.ai.domain.UserProfile;
import com.qypenguin.ai.dto.ProfileUpsertRequest;
import com.qypenguin.ai.repository.InMemoryProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
  private final InMemoryProfileRepository repository;

  public ProfileService(InMemoryProfileRepository repository) {
    this.repository = repository;
  }

  public UserProfile upsert(String userId, ProfileUpsertRequest request) {
    return repository.save(userId, request.age(), request.tryingDuration(), request.hasCondition(), request.conditions());
  }

  public UserProfile get(String userId) {
    return repository.findOrDefault(userId);
  }
}
