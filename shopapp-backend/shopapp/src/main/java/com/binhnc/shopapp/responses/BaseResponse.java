package com.binhnc.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseResponse implements Serializable {
    @JsonProperty("created_at")
    private LocalDateTime createAt;

    @JsonProperty("updated_at")
    private LocalDateTime updateAt;
}
