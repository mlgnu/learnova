package org.mlgnu.learnova.module.user.model.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum AccountStatus {
    ACTIVE(1),
    DISABLED(2),
    BANNED(3),
    DELETED(4);

    private static final Map<Integer, AccountStatus> lookup =
            Arrays.stream(values()).collect(Collectors.toMap(AccountStatus::getCode, s -> s));

    private final int code;

    AccountStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static AccountStatus fromCode(int code) {
        var status = lookup.get(code);
        if (status == null) throw new IllegalArgumentException("Unknown status code: " + code);
        return status;
    }
}
