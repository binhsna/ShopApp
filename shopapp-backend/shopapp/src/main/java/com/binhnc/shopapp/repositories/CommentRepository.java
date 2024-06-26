package com.binhnc.shopapp.repositories;

import com.binhnc.shopapp.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUserIdAndProductId(
            @Param("userId") Long userId,
            @Param("productId") Long productId);

    List<Comment> findByProductId(@Param("productId") Long productId);
}
