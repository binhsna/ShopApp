package com.binhnc.shopapp.responses.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("token")
    private String token;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("roles")
    private List<String> roles;
}
