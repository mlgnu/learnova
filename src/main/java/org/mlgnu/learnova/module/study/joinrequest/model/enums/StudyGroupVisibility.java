package org.mlgnu.learnova.module.study.joinrequest.model.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum StudyGroupVisibility {
    PUBLIC(0),
    PRIVATE(1);

    private static final Map<Integer, StudyGroupVisibility> lookup =
            Arrays.stream(values()).collect(Collectors.toMap(StudyGroupVisibility::getCode, s -> s));

    private final int code;

    StudyGroupVisibility(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static StudyGroupVisibility fromCode(int code) {
        var status = lookup.get(code);
        if (status == null) throw new IllegalArgumentException("Unknown status code: " + code);
        return status;
    }
}
