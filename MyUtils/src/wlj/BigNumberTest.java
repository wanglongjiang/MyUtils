package wlj;

import java.math.BigDecimal;

public class BigNumberTest {
	public static void main(String[] args) {
		BigDecimal a = new BigDecimal("6033");
		BigDecimal b = new BigDecimal("21.75");
		BigDecimal c = new BigDecimal("0.2");
		BigDecimal d = new BigDecimal("3");
		System.out.println(a.divide(b, 10, BigDecimal.ROUND_HALF_UP).multiply(c).multiply(d));
	}
}
