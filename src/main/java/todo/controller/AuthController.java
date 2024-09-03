package todo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todo.dto.request.SignUpRequestDto;
import todo.dto.response.ResponseDto;
import todo.service.AuthService;

@RestController
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> userSignUp(
            @Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        return authService.userSignUp(signUpRequestDto);
    }
}
