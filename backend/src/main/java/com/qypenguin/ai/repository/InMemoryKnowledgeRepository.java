package com.qypenguin.ai.repository;

import com.qypenguin.ai.domain.KnowledgeItem;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryKnowledgeRepository {
  private final CopyOnWriteArrayList<KnowledgeItem> items = new CopyOnWriteArrayList<>();

  public List<KnowledgeItem> findAll() {
    return List.copyOf(items);
  }

  public KnowledgeItem save(String type, String title, String content, List<String> tags) {
    KnowledgeItem item = new KnowledgeItem(
        UUID.randomUUID().toString(),
        type,
        title,
        content,
        tags == null ? List.of() : List.copyOf(tags),
        Instant.now()
    );
    items.add(item);
    return item;
  }

  public List<KnowledgeItem> search(String query, int limit) {
    String q = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
    if (q.isEmpty()) {
      return findAll().stream().limit(limit).toList();
    }
    return items.stream()
        .sorted(Comparator.comparing((KnowledgeItem item) -> score(item, q)).reversed()
            .thenComparing(KnowledgeItem::updatedAt, Comparator.reverseOrder()))
        .filter(item -> score(item, q) > 0)
        .limit(limit)
        .toList();
  }

  private int score(KnowledgeItem item, String q) {
    int score = 0;
    if (contains(item.title(), q)) score += 6;
    if (contains(item.content(), q)) score += 3;
    for (String tag : item.tags()) {
      if (contains(tag, q)) score += 4;
    }
    return score;
  }

  private boolean contains(String text, String query) {
    return text != null && text.toLowerCase(Locale.ROOT).contains(query);
  }

  public Optional<KnowledgeItem> findById(String id) {
    return items.stream().filter(item -> item.id().equals(id)).findFirst();
  }
}
