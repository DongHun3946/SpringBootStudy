package com.mysite.sbstudy.answer;

import com.mysite.sbstudy.user.SiteUser;
import com.mysite.sbstudy.question.Question;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Answer {
    @Id //기본키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동으로 1씩 증가하며 저장
    private Integer id;

    @Column(columnDefinition = "TEXT") //텍스트를 열 데이터로 넣을 수 있음
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @ManyToOne
    private SiteUser author;

    @ManyToOne //N:1 관계 (하나의 질문에 여러 개의 답변이 달릴 수 있으므로)
    private Question question;
}


