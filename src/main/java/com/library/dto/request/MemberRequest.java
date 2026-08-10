package com.library.dto.request;

import com.library.enums.MemberType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {
    private String name;
    private String email;
    private MemberType memberType;
}