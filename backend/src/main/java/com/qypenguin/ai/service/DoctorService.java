package com.qypenguin.ai.service;

import com.qypenguin.ai.domain.DoctorCard;
import com.qypenguin.ai.repository.InMemoryDoctorRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {
  private final InMemoryDoctorRepository repository;

  public DoctorService(InMemoryDoctorRepository repository) {
    this.repository = repository;
  }

  public List<DoctorCard> listAll() {
    return repository.findAll();
  }

  public DoctorCard add(DoctorCard doctor) {
    return repository.save(doctor);
  }
}
