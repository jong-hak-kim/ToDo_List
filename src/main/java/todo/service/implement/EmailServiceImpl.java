package todo.service.implement;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import todo.common.constant.ResponseMessage;
import todo.dto.request.ResetPwdRequestDto;
import todo.dto.response.ResponseDto;
import todo.entity.User;
import todo.repository.UserRepository;
import todo.service.EmailService;
import todo.util.PasswordUtil;
import todo.util.UUIDUtil;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {


    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender, UserRepository userRepository, PasswordUtil passwordUtil) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
        this.passwordUtil = passwordUtil;
    }

    @Override
    public ResponseEntity<ResponseDto> sendSignUpEmail(String email, String verificationUrl) throws MessagingException {
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

    @Override
    public ResponseEntity<ResponseDto> sendTemporalPwdEmail(ResetPwdRequestDto dto) {
        try {

            User user = userRepository.findUserByEmail(dto.getEmail());

            if (user == null) {
                return ResponseMessage.NOT_EXIST_USER;
            }

            if (!user.isActive()) {
                return ResponseMessage.IS_NOT_ACTIVATE;
            }

            String temporalPwd = UUIDUtil.generatePwdUUID();
            String encodePwd = passwordUtil.encodePassword(temporalPwd);
            user.setPassword(encodePwd);
            userRepository.save(user);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom("whdgkr9070@naver.com");
            helper.setTo(dto.getEmail());
            helper.setSubject("투두리스트 임시 비밀번호 발급 메일입니다.");
            String emailContent = "투두리스트 임시 비밀번호 발급 메일입니다.\n\n" +
                    "임시 비밀번호 : " +
                    temporalPwd;
            helper.setText(emailContent);

            mailSender.send(message);
            return ResponseMessage.SUCCESS;

        } catch (MessagingException exception) {
            log.error("error occurred while sending the email", exception);
            return ResponseMessage.EMAIL_SEND_ERROR;
        }
    }
}
