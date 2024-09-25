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
import todo.dto.request.user.ResetPwdRequestDto;
import todo.dto.response.ResponseDto;
import todo.entity.User;
import todo.repository.UserRepository;
import todo.service.EmailService;
import todo.util.PasswordUtil;
import todo.util.UUIDUtil;

import static todo.common.constant.ErrorMessage.MESSAGING_ERROR;

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
        sendEmail(email, "투두리스트 회원가입을 위한 인증 메일입니다.", "투두리스트 회원가입을 위한 이메일 인증 메일입니다.\n\n아래 링크를 클릭하면 가입 인증이 이루어집니다.\n", verificationUrl);
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

            sendEmail(dto.getEmail(), "투두리스트 임시 비밀번호 발급 메일입니다.", "투두리스트 임시 비밀번호 발급 메일입니다.\n\n임시 비밀번호 : ", temporalPwd);
            return ResponseMessage.SUCCESS;

        } catch (MessagingException exception) {
            log.error(MESSAGING_ERROR, exception);
            return ResponseMessage.EMAIL_SEND_ERROR;
        }
    }

    @Override
    public ResponseEntity<ResponseDto> sendRemoveEmail(String email, String content) {
        try {

            User user = userRepository.findUserByEmail(email);
            if (user == null) {
                return ResponseMessage.NOT_EXIST_USER;
            }

            String removeReason = "비속어 사용";

            sendEmail(email, "투두리스트 계정 탈퇴 안내 메일입니다.", "투두리스트 사이트에서 탈퇴되셨습니다. \n\n탈퇴사유 : ", removeReason);
            return ResponseMessage.SUCCESS;

        } catch (MessagingException exception) {
            log.error(MESSAGING_ERROR, exception);
            return ResponseMessage.EMAIL_SEND_ERROR;
        }
    }

    private void sendEmail(String email, String subject, String content, String verificationUrl) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("whdgkr9070@naver.com");
        helper.setTo(email);
        helper.setSubject(subject);
        String emailContent = content + verificationUrl;
        helper.setText(emailContent);

        mailSender.send(message);
    }
}
