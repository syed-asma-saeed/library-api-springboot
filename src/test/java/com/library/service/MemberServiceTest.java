package com.library.service;

import com.library.dto.request.MemberRequest;
import com.library.dto.response.BookResponse;
import com.library.dto.response.MemberResponse;
import com.library.enums.MemberType;
import com.library.exception.BookNotFoundException;
import com.library.exception.BorrowCountNotZeroException;
import com.library.exception.DuplicateMemberException;
import com.library.exception.MemberNotFoundException;
import com.library.model.Member;
import com.library.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    MemberService memberService;

    private Member testMember;
    private MemberRequest testRequest;

    @BeforeEach
    void setUp(){
        testMember = new Member();
        testMember.setId(1L);
        testMember.setName("John Doe");
        testMember.setEmail("jhondoe@email.com");
        testMember.setMemberType(MemberType.STUDENT);
        testMember.setCurrentBorrowCount(2);

        testRequest = new MemberRequest();
        testRequest.setName("John Doe");
        testRequest.setEmail("jhondoe.email.com");
        testRequest.setMemberType(MemberType.STUDENT);
    }

    @Test
    void getMember_WhenMemberExists_ShouldReturnMember(){
        // Arrange
        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(testMember));

        // Act
        MemberResponse result = memberService.getMember(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("jhondoe@email.com", result.getEmail());
        verify(memberRepository).findById(1L);
    }

    @Test
    void getMember_WhenMemberNotFound_ShouldThrowException(){
        // Arrange
        when(memberRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act
        MemberNotFoundException exception = assertThrows(MemberNotFoundException.class,
                ()-> memberService.getMember(99L));

        // Assert
        assertEquals("Member not found: 99", exception.getMessage());
        verify(memberRepository).findById(99L);
    }

    @Test
    void addMember_WhenMemberDoesNotExist_ShouldSaveAndReturnMember(){
        // Arrange
        when(memberRepository.findByEmail(testRequest.getEmail()))
                .thenReturn(Optional.empty());  // no duplicate
        when(memberRepository.save(any(Member.class)))
                .thenReturn(testMember);

        // Act
        MemberResponse result = memberService.addMember(testRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("johndoe@email.com", result.getEmail());
        verify(memberRepository).findByEmail(testRequest.getEmail());
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void addMember_WhenMemberExists_ShouldThrowDuplicateMemberException(){
        // Arrange
        when(memberRepository.findByEmail(testRequest.getEmail()))
                .thenReturn(Optional.of(testMember));

        // Act
        DuplicateMemberException exception = assertThrows(
                DuplicateMemberException.class,
                () -> memberService.addMember(testRequest));

        // Assert
        assertEquals("Member already exists with email: " +
                testRequest.getEmail(), exception.getMessage());
        verify(memberRepository).findByEmail(testRequest.getEmail());
        verify(memberRepository, never()).save(any());
    }

    @Test
    void deleteMember_WhenBorrowCountZero_Success(){
        // Arrange
        testMember.setCurrentBorrowCount(0);

        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(testMember));

        // Act
        memberService.deleteMember(1L);

        // Assert
        verify(memberRepository).existsById(1L);
        verify(memberRepository).delete(testMember);
    }

    @Test
    void deleteMember_WhenBorrowCountNotZero_ShouldThrow(){
        //Act
        testMember.setCurrentBorrowCount(1);
        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(testMember));

        BorrowCountNotZeroException exception = assertThrows(BorrowCountNotZeroException.class,
                () -> memberService.deleteMember(1L));

        // Assert
        assertEquals("Borrow count is not zero of Member:" + 1L, exception.getMessage());
        verify(memberRepository).findById(99L);
    }
}
