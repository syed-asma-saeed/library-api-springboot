package com.library.controller;

import com.library.dto.request.MemberRequest;
import com.library.dto.response.ErrorResponse;
import com.library.dto.response.MemberResponse;
import com.library.service.MemberService;
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

@Tag(
        name = "Member Management",
        description = "Endpoints for managing library members"
)
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    @Operation(
            summary = "Get all members",
            description = "ADMIN only."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Members retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": 401,
                                              "message": "Unauthorized user."
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<List<MemberResponse>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get member by ID",
            description = "ADMIN only."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Member found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": 401,
                                              "message": "Unauthorized."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Member not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": 404,
                                              "message": "Member not found."
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<MemberResponse> getMember(
            @PathVariable Long id) {

        return ResponseEntity.ok(memberService.getMember(id));
    }

    @PostMapping
    @Operation(
            summary = "Add a new member",
            description = "ADMIN only. Creates a new member."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Member created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": 400,
                                              "message": "Validation failed."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden — ADMIN role required",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": 403,
                                              "message": "Forbidden — ADMIN role required."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Member already exists with this email",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": 409,
                                              "message": "Member already exists with this email."
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<MemberResponse> addMember(
            @Valid @RequestBody MemberRequest request) {

        return ResponseEntity
                .status(201)
                .body(memberService.addMember(request));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a member",
            description = "ADMIN only."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Member updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden — ADMIN role required",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": 403,
                                              "message": "Forbidden — ADMIN role required."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Member not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": 404,
                                              "message": "Member not found."
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<MemberResponse> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody MemberRequest request) {

        return ResponseEntity.ok(
                memberService.updateMember(id, request)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a member",
            description = "ADMIN only."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Member deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Member has active borrows",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": 400,
                                              "message": "Member has active borrows."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden — ADMIN role required",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": 403,
                                              "message": "Forbidden — ADMIN role required."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Member not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": 404,
                                              "message": "Member not found."
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<Void> deleteMember(
            @PathVariable Long id) {

        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}