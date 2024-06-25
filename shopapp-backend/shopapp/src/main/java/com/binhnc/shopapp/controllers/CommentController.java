package com.binhnc.shopapp.controllers;

import com.binhnc.shopapp.dtos.CommentDTO;
import com.binhnc.shopapp.models.User;
import com.binhnc.shopapp.responses.comment.CommentResponse;
import com.binhnc.shopapp.services.comment.ICommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("${api.prefix}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final ICommentService commentService;

    @GetMapping("")
    public ResponseEntity<List<CommentResponse>> getAllComments(
            @RequestParam(value = "user_id", required = false) Long userId,
            @RequestParam("product_id") Long productId
    ) {
        List<CommentResponse> commentResponses;
        if (userId == null) {
            commentResponses = commentService.getCommentByProduct(productId);
        } else {
            commentResponses = commentService.getCommentByUserAndProduct(userId, productId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(commentResponses);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> insertComment(
            @Valid @RequestBody CommentDTO commentDTO
    ) {
        try {
            User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!Objects.equals(loginUser.getId(), commentDTO.getUserId())) {
                return ResponseEntity.badRequest().body("You cannot comment as other user");
            }
            commentService.insertComment(commentDTO);
            return ResponseEntity.ok("Insert comment successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred during comment insertion.");
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> updateComment(
            @PathVariable("id") Long commentId,
            @Valid @RequestBody CommentDTO commentDTO
    ) {
        try {
            User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!Objects.equals(loginUser.getId(), commentDTO.getUserId())) {
                return ResponseEntity.badRequest().body("You cannot update other user's comment");
            }
            commentService.updateComment(commentId, commentDTO);
            return ResponseEntity.ok("Update comment successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred during comment update.");
        }
    }

}
