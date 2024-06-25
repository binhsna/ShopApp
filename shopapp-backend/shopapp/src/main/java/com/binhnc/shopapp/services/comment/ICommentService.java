package com.binhnc.shopapp.services.comment;

import com.binhnc.shopapp.dtos.CommentDTO;
import com.binhnc.shopapp.exceptions.DataNotFoundException;
import com.binhnc.shopapp.models.Comment;
import com.binhnc.shopapp.responses.CommentResponse;

import java.util.List;

public interface ICommentService {
    Comment insertComment(CommentDTO commentDTO);

    void deleteComment(Long commentId);

    void updateComment(Long id, CommentDTO commentDTO) throws DataNotFoundException;

    List<CommentResponse> getCommentByUserAndProduct(Long userId, Long productId);

    List<CommentResponse> getCommentByProduct(Long productId);
}
