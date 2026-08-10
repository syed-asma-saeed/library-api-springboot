package com.library.enums;

import lombok.Getter;

@Getter
public enum MemberType{
    STUDENT(3, 14),
    FACULTY(10, 30);

    private final int borrowLimit;
    private final int dueDays;

    MemberType(int borrowLimit, int dueDays) {
        this.borrowLimit = borrowLimit;
        this.dueDays = dueDays;
    }
}