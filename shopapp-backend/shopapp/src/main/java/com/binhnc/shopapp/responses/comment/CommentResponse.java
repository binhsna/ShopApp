package com.binhnc.shopapp.responses.comment;

import com.binhnc.shopapp.models.Comment;
import com.binhnc.shopapp.responses.user.UserResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    @JsonProperty("content")
    private String content;

    @JsonProperty("user")
    private UserResponse userResponse;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public static CommentResponse fromComment(Comment comment) {
        return CommentResponse.builder()
                .content(comment.getContent())
                .userResponse(UserResponse.fromUser(comment.getUser()))
                .updatedAt(comment.getUpdateAt())
                .build();
    }
}
