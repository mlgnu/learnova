package org.mlgnu.learnova.module.study.joinrequest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record GetPostJoinRequestsRequest(@NotNull @Positive Long postId) {}
