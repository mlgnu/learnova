package org.mlgnu.learnova.module.study.joinrequest.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mlgnu.learnova.module.study.joinrequest.model.entity.StudyGroupJoinRequest;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudyGroupJoinRepositoryDefaultMethodsTest {
    @Mock(answer = Answers.CALLS_REAL_METHODS)
    StudyGroupJoinRepository repository;

    @Nested
    class FindByPostAndIdTest {
        @Test
        @DisplayName("should return request when found")
        public void shouldReturnRequestWhenFound() {
            StudyGroupJoinRequest request = new StudyGroupJoinRequest();
            when(repository.findByPostIdAndId(1L, 2L)).thenReturn(Optional.of(request));

            StudyGroupJoinRequest result = repository.findByPostIdAndIdOrThrow(1L, 2L);
            assertSame(request, result);
        }
    }
}
