package todo.common.constant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import todo.dto.response.ResponseDto;

public interface ResponseMessage {
    public static final ResponseEntity<ResponseDto> SUCCESS
            = ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("SU", "SUCCESS"));

    public static final ResponseEntity<ResponseDto> EXIST_USER_EMAIL
            = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto("UE", "Existent User Email"));

    public static final ResponseEntity<ResponseDto> EXIST_PHONE_NUMBER
            = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto("PE", "Existent Phone Number"));

    public static final ResponseEntity<ResponseDto> DATABASE_ERROR
            = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("DE", "Database Error"));

    public static final ResponseEntity<ResponseDto> TOKEN_NOT_FOUND
            = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("TN", "Token Not Found"));

    public static final ResponseEntity<ResponseDto> TOKEN_HAS_EXPIRED
            = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto("TE", "Token Has Expired"));

    public static final ResponseEntity<ResponseDto> EMAIL_SEND_ERROR
            = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("EE", "Email Send Error"));

    public static final ResponseEntity<ResponseDto> LOGIN_FAILED
            = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto("LF", "Login Failed"));

    public static final ResponseEntity<ResponseDto> NOT_EXIST_USER
            = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("NU", "Not Existed User"));

    public static final ResponseEntity<ResponseDto> IS_NOT_ACTIVATE
            = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto("NA", "Not Activate User"));
}
