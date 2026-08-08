package com.library.model;

import com.library.enums.Genre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity     //this class maps to a database table
@Table(name = "books")  //table will be named "books"
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Book {

    @Id //this field is the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // MySQL auto-increments this (AUTO_INCREMENT)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genre genre;

    @Column(name = "total_copies", nullable = false)  //column name in DB (snake_case convention)
    private int totalCopies;

    @Column(name = "available_copies", nullable = false)
    private int availableCopies;

}