package com.library.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    @Schema(example = "1") private Long id;
    @Schema(example = "Rahul Sharma") private String name;
    @Schema(example = "rahul@email.com") private String email;
    @Schema(example = "STUDENT") private String memberType;
    @Schema(example = "2") private int currentBorrowCount;
    @Schema(example = "3") private int borrowLimit;    // from enum
}