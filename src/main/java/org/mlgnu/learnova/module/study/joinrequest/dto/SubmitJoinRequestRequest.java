package org.mlgnu.learnova.module.study.joinrequest.dto;

import jakarta.validation.constraints.Size;

public record SubmitJoinRequestRequest(
        @Size(max = 2000, message = "Message should be less than 2K characters")
        String message) {}
