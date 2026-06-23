package com.qypenguin.ai.controller;

import com.qypenguin.ai.domain.ApiResponse;
import com.qypenguin.ai.domain.DoctorCard;
import com.qypenguin.ai.service.DoctorService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/experts")
public class DoctorController {
  private final DoctorService doctorService;

  public DoctorController(DoctorService doctorService) {
    this.doctorService = doctorService;
  }

  @GetMapping
  public ApiResponse<List<DoctorCard>> list() {
    return ApiResponse.ok(doctorService.listAll());
  }

  @PostMapping
  public ApiResponse<DoctorCard> add(@RequestBody DoctorCard doctor) {
    return ApiResponse.ok(doctorService.add(doctor));
  }
}
