package com.library.controller;

import com.library.dto.request.BookRequest;
import com.library.dto.response.BookResponse;
import com.library.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBook(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookResponse>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(bookService.searchByTitle(keyword));
    }

    @PostMapping
    public ResponseEntity<BookResponse> addBook(@RequestBody BookRequest request) {
        return ResponseEntity.status(201).body(bookService.addBook(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id,
            @RequestBody BookRequest request) {
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}