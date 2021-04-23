package com.insup.inflearn.config;

import com.insup.inflearn.discount.DiscountPolicy;
import com.insup.inflearn.discount.FixDiscountPolicy;
import com.insup.inflearn.member.MemberRepository;
import com.insup.inflearn.member.MemberService;
import com.insup.inflearn.member.MemberServiceImpl;
import com.insup.inflearn.member.MemoryMemberRepository;
import com.insup.inflearn.order.OrderService;
import com.insup.inflearn.order.OrderServiceImpl;

public class AppConfig {

    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    private MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }

    public OrderService orderService() {
        return new OrderServiceImpl(
                memberRepository(),
                discountPolicy());
    }

    public DiscountPolicy discountPolicy(){
        return new FixDiscountPolicy();
    }
}
