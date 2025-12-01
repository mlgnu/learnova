package org.mlgnu.learnova.module.study.joinrequest.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mlgnu.learnova.common.exception.ResourceNotFoundException;
import org.mlgnu.learnova.module.study.joinrequest.dto.SubmitJoinRequestRequest;
import org.mlgnu.learnova.module.study.joinrequest.exception.*;
import org.mlgnu.learnova.module.study.joinrequest.model.entity.StudyGroupJoinRequest;
import org.mlgnu.learnova.module.study.joinrequest.model.enums.JoinRequestStatus;
import org.mlgnu.learnova.module.study.joinrequest.model.enums.StudyGroupVisibility;
import org.mlgnu.learnova.module.study.joinrequest.repository.StudyGroupJoinRepository;
import org.mlgnu.learnova.module.study.post.model.entity.StudyGroupPost;
import org.mlgnu.learnova.module.study.post.repository.StudyGroupPostRepository;
import org.mlgnu.learnova.module.user.model.entity.UserAccount;
import org.mlgnu.learnova.module.user.repository.UserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudyGroupJoinServiceTest {

    @Mock
    private StudyGroupPostRepository studyGroupPostRepository;

    @Mock
    private StudyGroupJoinRepository joinRequestRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StudyGroupJoinService cut;

    @Nested
    class SubmitJoinRequestTest {

        @Test
        @DisplayName("should fail when post is private")
        public void privatePostNotAllowed() {

            StudyGroupPost post = new StudyGroupPost();
            post.setId(1L);
            post.setVisibility(StudyGroupVisibility.PRIVATE);

            SubmitJoinRequestRequest request = new SubmitJoinRequestRequest("join message");

            when(studyGroupPostRepository.getStudyGroupPostsByIdOrThrow(post.getId()))
                    .thenReturn(post);

            assertThrows(ResourceNotFoundException.class, () -> cut.submitJoinRequest(2L, post.getId(), request));
            verify(joinRequestRepository, never()).save(any());
        }

        @Test
        @DisplayName("should fail when submitting request for own group post")
        public void ownGroupPostNowAllowed() {

            UserAccount user = new UserAccount();
            user.setId(2L);

            StudyGroupPost post = new StudyGroupPost();
            post.setId(1L);
            post.setCreator(user);
            post.setVisibility(StudyGroupVisibility.PUBLIC);

            SubmitJoinRequestRequest request = new SubmitJoinRequestRequest("join message");

            when(studyGroupPostRepository.getStudyGroupPostsByIdOrThrow(post.getId()))
                    .thenReturn(post);

            assertThrows(
                    JoinOwnGroupException.class,
                    () -> cut.submitJoinRequest(post.getCreator().getId(), post.getId(), request));
            verify(joinRequestRepository, never()).save(any());
        }

        @Test
        @DisplayName("should fail when submitting more than one request for the same group post")
        public void moreThanOneRequestNotAllowed() {

            UserAccount user = new UserAccount();
            user.setId(2L);

            StudyGroupPost post = new StudyGroupPost();
            post.setId(1L);
            post.setCreator(user);
            post.setVisibility(StudyGroupVisibility.PUBLIC);

            SubmitJoinRequestRequest request = new SubmitJoinRequestRequest("join message");

            when(studyGroupPostRepository.getStudyGroupPostsByIdOrThrow(post.getId()))
                    .thenReturn(post);

            when(joinRequestRepository.existsStudyGroupPostJoinRequestByUserIdAndPostId(4L, post.getId()))
                    .thenReturn(true);

            assertThrows(
                    JoinRequestAlreadyExistsException.class, () -> cut.submitJoinRequest(4L, post.getId(), request));
            verify(joinRequestRepository, never()).save(any());
        }

        @Test
        @DisplayName("should pass when submitting valid request")
        public void shouldSubmitRequestWhenValidRequest() {

            UserAccount user = new UserAccount();
            user.setId(2L);

            StudyGroupPost post = new StudyGroupPost();
            post.setId(1L);
            post.setCreator(user);
            post.setVisibility(StudyGroupVisibility.PUBLIC);

            SubmitJoinRequestRequest request = new SubmitJoinRequestRequest("join message");

            when(studyGroupPostRepository.getStudyGroupPostsByIdOrThrow(post.getId()))
                    .thenReturn(post);

            when(joinRequestRepository.existsStudyGroupPostJoinRequestByUserIdAndPostId(4L, post.getId()))
                    .thenReturn(false);

            when(joinRequestRepository.save(any(StudyGroupJoinRequest.class))).thenAnswer(invoc -> {
                StudyGroupJoinRequest requestToSave = invoc.getArgument(0);
                requestToSave.setId(10L);
                return requestToSave;
            });

            Long savedRequestId = cut.submitJoinRequest(4L, post.getId(), request);
            assertEquals(10L, savedRequestId);
        }
    }

    @Nested
    class CancelJoinRequestTest {
        @Test
        @DisplayName("should fail when user is not the owner")
        public void nonOwnerNotAllowed() {

            UserAccount user = new UserAccount();
            user.setId(2L);

            StudyGroupJoinRequest joinRequest = new StudyGroupJoinRequest();
            joinRequest.setId(1L);
            joinRequest.setUser(user);

            when(joinRequestRepository.findByPostIdAndIdOrThrow(1L, 1L)).thenReturn(joinRequest);

            assertThrows(ResourceNotFoundException.class, () -> cut.cancelJoinRequest(3L, 1L, 1L));
            verify(joinRequestRepository, never()).save(any());
        }

        @ParameterizedTest
        @EnumSource(
                value = JoinRequestStatus.class,
                names = {"CANCELLED", "APPROVED", "REJECTED"})
        @DisplayName("should fail when requested to cancel non-pending request")
        public void nonPendingNotAllowed(JoinRequestStatus nonPendingStatus) {

            UserAccount user = new UserAccount();
            user.setId(2L);

            StudyGroupJoinRequest joinRequest = new StudyGroupJoinRequest();
            joinRequest.setId(1L);
            joinRequest.setStatus(nonPendingStatus);
            joinRequest.setUser(user);

            when(joinRequestRepository.findByPostIdAndIdOrThrow(1L, 1L)).thenReturn(joinRequest);

            assertThrows(JoinRequestNotPendingException.class, () -> cut.cancelJoinRequest(2L, 1L, 1L));
            verify(joinRequestRepository, never()).save(any());
        }

        @Test
        @DisplayName("should pass when request is pending and request creator is the requestor")
        public void shouldCancelPendingRequestWhenUserIsOwner() {

            UserAccount user = new UserAccount();
            user.setId(2L);

            StudyGroupJoinRequest joinRequest = new StudyGroupJoinRequest();
            joinRequest.setId(1L);
            joinRequest.setStatus(JoinRequestStatus.PENDING);
            joinRequest.setUser(user);

            when(joinRequestRepository.findByPostIdAndIdOrThrow(1L, 1L)).thenReturn(joinRequest);

            cut.cancelJoinRequest(2L, 1L, 1L);

            assertEquals(JoinRequestStatus.CANCELLED, joinRequest.getStatus());
        }
    }

    @Nested
    class ApproveJoinRequestTest {
        @Test
        @DisplayName("should fail when request isn't coming from creator")
        public void nonCreatorNotAllowed() {

            UserAccount user = new UserAccount();
            user.setId(1L);

            StudyGroupPost post = new StudyGroupPost();
            post.setCreator(user);

            StudyGroupJoinRequest joinRequest = new StudyGroupJoinRequest();
            joinRequest.setId(1L);
            joinRequest.setStatus(JoinRequestStatus.PENDING);
            joinRequest.setPost(post);

            when(joinRequestRepository.findByPostIdAndIdOrThrow(1L, 1L)).thenReturn(joinRequest);

            assertThrows(ResourceNotFoundException.class, () -> cut.approveJoinRequest(2L, 1L, 1L));
            verify(joinRequestRepository, never()).save(any());
        }

        @Test
        @DisplayName("should fail when request is cancelled")
        public void requestCancelledNotAllowed() {

            UserAccount user = new UserAccount();
            user.setId(2L);

            StudyGroupPost post = new StudyGroupPost();
            post.setCreator(user);

            StudyGroupJoinRequest joinRequest = new StudyGroupJoinRequest();
            joinRequest.setId(1L);
            joinRequest.setStatus(JoinRequestStatus.CANCELLED);
            joinRequest.setPost(post);

            when(joinRequestRepository.findByPostIdAndIdOrThrow(1L, 1L)).thenReturn(joinRequest);

            assertThrows(JoinRequestCancelledException.class, () -> cut.approveJoinRequest(2L, 1L, 1L));
            verify(joinRequestRepository, never()).save(any());
        }

        @Test
        @DisplayName("should fail when request is already approved")
        public void requestAlreadyApprovedNotAllowed() {

            UserAccount user = new UserAccount();
            user.setId(2L);

            StudyGroupPost post = new StudyGroupPost();
            post.setCreator(user);

            StudyGroupJoinRequest joinRequest = new StudyGroupJoinRequest();
            joinRequest.setId(1L);
            joinRequest.setStatus(JoinRequestStatus.APPROVED);
            joinRequest.setPost(post);

            when(joinRequestRepository.findByPostIdAndIdOrThrow(1L, 1L)).thenReturn(joinRequest);

            assertThrows(JoinRequestAlreadyApprovedException.class, () -> cut.approveJoinRequest(2L, 1L, 1L));
            verify(joinRequestRepository, never()).save(any());
        }

        @Test
        @DisplayName("should fail when request is rejected")
        public void requestRejectedNotAllowed() {

            UserAccount user = new UserAccount();
            user.setId(2L);

            StudyGroupPost post = new StudyGroupPost();
            post.setCreator(user);

            StudyGroupJoinRequest joinRequest = new StudyGroupJoinRequest();
            joinRequest.setId(1L);
            joinRequest.setStatus(JoinRequestStatus.REJECTED);
            joinRequest.setPost(post);

            when(joinRequestRepository.findByPostIdAndIdOrThrow(1L, 1L)).thenReturn(joinRequest);

            assertThrows(JoinRequestAlreadyRejectedException.class, () -> cut.approveJoinRequest(2L, 1L, 1L));
            verify(joinRequestRepository, never()).save(any());
        }

        @Test
        @DisplayName("should pass when request is pending and request coming from post creator")
        public void shouldApproveRequestWhenRequesterIsCreator() {

            UserAccount user = new UserAccount();
            user.setId(2L);

            StudyGroupPost post = new StudyGroupPost();
            post.setCreator(user);

            StudyGroupJoinRequest joinRequest = new StudyGroupJoinRequest();
            joinRequest.setId(1L);
            joinRequest.setStatus(JoinRequestStatus.PENDING);
            joinRequest.setPost(post);

            when(joinRequestRepository.findByPostIdAndIdOrThrow(1L, 1L)).thenReturn(joinRequest);

            cut.approveJoinRequest(2L, 1L, 1L);
            assertEquals(JoinRequestStatus.APPROVED, joinRequest.getStatus());
        }
    }

    @Nested
    class RejectJoinRequestTest {
        @Test
        @DisplayName("should fail when request isn't coming from creator")
        public void nonCreatorNotAllowed() {

            UserAccount user = new UserAccount();
            user.setId(1L);

            StudyGroupPost post = new StudyGroupPost();
            post.setCreator(user);

            StudyGroupJoinRequest joinRequest = new StudyGroupJoinRequest();
            joinRequest.setId(1L);
            joinRequest.setStatus(JoinRequestStatus.PENDING);
            joinRequest.setPost(post);

            when(joinRequestRepository.findByPostIdAndIdOrThrow(1L, 1L)).thenReturn(joinRequest);

            assertThrows(ResourceNotFoundException.class, () -> cut.rejectJoinRequest(2L, 1L, 1L));
            verify(joinRequestRepository, never()).save(any());
        }

        @Test
        @DisplayName("should fail when request is cancelled")
        public void requestCancelledNotAllowed() {

            UserAccount user = new UserAccount();
            user.setId(2L);

            StudyGroupPost post = new StudyGroupPost();
            post.setCreator(user);

            StudyGroupJoinRequest joinRequest = new StudyGroupJoinRequest();
            joinRequest.setId(1L);
            joinRequest.setStatus(JoinRequestStatus.CANCELLED);
            joinRequest.setPost(post);

            when(joinRequestRepository.findByPostIdAndIdOrThrow(1L, 1L)).thenReturn(joinRequest);

            assertThrows(JoinRequestCancelledException.class, () -> cut.rejectJoinRequest(2L, 1L, 1L));
            verify(joinRequestRepository, never()).save(any());
        }

        @Test
        @DisplayName("should fail when request is approved")
        public void requestApprovedNotAllowed() {

            UserAccount user = new UserAccount();
            user.setId(2L);

            StudyGroupPost post = new StudyGroupPost();
            post.setCreator(user);

            StudyGroupJoinRequest joinRequest = new StudyGroupJoinRequest();
            joinRequest.setId(1L);
            joinRequest.setStatus(JoinRequestStatus.APPROVED);
            joinRequest.setPost(post);

            when(joinRequestRepository.findByPostIdAndIdOrThrow(1L, 1L)).thenReturn(joinRequest);

            assertThrows(JoinRequestAlreadyApprovedException.class, () -> cut.rejectJoinRequest(2L, 1L, 1L));
            verify(joinRequestRepository, never()).save(any());
        }

        @Test
        @DisplayName("should fail when request is already rejected")
        public void requestAlreadyRejectedNotAllowed() {

            UserAccount user = new UserAccount();
            user.setId(2L);

            StudyGroupPost post = new StudyGroupPost();
            post.setCreator(user);

            StudyGroupJoinRequest joinRequest = new StudyGroupJoinRequest();
            joinRequest.setId(1L);
            joinRequest.setStatus(JoinRequestStatus.REJECTED);
            joinRequest.setPost(post);

            when(joinRequestRepository.findByPostIdAndIdOrThrow(1L, 1L)).thenReturn(joinRequest);

            assertThrows(JoinRequestAlreadyRejectedException.class, () -> cut.rejectJoinRequest(2L, 1L, 1L));
            verify(joinRequestRepository, never()).save(any());
        }

        @Test
        @DisplayName("should pass when request is pending and request coming from post creator")
        public void shouldApproveRequestWhenRequesterIsCreator() {

            UserAccount user = new UserAccount();
            user.setId(2L);

            StudyGroupPost post = new StudyGroupPost();
            post.setCreator(user);

            StudyGroupJoinRequest joinRequest = new StudyGroupJoinRequest();
            joinRequest.setId(1L);
            joinRequest.setStatus(JoinRequestStatus.PENDING);
            joinRequest.setPost(post);

            when(joinRequestRepository.findByPostIdAndIdOrThrow(1L, 1L)).thenReturn(joinRequest);

            cut.rejectJoinRequest(2L, 1L, 1L);
            assertEquals(JoinRequestStatus.REJECTED, joinRequest.getStatus());
        }
    }
}
