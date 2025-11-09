package shopping;

import java.util.function.BiFunction;
import java.util.function.Function;

public class ReaderUtil {
    public static double readPrice(String price){
        return Double.parseDouble(price);
    }

    public static Item readItem(String name, String cat){
        return new Item(name,ItemCategory.valueOf(cat));
    }

    public static <A,B,C,D,E> TriFunction<A,B,D,Pair<C, E>> parsePair(BiFunction<A,B,C> f1, Function<D,E> f2){
        return (a,b,d) -> new Pair<>(f1.apply(a, b), f2.apply(d));
    }

    public static Pair<Item,Double> readLine(String line){
        String[] data = line.split(",");

        TriFunction<String,String,String,Pair<Item,Double>> parse = parsePair((n, c) -> readItem(n,c),(p)->readPrice(p));

        return parse.apply(data[0],data[1],data[2]);
    }
}
