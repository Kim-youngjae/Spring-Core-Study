package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;

public class SingletonTest {
    @Test
    @DisplayName("스프링 없는 순수한 DI 컨테이너가 가지는 문제점")
    void pureContainer() {
        AppConfig appConfig = new AppConfig();

        // 1. 조회할 때 마다 객체를 생성한다.
        MemberService memberService1 = appConfig.memberService();

        // 2. 조회할 때 마다 객체를 생성한다.
        MemberService memberService2 = appConfig.memberService();
        // 참조 값이 다른 것을 확인
        System.out.println("memberService1 = " + memberService1); // 호출할 때마다 계속 다른 객체를 호출한다.
        System.out.println("memberService2 = " + memberService2);

        // memberService1 != memberService2
        assertThat(memberService1).isNotSameAs(memberService2);
        // 이렇게 두 개만 다르면 또 모를 수도 있겠지만 memberService 객체 생성을 위해서 memberRepository도 생성되어 총 4개의 호출이 발생
    }

    /**
     * 같은 인스턴스 임을 알 수 있다.
     */
    @Test
    @DisplayName("싱글톤 패턴을 사용한 객체 사용")
    void singletonServiceTest() {
        SingletonService singletonService1 = SingletonService.getInstance();
        SingletonService singletonService2 = SingletonService.getInstance();

        System.out.println("singletonService1 = " + singletonService1);
        System.out.println("singletonService2 = " + singletonService2);

        assertThat(singletonService1).isEqualTo(singletonService2);
    }

    @Test
    @DisplayName("스프링 컨테이너와 싱글톤")
    void springContainer() {
//        AppConfig appConfig = new AppConfig();
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        // 1. 조회할 때 마다 객체를 생성한다.
        MemberService memberService1 = ac.getBean("memberService", MemberService.class);
        // 2. 조회할 때 마다 객체를 생성한다.
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);
        // 참조 값이 다른 것을 확인
        System.out.println("memberService1 = " + memberService1); // 호출할 때마다 계속 다른 객체를 호출한다.
        System.out.println("memberService2 = " + memberService2);

        assertThat(memberService1).isSameAs(memberService2);
    }
}
