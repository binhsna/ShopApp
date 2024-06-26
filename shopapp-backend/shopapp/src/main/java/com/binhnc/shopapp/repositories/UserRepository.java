package com.binhnc.shopapp.repositories;

import com.binhnc.shopapp.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    // SELECT * FROM users WHERE phoneNumber=?
    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByEmail(String email);

    @Query("SELECT o FROM User o WHERE (:keyword IS NULL OR :keyword = '' " +
            "OR o.fullName LIKE %:keyword% OR o.address LIKE %:keyword%" +
            "OR o.phoneNumber LIKE %:keyword%)" +
            "AND LOWER(o.role.name) = 'user'")
    Page<User> findAll(@Param("keyword") String keyword, Pageable pageable);

}
