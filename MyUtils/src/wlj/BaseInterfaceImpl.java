package wlj;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public abstract class BaseInterfaceImpl<T> implements BaseInterface<T> {

	private Class<T> modelClass;

	public BaseInterfaceImpl() {
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		if(genericSuperclass instanceof ParameterizedType) {
			Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
			for (Type type : actualTypeArguments) {
				if(type instanceof Class<?>) {
					System.out.println(((Class) type).isAssignableFrom(BigTest.class));;
					modelClass = (Class<T>) type;
				}
				System.out.println(type);
			}
			System.out.println("####");
			System.out.println(((ParameterizedType) genericSuperclass).getOwnerType());
			System.out.println("####");
			System.out.println(((ParameterizedType) genericSuperclass).getRawType());
		}
		System.out.println("---");
		Type[] genericInterfaces = this.getClass().getGenericInterfaces();
		for (Type type : genericInterfaces) {
			System.out.println(type);
		}
		System.out.println("---");
		System.out.println(this.getClass().getGenericSuperclass());
		System.out.println("---");
		TypeVariable<?>[] typeParameters = this.getClass().getTypeParameters();
		for (TypeVariable<?> typeVariable : typeParameters) {
			System.out.println(typeVariable);
			System.out.println(typeVariable.getGenericDeclaration());
		}
	}
}
