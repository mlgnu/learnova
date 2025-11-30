package org.mlgnu.learnova.module.study.post.exception;

public class StudyGroupPostNotFoundException extends RuntimeException {

    public StudyGroupPostNotFoundException(String identifier) {
        super("Study group post not found: " + identifier);
    }
}
