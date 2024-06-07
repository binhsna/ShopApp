package com.binhnc.shopapp.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data // toString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    @NotEmpty(message = "Category's name cannot be empty")
    private String name;
}
