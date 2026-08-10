package com.library.repository;

import com.library.model.Book;
import com.library.model.BorrowRecord;
import com.library.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    List<BorrowRecord> findByMember(Member member);
    List<BorrowRecord> findByBook(Book book);
    List<BorrowRecord> findByReturned(boolean returned);
    List<BorrowRecord> findByReturnedFalseAndDueDateBefore(LocalDate date);
}