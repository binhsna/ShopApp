package com.binhnc.shopapp.services.product;

import com.binhnc.shopapp.responses.ProductResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductRedisService {
    // Clear cached data in Redis
    void clear();

    List<ProductResponse> getAllProducts(
            String keyword,
            Long categoryId,
            PageRequest pageRequest
    ) throws JsonProcessingException;

    void saveAllProducts(List<ProductResponse> productResponses,
                         String keyword,
                         Long categoryId,
                         PageRequest pageRequest
    ) throws JsonProcessingException;
}
