package com.library.enums;

import lombok.Getter;

@Getter
public enum MemberType{
    STUDENT(3),
    FACULTY(10);

    private final int borrowLimit;
    MemberType(int borrowLimit){
        this.borrowLimit = borrowLimit;
    }

}