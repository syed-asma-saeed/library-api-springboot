package com.library.controller;

import com.library.dto.request.BorrowRequest;
import com.library.dto.request.MemberRequest;
import com.library.dto.response.BorrowResponse;
import com.library.dto.response.MemberResponse;
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
    public ResponseEntity<BorrowResponse> borrowBook(@Valid @RequestBody BorrowRequest request) {
        return ResponseEntity.status(201).body(borrowService.borrowBook(request));
    }

    @PostMapping("return/{recordId}")
    public ResponseEntity<BorrowResponse> returnBook(@Valid @PathVariable Long recordId){
        return ResponseEntity.ok(borrowService.returnBook(recordId));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<BorrowResponse>> getOverdueRecords(){
        return ResponseEntity.ok(borrowService.getOverdueRecords());
    }

    @GetMapping("history/{memberId}")
    public ResponseEntity<List<BorrowResponse>> getMemberHistory(@PathVariable Long memberId){
        return ResponseEntity.ok(borrowService.getMemberHistory(memberId));
    }

}