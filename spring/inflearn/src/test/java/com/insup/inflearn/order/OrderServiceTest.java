package com.insup.inflearn.order;

import com.insup.inflearn.config.AppConfig;
import com.insup.inflearn.discount.DiscountPolicy;
import com.insup.inflearn.discount.FixDiscountPolicy;
import com.insup.inflearn.member.Grade;
import com.insup.inflearn.member.Member;
import com.insup.inflearn.member.MemberService;
import com.insup.inflearn.member.MemberServiceImpl;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderServiceTest {
    AppConfig appConfig = new AppConfig();
    DiscountPolicy discountPolicy = new FixDiscountPolicy();
    MemberService memberService = appConfig.memberService();
    OrderService orderService = appConfig.orderService();

    @Test
    void createOrder(){
        //given
        Long memberId = 1L;
        memberService.join(new Member(memberId, "memberA", Grade.VIP));

        //when
        Order order = orderService.createOrder(memberId, "aa", 10000);

        //then
        assertThat(order.getDiscountPrice()).isEqualTo(9000);

    }
}
