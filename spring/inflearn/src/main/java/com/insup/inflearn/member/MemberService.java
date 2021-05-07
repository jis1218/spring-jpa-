package com.insup.inflearn.member;

import org.springframework.context.annotation.ComponentScan;

public interface MemberService {

    void join(Member member);

    Member findMember(Long memberId);
}
