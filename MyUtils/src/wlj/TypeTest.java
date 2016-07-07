package wlj;

import java.lang.reflect.Field;

public class TypeTest {

	private int a;

	private static enum ee {
		A, B, C
	}

	public static void main(String[] args) throws NoSuchFieldException, SecurityException {
		Field field = TypeTest.class.getDeclaredField("a");
		System.out.println("field type:" + field.getType());
		System.out.println("field isPrimitive:" + field.getType().isPrimitive());
		System.out.println("field isAssignableFrom:" + Number.class.isAssignableFrom(field.getType()));
		System.out.println("enum isAssignableFrom:" + Enum.class.isAssignableFrom(ee.A.getClass()));
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}
}
