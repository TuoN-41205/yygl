package com.qypenguin.ai.domain;

import java.util.List;

public record DoctorCard(
    String id,
    String name,
    String avatar,
    String title,
    String headline,
    String hospital,
    String department,
    String fromYears,
    String cardSpecialty,
    String cardIntro,
    String detailIntro,
    String detailSocial,
    String detailSpecialty,
    List<String> specialtyTags,
    List<String> introLines,
    List<String> featureTags
) {}
