package com.binhnc.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @JsonProperty("fullname")// Tham số client gửi lên
    private String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;


    @JsonProperty("email")
    private String email = "";

    private String address;

    @NotBlank(message = "Password cannot is blank")
    private String password;

    @JsonProperty("retype_password")
    private String retypePassword;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @JsonProperty("facebook_account_id")
    private int facebookAccountId;

    @JsonProperty("google_account_id")
    private int googleAccountId;

    @NotNull(message = "Role ID is required")
    @JsonProperty("role_id")
    private Long roleId;
}

