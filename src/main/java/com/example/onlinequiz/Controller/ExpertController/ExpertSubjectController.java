package com.example.onlinequiz.Controller.ExpertController;

import com.example.onlinequiz.Services.SubjectTopicService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin( allowedHeaders = "*", origins = "https://quang-nv1911.onrender.com")
@RestController
@AllArgsConstructor
@RequestMapping("/api/expert/subject")
public class ExpertSubjectController {
    @Autowired
    private final SubjectTopicService subjectTopicService;

    // Delete subject topic
    @PutMapping("/delete/topic/{subjectTopicId}")
    public ResponseEntity<String> deleteSubjectTopic(
            @PathVariable Long subjectTopicId
    ) {
        try {
            subjectTopicService.deleteSubjectTopic(subjectTopicId);
            return ResponseEntity.ok("delete successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

     //Update order of subject topic
    @PutMapping("/update/order/{subjectTopicId}")
    public ResponseEntity<String> updateSubjectTopicOrder(
            @PathVariable Long subjectTopicId,
            @RequestParam String name,
            @RequestParam Integer order
    ) {
        try {
            subjectTopicService.updateOrderSubjectTopic(subjectTopicId, order, name);
            return ResponseEntity.ok("delete successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    // Add new subject topic
    @PostMapping("/add/topic/{subjectId}")
    public ResponseEntity<String> addNewSubjectTopic(
            @PathVariable Long subjectId,
            @RequestParam String topicName,
            @RequestParam Integer topicOrder
    ) {
        try {
            subjectTopicService.addNewSubjectTopic(subjectId, topicName, topicOrder);
            return ResponseEntity.ok("Add successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    // Edit data of the topic subject
    @PutMapping("/edit/topic/{subjectTopicId}")
    public ResponseEntity<String> updateSubjectTopicData(
            @PathVariable Long subjectTopicId,
            @RequestParam String topicName,
            @RequestParam Integer topicOrder
    ) {
        try {
            System.out.println("updateSubjectTopicData");
            subjectTopicService.updateSubjectTopic(subjectTopicId, topicName, topicOrder);
            return ResponseEntity.ok("update successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

}
