package com.library.repository;

import com.library.model.Book;
import com.library.model.BorrowRecord;
import com.library.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    Page<BorrowRecord> findByReturnedFalseAndDueDateBefore(LocalDate date, Pageable pageable);
    Page<BorrowRecord> findByMember(Member member, Pageable pageable);

    @Query(
            value = "SELECT br FROM BorrowRecord br " +
                    "JOIN FETCH br.member " +
                    "JOIN FETCH br.book " +
                    "WHERE br.member = :member",
            countQuery = "SELECT COUNT(br) FROM BorrowRecord br WHERE br.member = :member"
    )
    Page<BorrowRecord> findByMemberWithDetails(
            @Param("member") Member member,
            Pageable pageable);
}