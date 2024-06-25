package com.binhnc.shopapp.responses.product;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductListResponse {
    private List<ProductResponse> products;
    private int totalPages;
}
