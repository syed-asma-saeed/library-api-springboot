package com.library.service;

import com.library.dto.request.BorrowRequest;
import com.library.dto.response.BorrowResponse;
import com.library.dto.response.PageResponse;
import com.library.exception.*;
import com.library.model.Book;
import com.library.model.BorrowRecord;
import com.library.model.Member;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
@Service
public class BorrowService {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    public BorrowService(BookRepository bookRepository, MemberRepository memberRepository, BorrowRecordRepository borrowRecordRepository){
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    @Transactional
    public BorrowResponse borrowBook(BorrowRequest request){
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException("Member not found: " + request.getMemberId()));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Book not found: " + request.getBookId()));

        if(!member.canBorrow())
            throw new BorrowLimitExceededException("Cannot borrow more book.");

        if(book.getAvailableCopies() <= 0)
            throw new BookNotAvailableException("No copies available");

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        int borrowCount = member.incrementBorrowCount();

        LocalDate dueDate = LocalDate.now().plusDays(member.getMemberType().getDueDays());

        BorrowRecord borrowRecord = new BorrowRecord();

        borrowRecord.setMember(member);
        borrowRecord.setBook(book);
        borrowRecord.setBorrowDate(LocalDate.now());
        borrowRecord.setDueDate(dueDate);
        borrowRecord.setReturnDate(null);
        borrowRecord.setFine(0.0);
        borrowRecord.setReturned(false);

        BorrowRecord saved = borrowRecordRepository.save(borrowRecord);
        return toResponse(saved);

    }


    @Transactional
    public BorrowResponse returnBook(Long recordId){
        BorrowRecord record = borrowRecordRepository.findById(recordId)
                .orElseThrow(() -> new RecordNotFoundException("Record not found: " + recordId));

        if(record.isReturned())
            throw new AlreadyReturnedException("Book already returned with record id:" + recordId);

        Member member = record.getMember();
        Book book = record.getBook();

        double fine = record.calculateFine();

        record.setReturned(true);
        record.setReturnDate(LocalDate.now());
        record.setFine(fine);

        book.setAvailableCopies(book.getAvailableCopies()+1);

        member.decrementBorrowCount();

        BorrowRecord recordSaved = borrowRecordRepository.save(record);

        return toResponse(recordSaved);
    }


    public PageResponse<BorrowResponse> getOverdueRecords(int page, int size, String sortBy, String sortDir){
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<BorrowRecord> borrowPage = borrowRecordRepository
                .findByReturnedFalseAndDueDateBefore(LocalDate.now(), pageable);

        Page<BorrowResponse> responsePage = borrowPage.map(this::toResponse);
        return toPageResponse(responsePage);
    }


    public PageResponse<BorrowResponse> getMemberHistory(Long memberId, int page, int size){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new MemberNotFoundException("Member not found: " + memberId));

        Pageable pageable = PageRequest.of(page, size);

        Page<BorrowRecord> borrowPage =
                borrowRecordRepository.findByMember(member, pageable);

        return toPageResponse(borrowPage.map(this::toResponse));
    }


    private BorrowResponse toResponse(BorrowRecord borrowRecord){
        BorrowResponse response = new BorrowResponse();
        response.setRecordId(borrowRecord.getId());
        response.setMemberName(borrowRecord.getMember().getName());
        response.setBookTitle(borrowRecord.getBook().getTitle());
        response.setBorrowDate(borrowRecord.getBorrowDate());
        response.setDueDate(borrowRecord.getDueDate());
        response.setReturned(borrowRecord.isReturned());
        response.setFine(borrowRecord.getFine());

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