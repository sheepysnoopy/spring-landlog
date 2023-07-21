package com.landvibe.landlog.service;

import com.landvibe.landlog.controller.LoginForm;
import com.landvibe.landlog.domain.Member;
import com.landvibe.landlog.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MemberServiceTest {

    MemberService memberService;
    MemoryMemberRepository memberRepository;

    @BeforeEach
    public void beforeEach() {
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @AfterEach
    public void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    public void 회원가입() throws Exception {

        Member member = new Member();
        member.setName("hello");

        Long saveId = memberService.join(member);

        Member findMember = memberRepository.findById(saveId).get();
        assertEquals(member.getName(), findMember.getName());
    }

    @Test
    public void 중복_회원_예외() throws Exception {

        Member member1 = new Member();
        member1.setName("spring");
        Member member2 = new Member();
        member2.setName("spring");

        memberService.join(member1);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> memberService.join(member2));//예외가 발생해야 한다.
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }

    @Test
    void 로그인() {
        Member member = new Member("양재승", "jaeseung@naver.com", "123");

        memberService.join(member);

        LoginForm loginForm = new LoginForm("jaeseung@naver.com", "123");

        Long loginId = memberService.logIn(loginForm);
        assertThat(loginId).isEqualTo(member.getId());
    }

    @Test
    public void 틀린비밀번호입력(){
        String testEmail = "jaeseung@naver.com";
        Member member = new Member("양재승", testEmail, "123");

        memberService.join(member);
        LoginForm loginForm = new LoginForm(testEmail, "456");

        Exception e = assertThrows(Exception.class,
                () -> memberService.logIn(loginForm));
        assertThat(e.getMessage()).isEqualTo("비밀번호를 확인해주세요.");
    }

}