package com.mysite.sbstudy.question;

import java.util.List;
import com.mysite.sbstudy.user.SiteUser;
import com.mysite.sbstudy.answer.Answer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor  //파라미터가 없는 디폴트 생성자 생성
@AllArgsConstructor //모든 필드 값을 파라미터로 받는 생성자 생성
public class Question {
    @Id //기본키로 지정하기 위함
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동으로 1씩 증가하여 저장
    private Integer id;

    @Column(length = 200) //열의 길이를 200으로 지정
    private String subject;

    @Column(columnDefinition = "TEXT") //텍스트를 열 데이터로 넣을 수 있음
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @ManyToOne
    private SiteUser author;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;
}



