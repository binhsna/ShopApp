package com.binhnc.shopapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("quantity")
    private Integer quantity;
}
