package com.mysite.sbstudy.answer;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class AnswerForm {
    @NotEmpty(message="내용은 필수입니다")
    private String content;
}

