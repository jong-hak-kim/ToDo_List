package todo.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import todo.dto.request.SignUpRequestDto;
import todo.entity.User;

@RestController
public class UserController {

    @PostMapping("/regist")
    public String signUp(
            @Valid @RequestBody SignUpRequestDto signUpRequestDto) {

    }
}
