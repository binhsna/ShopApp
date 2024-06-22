package com.binhnc.shopapp.services.product;

import com.binhnc.shopapp.dtos.ProductDTO;
import com.binhnc.shopapp.dtos.ProductImageDTO;
import com.binhnc.shopapp.exceptions.DataNotFoundException;
import com.binhnc.shopapp.exceptions.InvalidParamException;
import com.binhnc.shopapp.models.Category;
import com.binhnc.shopapp.models.Product;
import com.binhnc.shopapp.models.ProductImage;
import com.binhnc.shopapp.repositories.CategoryRepository;
import com.binhnc.shopapp.repositories.ProductImageRepository;
import com.binhnc.shopapp.repositories.ProductRepository;
import com.binhnc.shopapp.responses.ProductResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO) {
        try {
            Category existingCategory = categoryRepository
                    .findById(productDTO.getCategoryId())
                    .orElseThrow(() ->
                            new DataNotFoundException(
                                    "Cannot find category with id: " + productDTO.getCategoryId()));
            Product newProduct = Product.builder()
                    .name(productDTO.getName())
                    .price(productDTO.getPrice())
                    .thumbnail(productDTO.getThumbnail())
                    .description(productDTO.getDescription())
                    .category(existingCategory)
                    .build();
            return productRepository.save(newProduct);
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product getProductById(Long id) throws Exception {
        return productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find product id with id = " + id));
    }

    @Override
    public List<Product> findProductsByIds(List<Long> productIds) {
        return productRepository.findProductsByIds(productIds);
    }

    @Override
    public Page<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest) {
        // Lấy danh sách sản phẩm theo trang (page) và giới hạn (limit), và categoryId (nếu có)
        Page<Product> productsPage;
        productsPage = productRepository.searchProducts(categoryId, keyword, pageRequest);
        return productsPage.map(product -> ProductResponse.fromProduct(product));
        // return productRepository.findAll(pageRequest).map(ProductResponse::fromProduct);
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, ProductDTO productDTO) throws Exception {
        Product existingProduct = getProductById(id);
        if (existingProduct != null) {
            // Copy các thuộc tính từ DTO -> Product
            // Có thể sử dụng ObjectMapper hoặc ModelMapper
            Category existingCategory = categoryRepository
                    .findById(productDTO.getCategoryId())
                    .orElseThrow(() ->
                            new DataNotFoundException(
                                    "Cannot find category with id: " + productDTO.getCategoryId()));
            if (productDTO.getName() != null &&
                    !productDTO.getName().isEmpty()) {
                existingProduct.setName(productDTO.getName());
            }
            existingProduct.setCategory(existingCategory);
            if (productDTO.getPrice() >= 0) {
                existingProduct.setPrice(productDTO.getPrice());
            }
            if (productDTO.getDescription() != null &&
                    !productDTO.getDescription().isEmpty()) {
                existingProduct.setDescription(productDTO.getDescription());
            }
            if (productDTO.getThumbnail() != null &&
                    !productDTO.getThumbnail().isEmpty()) {
                existingProduct.setThumbnail(productDTO.getThumbnail());
            }
            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    @Transactional
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception {
        Product existingProduct = productRepository
                .findById(productId)
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find product with id: " + productImageDTO.getProductId()));
        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        // Không cho insert quá 5 ảnh cho 1 sản phẩm
        int size = productImageRepository.findByProductId(existingProduct.getId()).size();
        if (size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new InvalidParamException(
                    "Number of images must be <= "
                            + ProductImage.MAXIMUM_IMAGES_PER_PRODUCT);
        }
        return productImageRepository.save(newProductImage);
    }
}
