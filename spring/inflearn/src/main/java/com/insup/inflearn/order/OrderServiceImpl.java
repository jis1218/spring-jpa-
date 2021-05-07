package com.insup.inflearn.order;

import com.insup.inflearn.discount.DiscountPolicy;
import com.insup.inflearn.discount.FixDiscountPolicy;
import com.insup.inflearn.discount.RateDiscountPolicy;
import com.insup.inflearn.member.Member;
import com.insup.inflearn.member.MemberRepository;
import com.insup.inflearn.member.MemoryMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final MemberRepository memberRepository;
//    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    //인터페이스와 구현체에도 둘 다 의존하고 있음, DIP 위반 -> OCP 위반
    private final DiscountPolicy discountPolicy;
//    @RequiredArgsConstructor
//    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
//        this.memberRepository = memberRepository;
//        this.discountPolicy = discountPolicy;
//    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = itemPrice - discountPolicy.discount(member, itemPrice);
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
