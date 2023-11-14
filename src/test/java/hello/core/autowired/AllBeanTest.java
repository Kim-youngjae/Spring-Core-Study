package hello.core.autowired;

import hello.core.AutoAppConfig;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class AllBeanTest {

    /**
     * 임의로 생성한 DiscountService에서 ApplicationContext는 생성자를 통해서 빈을 주입하는데 Map의 형태이다.
     * 근데 value값이 DiscountPolicy형태라 다형성을 이용했을 때 저기 주입이 된다는 느낌은 받았는데 key부분은 어디서 찾아오는거지라는 생각이 들었다.
     * 스프링은 기본적으로 빈 이름을 기반으로 한 Map 형태의 의존성 주입을 지원한다.
     * 빈 이름을 명시적으로 지정하지 않으면 빈의 이름이 key로 한다.
     * 따라서, 위의 코드에서는 "fixDiscountPolicy"와 "rateDiscountPolicy" 빈들이 자동으로 주입되는 것이다.
     */
    @Test
    void findAllBean() {

        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class, DiscountService.class); // AutoAppConfig, DiscountService를 뒤지면서 빈을 등록

        DiscountService discountService = ac.getBean(DiscountService.class);
        Member member = new Member(1L, "userA", Grade.VIP);
        int fixDiscountPrice = discountService.discount(member, 10000, "fixDiscountPolicy");

        assertThat(discountService).isInstanceOf(DiscountService.class);
        assertThat(fixDiscountPrice).isEqualTo(1000);

        int rateDiscountPrice = discountService.discount(member, 20000, "rateDiscountPolicy");
        assertThat(rateDiscountPrice).isEqualTo(2000);

        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println("beanDefinitionName = " + beanDefinitionName);
        }

    }

    static class DiscountService {
        private final Map<String, DiscountPolicy> policyMap;
        private final List<DiscountPolicy> listMap;

        public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> listMap) {
            this.policyMap = policyMap;
            this.listMap = listMap;
            System.out.println("policyMap = " + policyMap);
            System.out.println("listMap = " + listMap);
        }

        public int discount(Member member, int price, String discountCode) {
            DiscountPolicy discountPolicy = policyMap.get(discountCode); // Map 형태라 discount policy를 찾아온다.
            return discountPolicy.discount(member, price);
        }
    }
}
