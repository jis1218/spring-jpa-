package com.insup.inflearn.member;


import org.springframework.stereotype.Component;

public interface MemberRepository {

    void save(Member member);

    Member findById(Long memberId);
}
