package com.library.config;

import com.library.dto.request.BookRequest;
import com.library.dto.request.MemberRequest;
import com.library.dto.request.RegisterRequest;
import com.library.enums.Genre;
import com.library.enums.MemberType;
import com.library.enums.Role;
import com.library.repository.UserRepository;
import com.library.service.AuthService;
import com.library.service.BookService;
import com.library.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AuthService authService;
    private final BookService bookService;
    private final MemberService memberService;
    private final UserRepository userRepository;

    public DataInitializer(AuthService authService,
                           BookService bookService,
                           MemberService memberService,
                           UserRepository userRepository) {
        this.authService = authService;
        this.bookService = bookService;
        this.memberService = memberService;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {

        // Only seed if database is empty
        if (userRepository.count() == 0) {
            // Admin user
            authService.register(new RegisterRequest(
                    "Admin", "admin@library.com", Role.ADMIN, "admin123"));

            // Regular user
            authService.register(new RegisterRequest(
                    "User", "user@library.com", Role.USER, "user123"));

            // Sample books
            bookService.addBook(new BookRequest(
                    "Clean Code", "Robert C. Martin", Genre.TECHNOLOGY, 5));
            bookService.addBook(new BookRequest(
                    "The Pragmatic Programmer", "David Thomas", Genre.TECHNOLOGY, 3));
            bookService.addBook(new BookRequest(
                    "Sapiens", "Yuval Noah Harari", Genre.HISTORY, 4));
            bookService.addBook(new BookRequest(
                    "Atomic Habits", "James Clear", Genre.NON_FICTION, 6));
            bookService.addBook(new BookRequest(
                    "Dune", "Frank Herbert", Genre.FICTION, 2));

            // Sample members
            memberService.addMember(new MemberRequest(
                    "Rahul Sharma", "rahul@test.com", MemberType.STUDENT));
            memberService.addMember(new MemberRequest(
                    "Dr. Priya Iyer", "priya@test.com", MemberType.FACULTY));

            System.out.println("Sample data initialized");
            System.out.println("Admin: admin@library.com / admin123");
            System.out.println("User:  user@library.com  / user123");
        }
    }
}
