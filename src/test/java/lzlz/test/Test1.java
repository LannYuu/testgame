package lzlz.test;


import java.util.ArrayList;
import java.util.List;

public class Test1 {
    public static void main(String args[]){
        List<A> list = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder("ABCDE");
        stringBuilder.insert(1, "0");
        System.out.println(stringBuilder.toString());
//        list.add(new A("Abc","hello"));
//        System.out.println(list.contains(new A("Abc", "hello")));
//        System.out.println(list.remove(new A("Abca", "hello")));
    }
}

class A{
    String name;
    String value;

    public A(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof A && ((A) obj).name.equals(this.name);
    }
}
