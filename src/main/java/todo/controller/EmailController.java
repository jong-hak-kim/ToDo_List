package todo.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import todo.dto.request.EmailVerifyRequestDto;
import todo.dto.response.ResponseDto;

@RestController
public class EmailController {

    @PostMapping("/email/verify")
    public ResponseEntity<ResponseDto> emailVerify(
            @Valid @RequestBody EmailVerifyRequestDto
            ){

    }

}
