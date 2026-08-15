package com.library.service;

import com.library.dto.request.BorrowRequest;
import com.library.dto.response.BorrowResponse;
import com.library.enums.MemberType;
import com.library.enums.Genre;
import com.library.exception.*;
import com.library.model.Book;
import com.library.model.BorrowRecord;
import com.library.model.Member;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowServiceTest {

    // ─── Mocks ───────────────────────────────────────────────────

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BorrowRecordRepository borrowRecordRepository;

    @InjectMocks
    private BorrowService borrowService;

    // ─── Test Data ───────────────────────────────────────────────

    private Member studentMember;
    private Member facultyMember;
    private Member memberAtLimit;
    private Book availableBook;
    private Book unavailableBook;
    private BorrowRecord activeRecord;
    private BorrowRecord returnedRecord;
    private BorrowRecord overdueRecord;
    private BorrowRequest borrowRequest;

    @BeforeEach
    void setUp() {

        // Student member — can borrow, limit 3
        studentMember = new Member();
        studentMember.setId(1L);
        studentMember.setName("Rahul");
        studentMember.setEmail("rahul@test.com");
        studentMember.setMemberType(MemberType.STUDENT);
        studentMember.setCurrentBorrowCount(0);

        // Faculty member — can borrow, limit 10
        facultyMember = new Member();
        facultyMember.setId(2L);
        facultyMember.setName("Dr. Priya");
        facultyMember.setEmail("priya@test.com");
        facultyMember.setMemberType(MemberType.FACULTY);
        facultyMember.setCurrentBorrowCount(0);

        // Member at borrow limit — cannot borrow
        memberAtLimit = new Member();
        memberAtLimit.setId(3L);
        memberAtLimit.setName("Limited User");
        memberAtLimit.setEmail("limited@test.com");
        memberAtLimit.setMemberType(MemberType.STUDENT);
        memberAtLimit.setCurrentBorrowCount(3);

        // Book with available copies
        availableBook = new Book();
        availableBook.setId(1L);
        availableBook.setTitle("Clean Code");
        availableBook.setAuthor("Robert Martin");
        availableBook.setGenre(Genre.TECHNOLOGY);
        availableBook.setTotalCopies(3);
        availableBook.setAvailableCopies(2);

        // Book with no available copies
        unavailableBook = new Book();
        unavailableBook.setId(2L);
        unavailableBook.setTitle("Sapiens");
        unavailableBook.setAuthor("Yuval Harari");
        unavailableBook.setGenre(Genre.HISTORY);
        unavailableBook.setTotalCopies(1);
        unavailableBook.setAvailableCopies(0);

        // Active borrow record — not yet returned
        activeRecord = new BorrowRecord();
        activeRecord.setId(1L);
        activeRecord.setMember(studentMember);
        activeRecord.setBook(availableBook);
        activeRecord.setBorrowDate(LocalDate.now().minusDays(5));
        activeRecord.setDueDate(LocalDate.now().plusDays(9));
        // dueDate is in future → isOverdue() = false
        activeRecord.setReturned(false);
        activeRecord.setFine(0.0);

        // Already returned record
        returnedRecord = new BorrowRecord();
        returnedRecord.setId(2L);
        returnedRecord.setMember(studentMember);
        returnedRecord.setBook(availableBook);
        returnedRecord.setBorrowDate(LocalDate.now().minusDays(10));
        returnedRecord.setDueDate(LocalDate.now().minusDays(3));
        returnedRecord.setReturnDate(LocalDate.now().minusDays(4));
        returnedRecord.setReturned(true);
        returnedRecord.setFine(0.0);

        // Overdue record — past due date, not returned
        overdueRecord = new BorrowRecord();
        overdueRecord.setId(3L);
        overdueRecord.setMember(studentMember);
        overdueRecord.setBook(availableBook);
        overdueRecord.setBorrowDate(LocalDate.now().minusDays(20));
        overdueRecord.setDueDate(LocalDate.now().minusDays(6));
        overdueRecord.setReturned(false);
        overdueRecord.setFine(0.0);

        // Default borrow request
        borrowRequest = new BorrowRequest();
        borrowRequest.setMemberId(1L);
        borrowRequest.setBookId(1L);
    }

    @Test
    void borrowBook_ShouldSucceed_WhenStudentBorrows() {
        // Arrange
        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(studentMember));
        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(availableBook));

        // Set up what the saved record looks like
        BorrowRecord savedRecord = new BorrowRecord();
        savedRecord.setId(1L);
        savedRecord.setMember(studentMember);
        savedRecord.setBook(availableBook);
        savedRecord.setBorrowDate(LocalDate.now());
        savedRecord.setDueDate(LocalDate.now().plusDays(14));
        savedRecord.setReturned(false);
        savedRecord.setFine(0.0);

        when(borrowRecordRepository.save(any(BorrowRecord.class)))
                .thenReturn(savedRecord);

        // Act
        BorrowResponse result = borrowService.borrowBook(borrowRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Rahul", result.getMemberName());
        assertEquals("Clean Code", result.getBookTitle());
        assertEquals(LocalDate.now().plusDays(14), result.getDueDate());
        assertFalse(result.isReturned());
        assertEquals(0.0, result.getFine());

        // Verify side effects
        assertEquals(1, availableBook.getAvailableCopies());
        // Was 2, now 1 after borrow

        assertEquals(1, studentMember.getCurrentBorrowCount());
        // Was 0, now 1 after borrow

        verify(borrowRecordRepository).save(any(BorrowRecord.class));
    }

    @Test
    void borrowBook_ShouldSetCorrectDueDate_WhenFacultyBorrows() {
        // Faculty gets 30 days — different from student's 14
        borrowRequest.setMemberId(2L);

        when(memberRepository.findById(2L))
                .thenReturn(Optional.of(facultyMember));
        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(availableBook));

        BorrowRecord savedRecord = new BorrowRecord();
        savedRecord.setId(4L);
        savedRecord.setMember(facultyMember);
        savedRecord.setBook(availableBook);
        savedRecord.setBorrowDate(LocalDate.now());
        savedRecord.setDueDate(LocalDate.now().plusDays(30));
        // FACULTY due days = 30
        savedRecord.setReturned(false);
        savedRecord.setFine(0.0);

        when(borrowRecordRepository.save(any(BorrowRecord.class)))
                .thenReturn(savedRecord);

        // Act
        BorrowResponse result = borrowService.borrowBook(borrowRequest);

        // Assert — key check is the due date
        assertEquals(LocalDate.now().plusDays(30), result.getDueDate());
        // If this fails, MemberType.getDueDays() is wrong
    }

    @Test
    void borrowBook_ShouldThrow_WhenMemberNotFound() {
        // Arrange
        when(memberRepository.findById(99L))
                .thenReturn(Optional.empty());

        borrowRequest.setMemberId(99L);

        // Act + Assert
        MemberNotFoundException exception = assertThrows(
                MemberNotFoundException.class,
                () -> borrowService.borrowBook(borrowRequest)
        );

        assertEquals("Member not found: 99", exception.getMessage());

        // Verify book repository was never touched
        // If member not found, we shouldn't even query the book
        verify(bookRepository, never()).findById(any());
        verify(borrowRecordRepository, never()).save(any());
    }

    @Test
    void borrowBook_ShouldThrow_WhenBookNotFound() {
        // Arrange
        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(studentMember));
        when(bookRepository.findById(99L))
                .thenReturn(Optional.empty());

        borrowRequest.setBookId(99L);

        // Act + Assert
        BookNotFoundException exception = assertThrows(
                BookNotFoundException.class,
                () -> borrowService.borrowBook(borrowRequest)
        );

        assertEquals("Book not found: 99", exception.getMessage());
        verify(borrowRecordRepository, never()).save(any());
    }

    @Test
    void borrowBook_ShouldThrow_WhenMemberAtBorrowLimit() {
        // Arrange — member already at max (3 books for STUDENT)
        when(memberRepository.findById(3L))
                .thenReturn(Optional.of(memberAtLimit));
        // Note: book repository is also mocked because
        // your service queries book AFTER member
        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(availableBook));

        borrowRequest.setMemberId(3L);

        // Act + Assert
        assertThrows(
                BorrowLimitExceededException.class,
                () -> borrowService.borrowBook(borrowRequest)
        );

        // Verify no record was saved
        verify(borrowRecordRepository, never()).save(any());

        // Verify book copies were NOT decremented
        assertEquals(2, availableBook.getAvailableCopies());
    }

    @Test
    void borrowBook_ShouldThrow_WhenNoAvailableCopies() {
        // Arrange — book has 0 available copies
        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(studentMember));
        when(bookRepository.findById(2L))
                .thenReturn(Optional.of(unavailableBook));

        borrowRequest.setBookId(2L);

        // Act + Assert
        assertThrows(
                BookNotAvailableException.class,
                () -> borrowService.borrowBook(borrowRequest)
        );

        verify(borrowRecordRepository, never()).save(any());

        // Verify member count was NOT incremented
        assertEquals(0, studentMember.getCurrentBorrowCount());
    }

    @Test
    void returnBook_ShouldSucceed_WhenReturnedOnTime() {
        // Arrange — activeRecord has future due date → no fine
        when(borrowRecordRepository.findById(1L))
                .thenReturn(Optional.of(activeRecord));
        when(borrowRecordRepository.save(any(BorrowRecord.class)))
                .thenReturn(activeRecord);

        int copiesBefore = availableBook.getAvailableCopies();
        // Was 2 before return
        int borrowCountBefore = studentMember.getCurrentBorrowCount();
        // Was 0 (we didn't increment in setUp)

        // Act
        BorrowResponse result = borrowService.returnBook(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isReturned());
        assertEquals(0.0, result.getFine());
        // No fine — returned before due date

        assertEquals(LocalDate.now(), activeRecord.getReturnDate());

        // Verify side effects
        assertEquals(copiesBefore + 1,
                availableBook.getAvailableCopies());
        // Copies incremented back

        assertEquals(borrowCountBefore - 1,
                studentMember.getCurrentBorrowCount());
        // Borrow count decremented

        verify(borrowRecordRepository).save(activeRecord);
    }

    @Test
    void returnBook_ShouldCalculateFine_WhenReturnedLate() {
        // Arrange — overdueRecord has past due date
        // dueDate = today - 6 days → 6 days overdue
        // fine = 6 * 2.0 = ₹12.0
        when(borrowRecordRepository.findById(3L))
                .thenReturn(Optional.of(overdueRecord));
        when(borrowRecordRepository.save(any(BorrowRecord.class)))
                .thenReturn(overdueRecord);

        // Act
        BorrowResponse result = borrowService.returnBook(3L);

        // Assert
        assertTrue(result.isReturned());
        assertEquals(12.0, result.getFine(), 0.01);
        // 0.01 delta for floating point comparison
        // 6 days * ₹2.0 per day = ₹12.0

        assertTrue(overdueRecord.isReturned());
        assertEquals(LocalDate.now(), overdueRecord.getReturnDate());
    }

    @Test
    void returnBook_ShouldThrow_WhenRecordNotFound() {
        // Arrange
        when(borrowRecordRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act + Assert
        RecordNotFoundException exception = assertThrows(
                RecordNotFoundException.class,
                () -> borrowService.returnBook(99L)
        );

        assertEquals("Record not found: 99", exception.getMessage());
        verify(borrowRecordRepository, never()).save(any());
    }

    @Test
    void returnBook_ShouldThrow_WhenAlreadyReturned() {
        // Arrange — returnedRecord.isReturned() = true
        when(borrowRecordRepository.findById(2L))
                .thenReturn(Optional.of(returnedRecord));

        // Act + Assert
        assertThrows(
                AlreadyReturnedException.class,
                () -> borrowService.returnBook(2L)
        );

        // Verify nothing was saved
        verify(borrowRecordRepository, never()).save(any());

        // Verify book copies were NOT changed
        assertEquals(2, availableBook.getAvailableCopies());
    }

    @Test
    void returnBook_ShouldIncrementAvailableCopies_WhenReturned() {
        // Arrange
        when(borrowRecordRepository.findById(1L))
                .thenReturn(Optional.of(activeRecord));
        when(borrowRecordRepository.save(any(BorrowRecord.class)))
                .thenReturn(activeRecord);

        int copiesBefore = availableBook.getAvailableCopies(); // 2

        // Act
        borrowService.returnBook(1L);

        // Assert — focused purely on copy count side effect
        assertEquals(copiesBefore + 1,
                availableBook.getAvailableCopies()); // now 3
    }

    @Test
    void returnBook_ShouldDecrementBorrowCount_WhenReturned() {
        // Give member 1 active borrow before returning
        studentMember.setCurrentBorrowCount(1);

        when(borrowRecordRepository.findById(1L))
                .thenReturn(Optional.of(activeRecord));
        when(borrowRecordRepository.save(any(BorrowRecord.class)))
                .thenReturn(activeRecord);

        // Act
        borrowService.returnBook(1L);

        // Assert — focused purely on borrow count side effect
        assertEquals(0, studentMember.getCurrentBorrowCount());
    }
}