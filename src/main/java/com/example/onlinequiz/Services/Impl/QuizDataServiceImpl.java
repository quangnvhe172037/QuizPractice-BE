package com.example.onlinequiz.Services.Impl;

import com.example.onlinequiz.Model.*;
import com.example.onlinequiz.Payload.Response.QuestionResponse;
import com.example.onlinequiz.Repo.*;
import com.example.onlinequiz.Services.QuizDataService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@RequiredArgsConstructor
public class QuizDataServiceImpl implements QuizDataService {
    @Autowired
    private final QuizDetailRepository quizDetailRepository;

    @Autowired
    private final QuizDataRepository quizDataRepository;

    @Autowired
    private final QuizRepository quizRepository;

    @Autowired
    private final QuizAnswerRepository quizAnswerRepository;

    @Autowired
    private final QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private final SubjectRepository subjectRepository;

    @Autowired
    private final QuizResultDetailRepository quizResultDetailRepository;

    @Override
    public List<QuizData> getAllQuizData(Long id) {
        Quizzes q = new Quizzes();
        List<QuizDetail> qd;
        List<QuizData> quizData;

        q = quizRepository.findByQuizID(id);
        qd = quizDetailRepository.findAllByQuizzes(q);

//        quizData = qd.stream()
//                .map(QuizDetail::getQuizData)
//                .collect(Collectors.toList());
        quizData = quizDataRepository.findAllByQuizDetailIn(qd);
        return quizData;
    }

    @Override
    public void addNewQuizData(QuizData quizData) {
        quizDataRepository.save(quizData);
    }

    @Override
    public List<QuestionResponse> getQuestionBySubjectName(Long subjectName) {
        Subjects subject = subjectRepository.getSubjectsBySubjectID(subjectName);
        if (subject == null) {
            return new ArrayList<>();
        }

        List<QuizData> quizDataList = quizDataRepository.findBySubject(subject);
        List<QuestionResponse> questionResponses = new ArrayList<>();

        for (QuizData quizData : quizDataList) {
            List<QuizAnswers> quizAnswers = quizAnswerRepository.findByQuizData(quizData);
            List<String> answerOptions = new ArrayList<>();
            String explanation = null;
            String correctAnswer = ""; // Để lưu trữ câu trả lời đúng

            for (int i = 0; i < quizAnswers.size(); i++) {
                answerOptions.add(quizAnswers.get(i).getAnswerData());
                if (quizAnswers.get(i).isTrueAnswer()) {
                    // Ánh xạ câu trả lời đúng thành A, B, C, D
                    correctAnswer = Character.toString((char) ('A' + i));
                    explanation = quizAnswers.get(i).getExplanation();
                }
            }

            List<QuizQuestions> quizQuestions = quizQuestionRepository.findByQuizData(quizData);

            // Kiểm tra xem danh sách câu hỏi có ít nhất một câu hỏi hay không
            if (!quizQuestions.isEmpty()) {
                String questionData = quizQuestions.get(0).getQuestionData(); // Lấy câu hỏi đầu tiên

                QuestionResponse questionResponse = new QuestionResponse(quizData.getSentenceID(), questionData, answerOptions, explanation, correctAnswer);
                questionResponses.add(questionResponse);
            }
        }

        return questionResponses;
    }

    @Override
    public QuizData findById(Long id) {
        return quizDataRepository.findQuizDataBySentenceID(id);
    }

    @Override
    public String deleteSentenceLesson(Long sentenceId, Long lessonId) {
        try{
            QuizData quizData = quizDataRepository.findQuizDataBySentenceID(sentenceId);
            List<QuizAnswers> quizAnswers = quizAnswerRepository.findByQuizData(quizData);
            QuizQuestions quizQuestion = quizQuestionRepository.getByQuizData(quizData);
            List<QuizDetail> quizDetailList = quizDetailRepository.findAllByQuizData(quizData);

            List<QuizResultDetail> quizResultDetailList = quizResultDetailRepository.findAllByQuizData(quizData);
            if(quizResultDetailList != null){
                return "Can not delete because some student have do this test";
            }

            for (QuizDetail quizDetail: quizDetailList
            ) {
                quizDetailRepository.delete(quizDetail);
            }

            for (QuizAnswers quizAnswer : quizAnswers
            ) {
                quizAnswerRepository.delete(quizAnswer);
            }

            quizQuestionRepository.delete(quizQuestion);
            quizDataRepository.delete(quizData);
            return "Delete success";
        }catch (Exception e){
            System.out.println("deleteSentece - quizDataService");
            return "Delete fail";
        }

    }

    @Override
    public Boolean checkExistQuiz(QuizData quizData) {
        List<QuizResultDetail> quizResultDetailList = quizResultDetailRepository.findAllByQuizData(quizData);
        if(quizResultDetailList == null){
            return true;
        }else{
            return false;
        }
    }

    /**
     *
     * @param quantity
     * @return
     */

    public List<QuizData> getRandomQuizData(int quantity, Subjects subject) {
        // Lấy tất cả dữ liệu từ bảng quiz_data
        System.out.println("0" + subject.getSubjectID());
        try {
            List<Object[]> result = quizDataRepository.findAllBySubject(subject.getSubjectID());
            List<QuizData> allQuizData = new ArrayList<>();

            for (Object[] row : result) {
                QuizData quizData = new QuizData();
                quizData.setSentenceID((Long) row[0]);
                // Set other properties as needed
                allQuizData.add(quizData);
            }

            System.out.println("1");
            if(quantity > allQuizData.size()){
                System.out.println("2");
                quantity = allQuizData.size();
            }
            System.out.println("3");
            // Trộn ngẫu nhiên danh sách dữ liệu
            Collections.shuffle(allQuizData);
            System.out.println("4");
            // Chọn quantity phần tử đầu tiên sau khi đã trộn ngẫu nhiên
            quantity = allQuizData.size() < quantity ? allQuizData.size() : quantity;
            System.out.println("5");
            return allQuizData.subList(0, quantity);
        }catch (Exception e){
            System.out.println("QuizDataServcie + getRandom: " + e.getMessage());
            return null;
        }

    }

}

//    @Override
//    public Object getQuizData(Long id) {
//        QuizData quizData = quizDataRepository.getReferenceById(id);
//        QuizQuestions question = quizQuestionRepository.getByQuizData(quizData);
//        List<QuizAnswers> answers = quizAnswerRepository.getQuizAnswersByQuizData(quizData);
//
//
//        return;
//    }

