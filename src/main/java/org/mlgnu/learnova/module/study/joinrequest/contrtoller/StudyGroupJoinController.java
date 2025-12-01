package org.mlgnu.learnova.module.study.joinrequest.contrtoller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.mlgnu.learnova.module.auth.model.JwtUserPrincipal;
import org.mlgnu.learnova.module.study.joinrequest.dto.SubmitJoinRequestRequest;
import org.mlgnu.learnova.module.study.joinrequest.service.StudyGroupJoinService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
@SecurityRequirement(name = "bearerJWT")
@Tag(name = "Study Group Join Requests", description = "Endpoints for managing study group join requests")
@RequestMapping("/posts")
public class StudyGroupJoinController {

    private final StudyGroupJoinService joinService;

    @PostMapping("/{postId}/join-requests")
    @Operation(summary = "Submit Join Request", description = "Submit a request to join a study group")
    public ResponseEntity<Void> createJoinRequest(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long postId,
            @Valid @RequestBody SubmitJoinRequestRequest request,
            UriComponentsBuilder uriComponentsBuilder) {

        Long requestId = joinService.submitJoinRequest(principal.getId(), postId, request);

        UriComponents uriComponents = uriComponentsBuilder
                .path("/api/posts/{postId}/join-requests/{requestId}")
                .buildAndExpand(postId, requestId);

        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    @DeleteMapping("/{postId}/join-requests/{requestId}")
    @Operation(summary = "Cancel Join Request", description = "Cancel a previously submitted join")
    public ResponseEntity<Void> cancelJoinRequest(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long postId,
            @PathVariable Long requestId) {

        joinService.cancelJoinRequest(principal.getId(), postId, requestId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{postId}/join-requests/{requestId}/approve")
    @Operation(summary = "Approve Join Request", description = "Approve a join request for a study group")
    public ResponseEntity<Void> approveJoinRequest(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long postId,
            @PathVariable Long requestId) {

        joinService.approveJoinRequest(principal.getId(), postId, requestId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{postId}/join-requests/{requestId}/reject")
    @Operation(summary = "Reject Join Request", description = "Reject a join request for a study group")
    public ResponseEntity<Void> rejectJoinRequest(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable Long postId,
            @PathVariable Long requestId) {

        joinService.rejectJoinRequest(principal.getId(), postId, requestId);
        return ResponseEntity.ok().build();
    }
}
