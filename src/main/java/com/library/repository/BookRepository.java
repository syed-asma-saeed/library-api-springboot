package com.library.repository;

import com.library.enums.Genre;
import com.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Spring Data generates SQL just from method names — no implementation needed
    List<Book> findByAuthor(String author);
    List<Book> findByGenre(Genre genre);
    List<Book> findByTitleContainingIgnoreCase(String keyword);
    List<Book> findByAvailableCopiesGreaterThan(int copies);
}


/*
JpaRepository<Book, Long> gives you these for free:
    repository.save(book)          // INSERT if new, UPDATE if exists
    repository.findById(id)        // returns Optional<Book>
    repository.findAll()           // SELECT * FROM books
    repository.deleteById(id)      // DELETE WHERE id = ?
    repository.existsById(id)      // SELECT EXISTS
    repository.count()             // SELECT COUNT(*)

You write zero SQL for these. Spring Data generates it at startup.
 */