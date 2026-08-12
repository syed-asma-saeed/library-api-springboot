package com.library.controller;

import com.library.dto.request.BorrowRequest;
import com.library.dto.request.MemberRequest;
import com.library.dto.response.BorrowResponse;
import com.library.dto.response.MemberResponse;
import com.library.dto.response.PageResponse;
import com.library.service.BorrowService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService){
        this.borrowService = borrowService;
    }

    @PostMapping
    public ResponseEntity<BorrowResponse> borrowBook(@Valid @RequestBody BorrowRequest request) {  //@valid works only one @RequestBody
        return ResponseEntity.status(201).body(borrowService.borrowBook(request));
    }

    @PostMapping("return/{recordId}")
    public ResponseEntity<BorrowResponse> returnBook(@PathVariable Long recordId){
        return ResponseEntity.ok(borrowService.returnBook(recordId));
    }

    @GetMapping("/overdue")
    public ResponseEntity<PageResponse<BorrowResponse>> getOverdueRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dueDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        return ResponseEntity.ok(
                borrowService.getOverdueRecords(page, size, sortBy, sortDir)
        );
    }

    @GetMapping("/history/{memberId}")
    public ResponseEntity<PageResponse<BorrowResponse>> getMemberHistory(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                borrowService.getMemberHistory(memberId, page, size)
        );
    }

}