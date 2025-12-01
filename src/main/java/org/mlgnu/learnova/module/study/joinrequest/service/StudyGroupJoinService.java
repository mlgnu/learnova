package org.mlgnu.learnova.module.study.joinrequest.service;

import lombok.AllArgsConstructor;
import org.mlgnu.learnova.common.exception.ResourceNotFoundException;
import org.mlgnu.learnova.module.study.joinrequest.dto.SubmitJoinRequestRequest;
import org.mlgnu.learnova.module.study.joinrequest.exception.*;
import org.mlgnu.learnova.module.study.joinrequest.model.entity.StudyGroupJoinRequest;
import org.mlgnu.learnova.module.study.joinrequest.model.enums.JoinRequestStatus;
import org.mlgnu.learnova.module.study.joinrequest.model.enums.StudyGroupVisibility;
import org.mlgnu.learnova.module.study.joinrequest.repository.StudyGroupJoinRepository;
import org.mlgnu.learnova.module.study.post.model.entity.StudyGroupPost;
import org.mlgnu.learnova.module.study.post.repository.StudyGroupPostRepository;
import org.mlgnu.learnova.module.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class StudyGroupJoinService {

    private final StudyGroupPostRepository studyGroupPostRepository;
    private final StudyGroupJoinRepository joinRequestRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long submitJoinRequest(Long userId, Long postId, SubmitJoinRequestRequest request) {

        StudyGroupPost post = studyGroupPostRepository.getStudyGroupPostsByIdOrThrow(postId);

        // cannot join private requests
        if (post.getVisibility() == StudyGroupVisibility.PRIVATE) {
            throw new ResourceNotFoundException("Study group post", post.getId());
        }

        // cannot join own post
        if (post.getCreator().getId().equals(userId)) {
            throw new JoinOwnGroupException(userId);
        }

        // cannot request more than once
        if (joinRequestRepository.existsStudyGroupPostJoinRequestByUserIdAndPostId(userId, post.getId())) {
            throw new JoinRequestAlreadyExistsException(userId);
        }

        StudyGroupJoinRequest joinRequest = new StudyGroupJoinRequest();
        joinRequest.setPost(post);
        joinRequest.setUser(userRepository.getReferenceById(userId));
        joinRequest.setMessage(request.message());

        joinRequestRepository.save(joinRequest);
        return joinRequest.getId();
    }

    @Transactional
    public void cancelJoinRequest(Long userId, Long postId, Long requestId) {

        StudyGroupJoinRequest joinRequest = joinRequestRepository.findByPostIdAndIdOrThrow(postId, requestId);

        // only request owner can cancel the request
        if (!joinRequest.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Join request", requestId);
        }

        // only cancel pending requests
        if (joinRequest.getStatus() != JoinRequestStatus.PENDING) {
            throw new JoinRequestNotPendingException(joinRequest.getId());
        }

        joinRequest.setStatus(JoinRequestStatus.CANCELLED);
    }

    @Transactional
    public void approveJoinRequest(Long creatorId, Long postId, Long requestId) {

        StudyGroupJoinRequest joinRequest = joinRequestRepository.findByPostIdAndIdOrThrow(postId, requestId);

        // only post creator can approve the request
        if (!joinRequest.getPost().getCreator().getId().equals(creatorId)) {
            throw new ResourceNotFoundException("Join request", requestId);
        }

        // cancelled requests cannot be approved by post creator
        if (joinRequest.getStatus() == JoinRequestStatus.CANCELLED) {
            throw new JoinRequestCancelledException(joinRequest.getId());
        }

        // strict idempotency: cannot approve request more than once
        if (joinRequest.getStatus() == JoinRequestStatus.APPROVED) {
            throw new JoinRequestAlreadyApprovedException(joinRequest.getId());
        }

        // rejected requests cannot be approved
        if (joinRequest.getStatus() == JoinRequestStatus.REJECTED) {
            throw new JoinRequestAlreadyRejectedException(joinRequest.getId());
        }

        joinRequest.setStatus(JoinRequestStatus.APPROVED);
        // TODO call create group function in group package
    }

    @Transactional
    public void rejectJoinRequest(Long creatorId, Long postId, Long requestId) {

        StudyGroupJoinRequest joinRequest = joinRequestRepository.findByPostIdAndIdOrThrow(postId, requestId);

        // only post creator can reject the request
        if (!joinRequest.getPost().getCreator().getId().equals(creatorId)) {
            throw new ResourceNotFoundException("Join request", requestId);
        }

        // cancelled requests cannot be rejected by post creator
        if (joinRequest.getStatus() == JoinRequestStatus.CANCELLED) {
            throw new JoinRequestCancelledException(joinRequest.getId());
        }

        // strict idempotency: cannot reject request more than once
        if (joinRequest.getStatus() == JoinRequestStatus.REJECTED) {
            throw new JoinRequestAlreadyRejectedException(joinRequest.getId());
        }

        // approved requests cannot be rejected
        if (joinRequest.getStatus() == JoinRequestStatus.APPROVED) {
            throw new JoinRequestAlreadyApprovedException(joinRequest.getId());
        }

        joinRequest.setStatus(JoinRequestStatus.REJECTED);
    }
}
