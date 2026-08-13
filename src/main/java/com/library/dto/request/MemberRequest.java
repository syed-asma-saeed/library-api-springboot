package com.library.dto.request;

import com.library.enums.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {

    @NotBlank(message = "Name is required")
    @Schema(description = "Member name", example = "John Doe")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Schema(description = "Email", example = "jhondoe@email.com")
    private String email;

    @NotNull(message = "Member Type is required")
    @Schema(description = "Member Type", example = "STUDENT")
    private MemberType memberType;
}