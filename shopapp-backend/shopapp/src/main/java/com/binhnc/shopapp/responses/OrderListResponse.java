package com.binhnc.shopapp.responses;

import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderListResponse {
    private List<OrderResponse> orders;
    private int totalPages;
}
