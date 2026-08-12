package com.library.controller;

import com.library.dto.request.BookRequest;
import com.library.dto.response.BookResponse;
import com.library.dto.response.PageResponse;
import com.library.service.BookService;
import jakarta.validation.Valid;
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
    public ResponseEntity<PageResponse<BookResponse>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        return ResponseEntity.ok(
                bookService.getAllBooks(page, size, sortBy, sortDir));
    }

    // @RequestParam(defaultValue = "0") means:
    // if client doesn't send ?page=X, use 0
    // client can override: GET /api/books?page=2&size=5&sortBy=author&sortDir=desc

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBook(id));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<BookResponse>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(bookService.searchByTitle(keyword, page, size));
    }

    @PostMapping
    public ResponseEntity<BookResponse> addBook(@Valid @RequestBody BookRequest request) {
        return ResponseEntity.status(201).body(bookService.addBook(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@Valid
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