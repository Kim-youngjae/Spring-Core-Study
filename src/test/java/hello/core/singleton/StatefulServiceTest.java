package hello.core.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

class StatefulServiceTest {


    /**
     * A는 10000원을 긁었고, 그 이후 B가 개입하여 2만원을 긁었다. 하지만 A의 계좌에서는 2만원이 빠져나간 경우이다.
     * 이렇게 되면 이 서비스는 망한다.
     * 이렇게 되는 이유는 같은 statefulService 객체를 참조하여 10000 -> 20000으로 엎어치기가 된다.
     * 이런 문제가 절대로..! 발생하면 안되지만 꼭 몇년에 한번씩 발생한다고 한다.
     * 특히 이 문제가 돈과 관련이 있다면 심각한 문제를 야기한다.
     *
     */
    @Test
    void statefulServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        // ThreadA 사용자 -> A는 10000원을 주문
        int userAPrice = statefulService1.order("userA", 10000);
        // ThreadB 사용자 -> B는 20000원을 주문
        int userBPrice = statefulService2.order("userB", 20000);
        // 사용자A 금액 조회
//        int price = statefulService1.getPrice();
        System.out.println("userAPrice = " + userAPrice);

        Assertions.assertThat(userAPrice).isEqualTo(10000);
    }

    static class TestConfig {

        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }

}