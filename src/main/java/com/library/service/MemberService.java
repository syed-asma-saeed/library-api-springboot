package com.library.service;

import com.library.dto.request.MemberRequest;
import com.library.dto.response.MemberResponse;
import com.library.exception.BookNotFoundException;
import com.library.exception.BorrowCountNotZeroException;
import com.library.exception.DuplicateMemberException;
import com.library.exception.MemberNotFoundException;
import com.library.model.Member;
import com.library.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository){ this.memberRepository = memberRepository; }

    public List<MemberResponse> getAllMembers(){
        return memberRepository.findAll()
                .stream()
                .map(this :: toResponse)
                .collect(Collectors.toList());
    }

    public MemberResponse getMember(Long id){
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found: " + id));

        return toResponse(member);

    }
    public Optional<MemberResponse> addMember(MemberRequest request){
        memberRepository.findByEmail(request.getEmail())
                .ifPresent(member -> {
                    throw new DuplicateMemberException(
                            "Member already exists with email: " + request.getEmail()
                    );
                });

        Member member = new Member();

        member.setName(request.getName());
        member.setEmail(request.getEmail());
        member.setMemberType(request.getMemberType());

        Member saved = memberRepository.save(member);
        return Optional.of(toResponse(saved));
    }

    public MemberResponse updateMember(Long id, MemberRequest request){
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found: " + id));

        member.setName(request.getName());
        member.setEmail(request.getEmail());
        member.setMemberType(request.getMemberType());

        Member saved = memberRepository.save(member);
        return toResponse(saved);
    }


    public void deleteMember(Long id){
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found: " + id));

        if(member.getCurrentBorrowCount() == 0)
            memberRepository.delete(member);
        else
            throw new BorrowCountNotZeroException("Borrow count is not zero of Member:" + id);
    }

    private MemberResponse toResponse(Member member){
        MemberResponse response = new MemberResponse();
        response.setId(member.getId());
        response.setName(member.getName());
        response.setEmail(member.getEmail());
        response.setMemberType(member.getMemberType().name());
        response.setCurrentBorrowCount(member.getCurrentBorrowCount());
        response.setBorrowLimit(member.getMemberType().getBorrowLimit());

        return response;
    }

}