package com.library.service;

import com.library.dto.request.BookRequest;
import com.library.dto.response.BookResponse;
import com.library.enums.Genre;
import com.library.exception.BookNotFoundException;
import com.library.model.Book;
import com.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // activates Mockito annotations
class BookServiceTest {

    @Mock
    BookRepository bookRepository;
    // Creates a fake BookRepository
    // All methods return null/empty by default
    // You control behavior with when()

    @InjectMocks
    BookService bookService;
    // Creates a real BookService
    // Injects the @Mock bookRepository into it
    // So bookService uses your fake repository

    private Book testBook;
    private BookRequest testRequest;

    @BeforeEach
    void setUp() {
        // Runs before every @Test
        // Build test data once, reuse in all tests
        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Clean Code");
        testBook.setAuthor("Robert Martin");
        testBook.setGenre(Genre.TECHNOLOGY);
        testBook.setTotalCopies(3);
        testBook.setAvailableCopies(3);

        testRequest = new BookRequest();
        testRequest.setTitle("Clean Code");
        testRequest.setAuthor("Robert Martin");
        testRequest.setGenre(Genre.TECHNOLOGY);
        testRequest.setTotalCopies(3);
    }

    @Test
    void getBook_ShouldReturnBook_WhenBookExists() {
        // Arrange
        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(testBook));

        // Act
        BookResponse result = bookService.getBook(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Clean Code", result.getTitle());
        assertEquals("Robert Martin", result.getAuthor());
        verify(bookRepository).findById(1L);
    }

    @Test
    void getBook_ShouldThrowException_WhenBookNotFound() {
        // Arrange
        when(bookRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act + Assert
        BookNotFoundException exception = assertThrows(
                BookNotFoundException.class,
                () -> bookService.getBook(99L)
        );

        assertEquals("Book not found: 99", exception.getMessage());
        verify(bookRepository).findById(99L);
    }

    @Test
    void addBook_ShouldSaveAndReturnBook() {
        // Arrange
        when(bookRepository.save(any(Book.class)))
                .thenReturn(testBook);

        // Act
        BookResponse result = bookService.addBook(testRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Clean Code", result.getTitle());
        assertEquals(3, result.getAvailableCopies());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void deleteBook_ShouldDelete_WhenBookExists() {
        // Arrange
        when(bookRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(1L);
        // doNothing() for void methods

        // Act
        bookService.deleteBook(1L);

        // Assert
        verify(bookRepository).existsById(1L);
        verify(bookRepository).deleteById(1L);
    }

    @Test
    void deleteBook_ShouldThrow_WhenBookNotFound() {
        // Arrange
        when(bookRepository.existsById(99L)).thenReturn(false);

        // Act + Assert
        assertThrows(
                BookNotFoundException.class,
                () -> bookService.deleteBook(99L)
        );

        verify(bookRepository, never()).deleteById(any());
        // Confirm deleteById was never called
    }
}