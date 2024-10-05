package todo.common.constant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import todo.dto.response.ResponseDto;

public interface ResponseMessage {
    ResponseEntity<ResponseDto> SUCCESS
            = ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("SU", "SUCCESS"));

    ResponseEntity<ResponseDto> EXIST_USER_EMAIL
            = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto("UE", "Existent User Email"));

    ResponseEntity<ResponseDto> EXIST_PHONE_NUMBER
            = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto("PE", "Existent Phone Number"));

    ResponseEntity<ResponseDto> DATABASE_ERROR
            = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("DE", "Database Error"));

    ResponseEntity<ResponseDto> TOKEN_NOT_FOUND
            = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("TN", "Token Not Found"));

    ResponseEntity<ResponseDto> TOKEN_HAS_EXPIRED
            = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto("TE", "Token Has Expired"));

    ResponseEntity<ResponseDto> EMAIL_SEND_ERROR
            = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("EE", "Email Send Error"));

    ResponseEntity<ResponseDto> LOGIN_FAILED
            = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto("LF", "Login Failed"));

    ResponseEntity<ResponseDto> NOT_EXIST_USER
            = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("NU", "Not Existed User"));

    ResponseEntity<ResponseDto> IS_NOT_ACTIVATE
            = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto("NA", "Not Activate User"));

    ResponseEntity<ResponseDto> UNAUTHORIZED_TOKEN
            = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto("UT", "Unauthorized Token"));

    ResponseEntity<ResponseDto> PASSWORD_CURRENT_INVALID
            = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto("PI", "Current Password Invalid"));

    ResponseEntity<ResponseDto> NOT_EXIST_TODO
            = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("NT", "Not Exist ToDoList"));

    ResponseEntity<ResponseDto> NOT_EXIST_COMMENT
            = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("NC", "Not Exist Comment"));

    ResponseEntity<ResponseDto> UNAUTHORIZED_USER
            = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto("UU", "Unauthorized User")) ;

    ResponseEntity<ResponseDto> IMAGE_UPLOAD_ERROR
            = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("IE", "Image Upload Error"));
}
