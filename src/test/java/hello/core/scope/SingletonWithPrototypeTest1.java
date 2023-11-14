package hello.core.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonWithPrototypeTest1 {

    @Test
    void prototypeFind() {
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(PrototypeBean.class); // 스프링 컨테이너에 직접 등록

        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        prototypeBean1.addCount();

        assertThat(prototypeBean1.getCount()).isEqualTo(1);

        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class); // 스프링 컨테이너에서 같은 객체를 반환할 것 같지만 실제로는 다른 빈 객체를 반환
        prototypeBean2.addCount();

        assertThat(prototypeBean2.getCount()).isNotEqualTo(2);
    }

    @Test
    void singletonClientUsePrototype() {
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);

        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);

        System.out.println("client1 = " + clientBean1.getPrototypeBean());

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isNotEqualTo(1);

        System.out.println("client2 = " + clientBean2.getPrototypeBean());


        System.out.println("clientBean1 = " + clientBean1);
        System.out.println("clientBean2 = " + clientBean2);

    }

    @Scope("singleton")
    @RequiredArgsConstructor
    static class ClientBean {

//        @Autowired
//        private Provider<PrototypeBean> prototypeBeanProvider;

        private final PrototypeBean prototypeBean;

        public int logic() {
//            PrototypeBean prototypeBean = prototypeBeanProvider.get();
//            PrototypeBean prototypeBean = ac.getBean(PrototypeBean.class);
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }

        public PrototypeBean getPrototypeBean() {
            return prototypeBean;
        }
    }


    @Scope("prototype")
    static class PrototypeBean {
        private int count = 0;

        public void addCount() {
            count++;
        }

        public int getCount() {
            return count;
        }

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init = " + this);
        }

        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }
}
