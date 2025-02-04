package com.cjh.blockbuster.api.model.exception;

import com.cjh.blockbuster.api.model.vo.Status;
import com.cjh.blockbuster.api.model.vo.constants.StatusEnum;
import lombok.Getter;

/**
 * 业务异常
 *
 * @author YiHui
 * @date 2022/9/2
 */
public class ForumAdviceException extends RuntimeException {
    @Getter
    private Status status;

    public ForumAdviceException(Status status) {
        this.status = status;
    }

    public ForumAdviceException(int code, String msg) {
        this.status = Status.newStatus(code, msg);
    }

    public ForumAdviceException(StatusEnum statusEnum, Object... args) {
        this.status = Status.newStatus(statusEnum, args);
    }

}
