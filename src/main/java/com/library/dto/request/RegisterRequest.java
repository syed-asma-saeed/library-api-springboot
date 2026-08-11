package com.library.dto.request;

import com.library.enums.Genre;
import com.library.enums.Role;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
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
    private String name;

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @NotNull(message = "Role is required")
    private Role role;

    @NotNull(message = "Password is required")
    private String password;

}