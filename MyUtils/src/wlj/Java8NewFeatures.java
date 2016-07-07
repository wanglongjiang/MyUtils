package wlj;

import java.util.Comparator;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Java8NewFeatures {
	public static void main(String[] args) {
		String str = "abc";
		Runnable run = () -> System.out.println(str);
		run.run();

		Comparator<String> cmptor = (a, b) -> a.compareTo(b);
		System.out.println(cmptor.compare("ab", "ac"));

		Function<Object, String> tostr = Object::toString;
		System.out.println(tostr.apply(66));
		Function<String, Integer> toint = Integer::valueOf;
		System.out.println(toint.apply("78"));

		Predicate<String> pred = (a) -> Boolean.valueOf(a);
		System.out.println(pred.test("true"));

		Consumer<String> consumer = (a) -> System.out.println(a);
		consumer.accept("ddd");

		Supplier<String> supplier = () -> "efd";
		System.out.println(supplier);
		System.out.println(supplier.get());

		BinaryOperator<String> biOpt = (a, b) -> a + b;
		System.out.println(biOpt.apply("ddd", "eee"));
	}
}
