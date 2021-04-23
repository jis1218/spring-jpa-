package com.insup.inflearn;

import com.insup.inflearn.config.AppConfig;
import com.insup.inflearn.member.Grade;
import com.insup.inflearn.member.Member;
import com.insup.inflearn.member.MemberService;
import com.insup.inflearn.member.MemberServiceImpl;

public class MemberApp {
    public static void main(String[] args){
        AppConfig appConfig = new AppConfig();
        MemberService memberService = appConfig.memberService();
        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);

        Member findMember = memberService.findMember(1L);
    }

}
