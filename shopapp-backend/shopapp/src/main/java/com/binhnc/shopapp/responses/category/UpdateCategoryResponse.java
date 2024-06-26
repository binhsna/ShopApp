package com.binhnc.shopapp.responses.category;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCategoryResponse {
    @JsonProperty("message")
    private String message;
}
