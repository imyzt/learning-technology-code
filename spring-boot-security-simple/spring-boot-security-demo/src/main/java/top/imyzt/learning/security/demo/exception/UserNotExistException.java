package top.imyzt.learning.security.demo.exception;

import lombok.Getter;

/**
 * @author imyzt
 * @date 2019/6/3
 * @description UserNotExistException
 */
@Getter
public class UserNotExistException extends RuntimeException {

    private String id;

    public UserNotExistException(String id) {
        super("user not exist");
        this.id = id;
    }

}
