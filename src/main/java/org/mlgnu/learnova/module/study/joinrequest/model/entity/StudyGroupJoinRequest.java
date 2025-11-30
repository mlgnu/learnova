package org.mlgnu.learnova.module.study.joinrequest.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.mlgnu.learnova.module.study.joinrequest.model.converter.JoinRequestStatusConverter;
import org.mlgnu.learnova.module.study.joinrequest.model.enums.JoinRequestStatus;
import org.mlgnu.learnova.module.study.post.model.entity.StudyGroupPost;
import org.mlgnu.learnova.module.user.model.entity.UserAccount;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "study_group_post_join_requests")
public class StudyGroupJoinRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private StudyGroupPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private UserAccount user;

    @Convert(converter = JoinRequestStatusConverter.class)
    @Column(nullable = false)
    private JoinRequestStatus status = JoinRequestStatus.PENDING;

    private String message;

    @Column(updatable = false)
    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}
