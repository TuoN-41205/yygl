package com.qypenguin.ai.repository;

import com.qypenguin.ai.domain.DoctorCard;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryDoctorRepository {
  private final CopyOnWriteArrayList<DoctorCard> doctors = new CopyOnWriteArrayList<>();

  public List<DoctorCard> findAll() {
    return List.copyOf(doctors);
  }

  public DoctorCard save(DoctorCard doctor) {
    doctors.add(doctor);
    return doctor;
  }
}
