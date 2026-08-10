package com.library.service;

import com.library.dto.request.BorrowRequest;
import com.library.dto.response.BorrowResponse;
import com.library.dto.response.MemberResponse;
import com.library.exception.*;
import com.library.model.Book;
import com.library.model.BorrowRecord;
import com.library.model.Member;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowService {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    BorrowService(BookRepository bookRepository, MemberRepository memberRepository, BorrowRecordRepository borrowRecordRepository){
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

        Book bookSaved = bookRepository.save(book);
        Member memberSaved = memberRepository.save(member);
        BorrowRecord recordSaved = borrowRecordRepository.save(record);

        return toResponse(recordSaved);
    }


    public List<BorrowResponse> getOverdueRecords(){
        return borrowRecordRepository
                .findByReturnedFalseAndDueDateBefore(LocalDate.now())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }


    public List<BorrowResponse> getMemberHistory(Long memberId){
        return borrowRecordRepository.findByMember(memberRepository.findById(memberId))
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toUnmodifiableList());
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
}