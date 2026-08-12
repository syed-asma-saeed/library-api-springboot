package com.library.service;

import com.library.dto.request.BookRequest;
import com.library.dto.response.BookResponse;
import com.library.dto.response.PageResponse;
import com.library.exception.BookNotFoundException;
import com.library.exception.InvalidSortFieldException;
import com.library.model.Book;
import com.library.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService{

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    public PageResponse<BookResponse> getAllBooks(int page, int size, String sortBy, String sortDir){

        // Prevent invalid sort fields that could cause DB errors
        List<String> allowedSortFields = List.of("title", "author", "genre", "totalCopies");

        if (!allowedSortFields.contains(sortBy)) {
            throw new InvalidSortFieldException("Cannot sort by: " + sortBy + ". Allowed: " + allowedSortFields);
        }

        if (size > 100) size = 100; // cap maximum page size
        if (size < 1) size = 10;

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Book> bookPage = bookRepository.findAll(pageable);

        Page<BookResponse> responsePage = bookPage.map(this::toResponse);

        return toPageResponse(responsePage);
    }

    public BookResponse getBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found: " + id));

        return toResponse(book);
    }

    public BookResponse addBook(BookRequest request){
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setGenre(request.getGenre());
        book.setTotalCopies(request.getTotalCopies());
        book.setAvailableCopies(request.getTotalCopies()); // starts fully available
        Book saved = bookRepository.save(book);
        return toResponse(saved);
    }

    public BookResponse updateBook(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found: " + id));
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setGenre(request.getGenre());
        book.setTotalCopies(request.getTotalCopies());
        Book saved = bookRepository.save(book);
        return toResponse(saved);
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id))
            throw new BookNotFoundException("Book not found: " + id);
        bookRepository.deleteById(id);
    }

    public PageResponse<BookResponse> searchByTitle(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        return toPageResponse(bookRepository.findByTitleContainingIgnoreCase(keyword, pageable).map(this::toResponse));
    }

    private BookResponse toResponse(Book book){
        BookResponse response = new BookResponse();
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        response.setGenre(book.getGenre().getDisplayName());
        response.setTotalCopies(book.getTotalCopies());
        response.setAvailableCopies(book.getAvailableCopies());
        return response;
    }

    private <T> PageResponse<T> toPageResponse(Page<T> page) {
        PageResponse<T> response = new PageResponse<>();

        response.setContent(page.getContent());
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setFirst(page.isFirst());
        response.setLast(page.isLast());
        response.setHasNext(page.hasNext());

        return response;
    }
}