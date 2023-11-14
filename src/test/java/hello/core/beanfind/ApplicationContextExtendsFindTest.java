package hello.core.beanfind;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 타입으로 조회 시 부모 타입으로 조회하면 부모를 상속한 자식 타입들도 조회된다.
 * Object 타입으로 조회 시 모든 스프링 빈을 조회함.
 */
public class ApplicationContextExtendsFindTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

    /**
     * 이 경우 역시 타입으로 조회하였을 때 여러 개의 자식이 조회되면 getBean() 호출 시 오류 발생
     */
    @Test
    @DisplayName("부모 타입으로 조회 시, 자식이 둘 이상이 있으면, 중복 오류가 발생한다.")
    void findBeanByParentTypeDuplicate() {
        assertThrows(NoUniqueBeanDefinitionException.class,
                () -> ac.getBean(DiscountPolicy.class));
    }

    /**
     * 그래서 getBean()으로 빈 객체를 조회 시 중복의 여지가 있으면 빈 이름을 지정하는 방법을 사용하자
     */
    @Test
    @DisplayName("부모 타입으로 조회 시, 자식이 둘 이상이 있으면, 빈 이름을 지정하면 된다.")
    void findBeanBySpecificBeanName() {
        DiscountPolicy rateDiscountPolicy = ac.getBean("rateDiscountPolicy", DiscountPolicy.class);
        assertThat(rateDiscountPolicy).isInstanceOf(RateDiscountPolicy.class);
    }

    /**
     * 안좋은 방법: 특정 하위 타입으로 조회
     * 물론 조회에는 문제가 없다. 근데 왜 안 좋은 방법일까?
     * 하위 타입에 의존하게 되면 구현체에 의존하게 되어 나중에 변경이 발생했을 때 문제가 되기 때문이다.
     */
    @Test
    @DisplayName("특정 하위 타입으로 조회")
    void findBeanBySubType() {
        RateDiscountPolicy bean = ac.getBean(RateDiscountPolicy.class);
        assertThat(bean).isInstanceOf(RateDiscountPolicy.class);
    }

    @Test
    @DisplayName("부모 타입으로 모두 조회해보기")
    void findBeanByParentType() {
        Map<String, DiscountPolicy> beansOfType = ac.getBeansOfType(DiscountPolicy.class);
        for (String key : beansOfType.keySet()) {
            System.out.println("key = " + key + " value = " + beansOfType.get(key));
        }
        assertThat(beansOfType.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Object(모든 객체의 조상 타입) 타입으로 조회해보기")
    void findBeanByObjectType() {
        Map<String, Object> beansOfType = ac.getBeansOfType(Object.class);
        for (String key : beansOfType.keySet()) {
            System.out.println("key = " + key + "\nvalue = " + beansOfType.get(key));
        }
    }

    @Configuration
    static class TestConfig {
        @Bean
        public DiscountPolicy rateDiscountPolicy() {
            return new RateDiscountPolicy();
        }

        @Bean
        public DiscountPolicy fixDiscountPolicy() {
            return new FixDiscountPolicy();
        }
    }
}
