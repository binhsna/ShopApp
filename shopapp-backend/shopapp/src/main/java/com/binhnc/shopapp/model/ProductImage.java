package com.binhnc.shopapp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_images")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "image_url", length = 300)
    private String imageUrl;
}
