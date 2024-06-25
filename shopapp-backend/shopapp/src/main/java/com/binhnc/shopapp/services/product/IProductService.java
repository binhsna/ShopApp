package com.binhnc.shopapp.services.product;


import com.binhnc.shopapp.dtos.ProductDTO;
import com.binhnc.shopapp.dtos.ProductImageDTO;
import com.binhnc.shopapp.models.Product;
import com.binhnc.shopapp.models.ProductImage;
import com.binhnc.shopapp.responses.product.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductService {
    Product createProduct(ProductDTO productDTO);

    Product getProductById(Long id) throws Exception;

    List<Product> findProductsByIds(List<Long> productIds);

    Page<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest);

    Product updateProduct(Long id, ProductDTO productDTO) throws Exception;

    void deleteProduct(Long id);

    boolean existsByName(String name);

    ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception;
}
