package com.mysite.sbstudy.question;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

import com.mysite.sbstudy.user.SiteUser;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.mysite.sbstudy.DataNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Service
public class QuestionService {
    //데이터베이스와 상호작용하기 위함
    private final QuestionRepository questionRepository;

    public List<Question> getList() {//데이터베이스에서 모든 질문을 가져옴
        return this.questionRepository.findAll();
    }

    public Question getQuestion(Integer id) {
        //데이터베이스에서 id에 해당하는 질문을 가져옴
        Optional<Question> question = this.questionRepository.findById(id);
        //해당하는 질문이 있으면 가져옴
        if (question.isPresent()) {
            return question.get();
        }
        //없으면 예외처리
        else {
            throw new DataNotFoundException("question not found");
        }
    }

    public Page<Question> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        return this.questionRepository.findAll(pageable);
    }

    public void create(String subject, String content, SiteUser user) {
        Question q = Question.builder()
                .subject(subject)
                .content(content)
                .createDate(LocalDateTime.now())
                .author(user)
                .build();
        this.questionRepository.save(q);
    }

    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }
    public void delete(Question question){
        this.questionRepository.delete(question);
    }

}

