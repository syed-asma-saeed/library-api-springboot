package com.library.dto.request;

import com.library.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                   // getters + setters + toString + equals + hashCode
@NoArgsConstructor
@AllArgsConstructor

public class RegisterRequest{

    @NotBlank(message = "Name is required")
    @Schema(description = "User name", example = "John Doe")
    private String name;

    @Email
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Schema(description = "Email", example = "jhondoe@email.com")
    private String email;

    @NotNull(message = "Role is required")
    @Schema(description = "User Type", example = "USER")
    private Role role;

    @NotBlank(message = "Password is required")
    @Schema(description = "Password", example = "********")
    private String password;

}