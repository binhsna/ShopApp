package com.binhnc.shopapp.responses.user;

import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserListResponse {
    private List<UserResponse> users;
    private int totalPages;
}
