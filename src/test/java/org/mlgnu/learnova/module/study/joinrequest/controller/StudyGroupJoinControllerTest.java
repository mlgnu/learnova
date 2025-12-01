package org.mlgnu.learnova.module.study.joinrequest.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mlgnu.learnova.common.exception.ResourceNotFoundException;
import org.mlgnu.learnova.config.SecurityConfig;
import org.mlgnu.learnova.module.study.joinrequest.contrtoller.StudyGroupJoinController;
import org.mlgnu.learnova.module.study.joinrequest.exception.*;
import org.mlgnu.learnova.module.study.joinrequest.service.StudyGroupJoinService;
import org.mlgnu.learnova.support.security.WithMockJwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudyGroupJoinController.class)
@Import({SecurityConfig.class})
@WithMockJwtUser
public class StudyGroupJoinControllerTest {

    @MockitoBean
    private StudyGroupJoinService joinService;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    class SubmitJoinRequestTest {

        @Test
        @WithAnonymousUser
        @DisplayName("should reject requests when use isn't authenticated")
        public void noAuthenticationNotAllowed() throws Exception {

            String request = """
                {
                "message": "test message"
                }
                """;

            mockMvc.perform(post("/posts/{postId}/join-requests", 2L)
                            .content(request)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("should fail when message exceeds max length")
        public void tooLongMessageNotAllowed() throws Exception {

            String longMessage = "a".repeat(2001);
            String request = """
                {
                "message": "%s"
                }
                """.formatted(longMessage);

            mockMvc.perform(post("/posts/{postId}/join-requests", 2L)
                            .contentType(APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("Bad Request"))
                    .andExpect(jsonPath("$.errors.message").value("Message should be less than 2K characters"))
                    .andExpect(jsonPath("$.path").value("/posts/2/join-requests"));
        }

        @Test
        @DisplayName("should pass when request is correct")
        public void shouldPassWhenCorrectRequest() throws Exception {

            String request = """
                {
                "message": "test message"
                }
                """;

            when(joinService.submitJoinRequest(anyLong(), anyLong(), any())).thenReturn(2L);

            mockMvc.perform(post("/posts/{postId}/join-requests", 2L)
                            .content(request)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("Location"))
                    .andExpect(header().string("Location", containsString("/posts/2/join-requests")));
        }
    }

    @Nested
    class CancelJoinRequestTest {

        @Test
        @WithAnonymousUser
        @DisplayName("should reject requests when user isn't authenticated")
        public void notAuthenticatedNotAllowed() throws Exception {
            mockMvc.perform(delete("/posts/{postId}/join-requests/{requestId}", 1L, 2L))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("should pass when user is authenticated")
        public void shouldPassWhenAuthenticated() throws Exception {
            doNothing().when(joinService).cancelJoinRequest(anyLong(), anyLong(), anyLong());

            mockMvc.perform(delete("/posts/{postId}/join-requests/{requestId}", 2L, 3L))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    class ApproveJoinRequestTest {

        @Test
        @WithAnonymousUser
        @DisplayName("should reject requests when user isn't authenticated")
        public void notAuthenticatedNotAllowed() throws Exception {
            mockMvc.perform(patch("/posts/{postId}/join-requests/{requestId}/approve", 1L, 2L))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("should pass when user is authenticated")
        public void shouldPassWhenAuthenticated() throws Exception {
            doNothing().when(joinService).approveJoinRequest(anyLong(), anyLong(), anyLong());

            mockMvc.perform(patch("/posts/{postId}/join-requests/{requestId}/approve", 2L, 3L))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    class RejectJoinRequestTest {

        @Test
        @WithAnonymousUser
        @DisplayName("should reject requests when user isn't authenticated")
        public void notAuthenticatedNotAllowed() throws Exception {
            mockMvc.perform(patch("/posts/{postId}/join-requests/{requestId}/reject", 1L, 2L))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("should pass when user is authenticated")
        public void shouldPassWhenAuthenticated() throws Exception {
            doNothing().when(joinService).approveJoinRequest(anyLong(), anyLong(), anyLong());

            mockMvc.perform(patch("/posts/{postId}/join-requests/{requestId}/reject", 2L, 3L))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    class DomainExceptionTest {

        @Test
        @DisplayName("resource not found test")
        public void resourceNotFoundTest() throws Exception {

            doThrow(new ResourceNotFoundException("Join request", 3L))
                    .when(joinService)
                    .cancelJoinRequest(anyLong(), anyLong(), anyLong());

            mockMvc.perform(delete("/posts/{postId}/join-requests/{requestId}", 2L, 3L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.error").value("Not Found"))
                    .andExpect(jsonPath("$.message").value("Join request with id 3 was not found"))
                    .andExpect(jsonPath("$.path").value("/posts/2/join-requests/3"));
        }

        @Test
        @DisplayName("join own group test")
        public void joinOwnGroupTest() throws Exception {
            doThrow(new JoinOwnGroupException(3L)).when(joinService).cancelJoinRequest(anyLong(), anyLong(), anyLong());

            mockMvc.perform(delete("/posts/{postId}/join-requests/{requestId}", 2L, 3L))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("Bad Request"))
                    .andExpect(jsonPath("$.message").value("User with id 3 cannot join their own group"))
                    .andExpect(jsonPath("$.path").value("/posts/2/join-requests/3"));
        }

        @Test
        @DisplayName("join request already exists")
        public void joinRequestAlreadyExistsTest() throws Exception {
            doThrow(new JoinRequestAlreadyExistsException(3L))
                    .when(joinService)
                    .cancelJoinRequest(anyLong(), anyLong(), anyLong());

            mockMvc.perform(delete("/posts/{postId}/join-requests/{requestId}", 2L, 3L))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.status").value(409))
                    .andExpect(jsonPath("$.error").value("Conflict"))
                    .andExpect(jsonPath("$.message").value("Join request already exists for user 3"))
                    .andExpect(jsonPath("$.path").value("/posts/2/join-requests/3"));
        }

        @Test
        @DisplayName("join request not pending")
        public void joinRequestNotPendingTest() throws Exception {
            doThrow(new JoinRequestNotPendingException(3L))
                    .when(joinService)
                    .cancelJoinRequest(anyLong(), anyLong(), anyLong());

            mockMvc.perform(delete("/posts/{postId}/join-requests/{requestId}", 2L, 3L))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("Bad Request"))
                    .andExpect(jsonPath("$.message").value("Join request is not pending: 3"))
                    .andExpect(jsonPath("$.path").value("/posts/2/join-requests/3"));
        }

        @Test
        @DisplayName("join request already rejected")
        public void joinRequestAlreadyRejectedTest() throws Exception {
            doThrow(new JoinRequestAlreadyRejectedException(3L))
                    .when(joinService)
                    .cancelJoinRequest(anyLong(), anyLong(), anyLong());

            mockMvc.perform(delete("/posts/{postId}/join-requests/{requestId}", 2L, 3L))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("Bad Request"))
                    .andExpect(jsonPath("$.message").value("Join request already rejected: 3"))
                    .andExpect(jsonPath("$.path").value("/posts/2/join-requests/3"));
        }

        @Test
        @DisplayName("join request already approved")
        public void joinRequestAlreadyApprovedTest() throws Exception {
            doThrow(new JoinRequestAlreadyApprovedException(3L))
                    .when(joinService)
                    .cancelJoinRequest(anyLong(), anyLong(), anyLong());

            mockMvc.perform(delete("/posts/{postId}/join-requests/{requestId}", 2L, 3L))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("Bad Request"))
                    .andExpect(jsonPath("$.message").value("Join request already approved: 3"))
                    .andExpect(jsonPath("$.path").value("/posts/2/join-requests/3"));
        }

        @Test
        @DisplayName("join request cancelled")
        public void joinRequestCancelledTest() throws Exception {
            doThrow(new JoinRequestCancelledException(3L))
                    .when(joinService)
                    .cancelJoinRequest(anyLong(), anyLong(), anyLong());

            mockMvc.perform(delete("/posts/{postId}/join-requests/{requestId}", 2L, 3L))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("Bad Request"))
                    .andExpect(jsonPath("$.message").value("Join request is cancelled: 3"))
                    .andExpect(jsonPath("$.path").value("/posts/2/join-requests/3"));
        }
    }
}
