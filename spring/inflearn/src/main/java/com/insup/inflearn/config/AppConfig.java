package com.insup.inflearn.config;

import com.insup.inflearn.discount.DiscountPolicy;
import com.insup.inflearn.discount.FixDiscountPolicy;
import com.insup.inflearn.member.MemberRepository;
import com.insup.inflearn.member.MemberService;
import com.insup.inflearn.member.MemberServiceImpl;
import com.insup.inflearn.member.MemoryMemberRepository;
import com.insup.inflearn.order.OrderService;
import com.insup.inflearn.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {


    @Bean
    public MemberService memberService() {
        System.out.println("AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(
                memberRepository(),
                discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy(){
        return new FixDiscountPolicy();
    }
}
