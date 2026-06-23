package com.qypenguin.ai.repository;

import com.qypenguin.ai.domain.UserProfile;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryProfileRepository {
  private final ConcurrentHashMap<String, UserProfile> store = new ConcurrentHashMap<>();

  public UserProfile save(String userId, Integer age, String tryingDuration, Boolean hasCondition, List<String> conditions) {
    UserProfile profile = new UserProfile(userId, age, tryingDuration, hasCondition, conditions == null ? List.of() : List.copyOf(conditions), Instant.now());
    store.put(userId, profile);
    return profile;
  }

  public UserProfile findOrDefault(String userId) {
    return store.getOrDefault(userId, new UserProfile(userId, null, null, null, List.of(), null));
  }
}
