package org.mlgnu.learnova.module.study.post.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.mlgnu.learnova.module.study.joinrequest.model.enums.StudyGroupVisibility;
import org.mlgnu.learnova.module.study.post.model.converter.StudyGroupVisibilityConverter;
import org.mlgnu.learnova.module.user.model.entity.UserAccount;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "study_group_posts")
@SQLDelete(sql = "UPDATE study_group_posts SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class StudyGroupPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private UserAccount creator;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "VARCHAR(20)[]")
    private List<String> tags;

    @Convert(converter = StudyGroupVisibilityConverter.class)
    @Column(nullable = false)
    private StudyGroupVisibility visibility = StudyGroupVisibility.PUBLIC;

    @Column(updatable = false, columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime updatedAt;

    @Column(columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime deletedAt;

    @Column(columnDefinition = "tsvector", insertable = false, updatable = false)
    private String searchVector;

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
