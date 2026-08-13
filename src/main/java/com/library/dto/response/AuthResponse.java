package com.library.dto.response;

import com.library.enums.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AuthResponse{

    @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc21...")
    private String token;
}