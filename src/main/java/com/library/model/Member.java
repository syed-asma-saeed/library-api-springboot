package com.library.model;

import com.library.enums.MemberType;
import com.library.model.BorrowRecord;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Member{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberType memberType;

    @Column(name = "current_borrow_count")
    private int currentBorrowCount = 0;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<BorrowRecord> borrowRecords = new ArrayList<>();

    public boolean canBorrow(){
        return currentBorrowCount < memberType.getBorrowLimit();
    }

    public int incrementBorrowCount(){
        return currentBorrowCount++;
    }

    public int decrementBorrowCount(){
        return currentBorrowCount--;
    }
}