package wlj;

import java.io.UnsupportedEncodingException;

public class StringTest {

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println("abc".getBytes().length);
		System.out.println("中国人".getBytes("utf8").length);
	}

}
