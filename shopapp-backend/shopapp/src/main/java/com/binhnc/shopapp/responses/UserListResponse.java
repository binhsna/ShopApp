package com.binhnc.shopapp.responses;

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
