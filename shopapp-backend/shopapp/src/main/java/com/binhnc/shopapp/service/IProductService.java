package com.binhnc.shopapp.service;


import com.binhnc.shopapp.dto.ProductDTO;
import com.binhnc.shopapp.dto.ProductImageDTO;
import com.binhnc.shopapp.model.Product;
import com.binhnc.shopapp.model.ProductImage;
import com.binhnc.shopapp.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
    Product createProduct(ProductDTO productDTO);

    Product getProductById(Long id) throws Exception;

    Page<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest);

    Product updateProduct(Long id, ProductDTO productDTO) throws Exception;

    void deleteProduct(Long id);

    boolean existsByName(String name);

    ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception;
}
