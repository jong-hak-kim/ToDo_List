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
}
