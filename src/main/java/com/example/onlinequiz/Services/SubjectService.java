package com.example.onlinequiz.Services;

import com.example.onlinequiz.Model.SubjectTopics;
import com.example.onlinequiz.Model.Subjects;
import com.example.onlinequiz.Payload.Response.SubjectDetailResponse;

import java.util.List;

public interface SubjectService {
    List<Subjects> getAllSubject();

//    List<Subjects> getExpertSubject(Long userId);

    Subjects getSubjectById(Long id);

    Subjects save(Subjects subjects);

    SubjectDetailResponse getSubjectDetailPublic(Long subjectId);
    SubjectDetailResponse getSubjectDetail(Long userId, Long subjectId);

    Long countAllSubject();

    List<Subjects> getSubjectByExpert(Long userId);

}
