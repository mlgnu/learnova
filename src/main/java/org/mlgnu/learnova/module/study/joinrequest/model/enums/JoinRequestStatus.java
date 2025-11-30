package org.mlgnu.learnova.module.study.joinrequest.model.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum JoinRequestStatus {
    PENDING(0),
    APPROVED(1),
    REJECTED(2),
    CANCELLED(3);

    private static final Map<Integer, JoinRequestStatus> lookup =
            Arrays.stream(values()).collect(Collectors.toMap(JoinRequestStatus::getCode, s -> s));

    private final int code;

    JoinRequestStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static JoinRequestStatus fromCode(int code) {
        var status = lookup.get(code);
        if (status == null) throw new IllegalArgumentException("Unknown status code: " + code);
        return status;
    }
}
