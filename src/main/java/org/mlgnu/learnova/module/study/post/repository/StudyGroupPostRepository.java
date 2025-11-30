package org.mlgnu.learnova.module.study.post.repository;

import org.mlgnu.learnova.module.study.post.exception.StudyGroupPostNotFoundException;
import org.mlgnu.learnova.module.study.post.model.entity.StudyGroupPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyGroupPostRepository extends JpaRepository<StudyGroupPost, Long> {
    Optional<StudyGroupPost> getStudyGroupPostsById(Long id);

    default StudyGroupPost getStudyGroupPostsByIdOrThrow(Long id) {
        return getStudyGroupPostsById(id).orElseThrow(() -> new StudyGroupPostNotFoundException(id.toString()));
    }
}
