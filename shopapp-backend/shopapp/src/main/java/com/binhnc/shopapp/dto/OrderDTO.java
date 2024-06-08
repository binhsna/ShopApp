package com.binhnc.shopapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data // toString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    @Min(value = 1, message = "User's ID must be > 0")
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("fullname")
    private String fullName; // Không yêu cầu validate

    @JsonProperty("email")
    private String email; // Không yêu cầu validate

    @NotBlank(message = "Phone number is required")
    @Size(min = 5, message = "Phone number must be at last 5 characters")
    @JsonProperty("phone_number")
    private String phoneNumber; // Quan trọng

    private String address; // Không yêu cầu validate

    private String note;

    @Min(value = 0, message = "Total money must be >= 0")
    @JsonProperty("total_money")
    private Float totalMoney;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("payment_method")
    private String paymentMethod;

    // status, mặc định là PENDING
}
