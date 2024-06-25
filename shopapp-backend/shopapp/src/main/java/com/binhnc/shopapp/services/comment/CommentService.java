package com.binhnc.shopapp.services.comment;

import com.binhnc.shopapp.dtos.CommentDTO;
import com.binhnc.shopapp.exceptions.DataNotFoundException;
import com.binhnc.shopapp.models.Comment;
import com.binhnc.shopapp.models.Product;
import com.binhnc.shopapp.models.User;
import com.binhnc.shopapp.repositories.CommentRepository;
import com.binhnc.shopapp.repositories.ProductRepository;
import com.binhnc.shopapp.repositories.UserRepository;
import com.binhnc.shopapp.responses.comment.CommentResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Comment insertComment(CommentDTO commentDTO) {
        User user = userRepository.findById(commentDTO.getUserId()).orElse(null);
        Product product = productRepository.findById(commentDTO.getProductId()).orElse(null);

        if (user == null || product == null) {
            throw new IllegalArgumentException("User or product not found.");
        }

        Comment newComment = Comment.builder()
                .user(user)
                .product(product)
                .content(commentDTO.getContent())
                .build();
        return commentRepository.save(newComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public void updateComment(Long id, CommentDTO commentDTO) throws DataNotFoundException {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Comment not found."));
        existingComment.setContent(commentDTO.getContent());
        commentRepository.save(existingComment);
    }

    @Override
    public List<CommentResponse> getCommentByUserAndProduct(Long userId, Long productId) {
        List<Comment> comments = commentRepository.findByUserIdAndProductId(userId, productId);
        return comments.stream().map(comment -> CommentResponse.fromComment(comment)).collect(Collectors.toList());
    }

    @Override
    public List<CommentResponse> getCommentByProduct(Long productId) {
        List<Comment> comments = commentRepository.findByProductId(productId);
        return comments.stream().map(CommentResponse::fromComment).collect(Collectors.toList());
    }

}
