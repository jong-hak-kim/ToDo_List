package todo.service.implement;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import todo.common.constant.ResponseMessage;
import todo.dto.response.ResponseDto;
import todo.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {


    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public ResponseEntity<ResponseDto> sendTokenEmail(String email, String verificationUrl) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("whdgkr9070@naver.com");
        helper.setTo(email);
        helper.setSubject("투두리스트 회원가입을 위한 인증 메일입니다.");
        String emailContent = "투두리스트 회원가입을 위한 이메일 인증 메일입니다.\n\n" +
                "아래 링크를 클릭하면 가입 인증이 이루어집니다.\n" +
                verificationUrl;
        helper.setText(emailContent);

        mailSender.send(message);
        return ResponseMessage.SUCCESS;
    }
}
