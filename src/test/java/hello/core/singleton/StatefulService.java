package hello.core.singleton;

public class StatefulService {
    private int price;

    // Stateful 한 설계 -> 오류 가능성 높음
//    public void order(String name, int price) {
//        System.out.println("name = " + name + " price = " + price);
//        this.price = price; // 여기가 문제가 된다.
//    }

    // Stateless 한 설계 -> 공유되지 않는 지역변수 등을 활용
    public int order(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
        return price;
    }

    public int getPrice() {
        return price;
    }
}
