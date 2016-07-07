package wlj;

import java.util.regex.Pattern;

public class PatternTest1 {

	public static void main(String[] args) {
		Pattern startAnd = Pattern.compile("^\\s*and.*", Pattern.CASE_INSENSITIVE);
		System.out.println(startAnd.matcher(" and sefeef").matches());
		System.out.println(startAnd.matcher("and sefeef").matches());
		System.out.println(startAnd.matcher(" 1=1 and sefeef").matches());
		System.out.println(startAnd.matcher("a=b").matches());
	}

}
