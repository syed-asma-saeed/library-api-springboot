package com.library.controller;

import com.library.dto.request.BorrowRequest;
import com.library.dto.request.MemberRequest;
import com.library.dto.response.BorrowResponse;
import com.library.dto.response.ErrorResponse;
import com.library.dto.response.MemberResponse;
import com.library.dto.response.PageResponse;
import com.library.service.BorrowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Borrow Operations", description = "Endpoints for borrowing books, returning them, and viewing history")
@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService){
        this.borrowService = borrowService;
    }

    @PostMapping
    @Operation(summary = "Borrow a Book")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book borrowed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BorrowResponse.class)
                    )),
            @ApiResponse(responseCode = "400", description = "No copies available OR Borrow Limit exceeded",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "status": 400,
                                      "message": "No copies available OR Borrow Limit exceeded.",
                                      "timestamp": "2026-08-17T18:30:00"
                                    }
                                    """
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "status": 404,
                                      "message": "Book not found.",
                                      "timestamp": "2026-08-17T18:30:00"
                                    }
                                    """
                            )
                    ))
    })
    public ResponseEntity<BorrowResponse> borrowBook(@Valid @RequestBody BorrowRequest request) {  //@valid works only one @RequestBody
        return ResponseEntity.status(201).body(borrowService.borrowBook(request));
    }

    @PostMapping("return/{recordId}")
    @Operation(summary = "Return a Book")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book returned successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BorrowResponse.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Book already returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "status": 400,
                                      "message": "Book already returned.",
                                      "timestamp": "2026-08-17T18:30:00"
                                    }
                                    """
                            )
                    )),

            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "status": 404,
                                      "message": "Book not found.",
                                      "timestamp": "2026-08-17T18:30:00"
                                    }
                                    """
                            )
                    ))
    })
    public ResponseEntity<BorrowResponse> returnBook(@PathVariable Long recordId){
        return ResponseEntity.ok(borrowService.returnBook(recordId));
    }

    @GetMapping("/overdue")
    @Operation(summary = "Get overdue records")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Overdue records retrieved (may be empty list)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BorrowResponse.class)
                    )),
            @ApiResponse(responseCode = "401", description = "Unauthorized — JWT token required",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "status": 401,
                                      "message": "Unauthorized — JWT token required.",
                                      "timestamp": "2026-08-17T18:30:00"
                                    }
                                    """
                            )
                    ))
    })
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
    @Operation(summary = "Get member borrow history")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Borrow history retrieved (may be empty list)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BorrowResponse.class)
                    )),
            @ApiResponse(responseCode = "401", description = "No history available",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "status": 401,
                                      "message": "No history available.",
                                      "timestamp": "2026-08-17T18:30:00"
                                    }
                                    """
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "Member not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "status": 404,
                                      "message": "Member not found.",
                                      "timestamp": "2026-08-17T18:30:00"
                                    }
                                    """
                            )
                    ))
    })
    public ResponseEntity<PageResponse<BorrowResponse>> getMemberHistory(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                borrowService.getMemberHistory(memberId, page, size)
        );
    }

}