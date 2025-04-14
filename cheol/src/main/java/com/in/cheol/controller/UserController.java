package com.in.cheol.controller;

import com.in.cheol.dto.QuestionFormDTO;
import com.in.cheol.dto.UserCreateFormDTO;
import com.in.cheol.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("userCreateFormDTO", new UserCreateFormDTO());
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateFormDTO userCreateFormDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!userCreateFormDTO.getPassword1().equals(userCreateFormDTO.getPassword2())) {
            //bindingResult.rejectValue(필드명, 오류 코드, 오류 메시지)
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }

        try {
            userService.create(userCreateFormDTO.getUsername(),
                    userCreateFormDTO.getEmail(), userCreateFormDTO.getPassword1());
        } catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        } catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

}
