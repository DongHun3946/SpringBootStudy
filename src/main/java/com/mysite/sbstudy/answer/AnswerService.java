package com.mysite.sbstudy.answer;

import com.mysite.sbstudy.user.SiteUser;
import com.mysite.sbstudy.question.Question;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Builder
public class AnswerService {
    // AnswerRepository를 사용하여 답변을 데이터베이스에 저장하기 위함
    private final AnswerRepository answerRepository;

    // 답변 내용을 설정하여 데이터베이스에 저장
    public Answer create(Question question, String content, SiteUser author) {
        Answer answer = Answer.builder()
                .content(content)
                .createDate(LocalDateTime.now())
                .question(question)
                .author(author)
                .build();
        this.answerRepository.save(answer);
        return answer;
    }
}
