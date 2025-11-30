package org.mlgnu.learnova.module.study.joinrequest.repository;

import org.mlgnu.learnova.common.exception.ResourceNotFoundException;
import org.mlgnu.learnova.module.study.joinrequest.model.entity.StudyGroupJoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyGroupJoinRepository extends JpaRepository<StudyGroupJoinRequest, Long> {
    boolean existsStudyGroupPostJoinRequestByUserIdAndPostId(Long userId, Long postId);

    Optional<StudyGroupJoinRequest> findStudyGroupJoinRequestByPostIdAndId(Long requestId, Long postId);

    default StudyGroupJoinRequest findStudyGroupJoinRequestByIdAndPostIdOrThrow(Long requestId, Long postId) {
        return findStudyGroupJoinRequestByPostIdAndId(requestId, postId)
                .orElseThrow(() -> new ResourceNotFoundException("Join request", requestId));
    }
}
