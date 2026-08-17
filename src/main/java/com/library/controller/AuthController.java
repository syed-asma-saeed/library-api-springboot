package com.library.controller;

import com.library.dto.request.AuthRequest;
import com.library.dto.request.RegisterRequest;
import com.library.dto.response.AuthResponse;
import com.library.dto.response.ErrorResponse;
import com.library.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
<<<<<<< HEAD
import io.swagger.v3.oas.annotations.media.ExampleObject;
=======
>>>>>>> 508f3eae2851d7b50f65c2519dc5c38db4bd4a6d
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Register and login to get JWT token")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Create your Account")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account Created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(
                            mediaType = "application/json",
<<<<<<< HEAD
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "status": 400,
                                      "message": "Validation failed!",
                                      "timestamp": "2026-08-17T18:30:00"
                                    }
                                    """
                            )
=======
                            schema = @Schema(implementation = ErrorResponse.class)
>>>>>>> 508f3eae2851d7b50f65c2519dc5c38db4bd4a6d
                    )),
            @ApiResponse(responseCode = "409", description = "User already exists",
                    content = @Content(
                            mediaType = "application/json",
<<<<<<< HEAD
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "status": 409,
                                      "message": "User already exists with this email.",
                                      "timestamp": "2026-08-17T18:30:00"
                                    }
                                    """
                            )
=======
                            schema = @Schema(implementation = ErrorResponse.class)
>>>>>>> 508f3eae2851d7b50f65c2519dc5c38db4bd4a6d
                    )),
    })
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(201).body(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Login to your Account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logged in successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)
                    )),
<<<<<<< HEAD
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "status": 401,
                                      "message": "Invalid credentials.",
                                      "timestamp": "2026-08-17T18:30:00"
                                    }
                                    """
                            )
=======
            @ApiResponse(responseCode = "401", description = "Wrong Credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
>>>>>>> 508f3eae2851d7b50f65c2519dc5c38db4bd4a6d
                    ))
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

<<<<<<< HEAD

=======
>>>>>>> 508f3eae2851d7b50f65c2519dc5c38db4bd4a6d
}
