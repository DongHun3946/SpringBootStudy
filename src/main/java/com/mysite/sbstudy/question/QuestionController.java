package com.mysite.sbstudy.question;

import java.security.Principal;
import com.mysite.sbstudy.user.UserService;
import com.mysite.sbstudy.answer.AnswerForm;
import com.mysite.sbstudy.user.SiteUser;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;
    //웹 접속 시 맨 처음 보이는 화면(질문 리스트)
    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw){
        Page<Question> paging = this.questionService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "question_list";
    }


    //질문 클릭 시 보이는 상세화면
    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm){
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }
    //-------------------------------------create------------------------------------------
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm){
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal){
        if(bindingResult.hasErrors()){
            return "question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(), questionForm.
                getContent(), siteUser);

        return "redirect:/question/list";
    }
    //-------------------------------------create--------------------------------------------

    //-------------------------------------update--------------------------------------------
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id")
    Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수 정 권 한 이 없 습 니 다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult
            bindingResult, Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수 정 권 한 이 없 습 니 다.");
        }
        this.questionService.modify(question, questionForm.getSubject(),
                questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }
    //-------------------------------------update--------------------------------------------

    //-------------------------------------delete--------------------------------------------
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭 제 권 한 이 없 습 니 다.");
        }
        this.questionService.delete(question);
        return "redirect:/";
    }
    //-------------------------------------delete--------------------------------------------



}


