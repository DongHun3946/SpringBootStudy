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
import com.mysite.sbstudy.answer.Answer;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
@RequiredArgsConstructor
@Service
public class QuestionService {
    //데이터베이스와 상호작용하기 위함
    private final QuestionRepository questionRepository;

    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                query.distinct(true); // 중 복 을 제 거
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제 목
                        cb.like(q.get("content"), "%" + kw + "%"), // 내 용
                        cb.like(u1.get("username"), "%" + kw + "%"), // 질 문 작 성
                        cb.like(a.get("content"), "%" + kw + "%"), // 답 변 내 용 자
                        cb.like(u2.get("username"), "%" + kw + "%")); // 답 변 작 성 자
            }
        };
    }


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

    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        Specification<Question> spec = search(kw);
        return this.questionRepository.findAll(spec, pageable);
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

