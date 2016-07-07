package wlj;

import com.baic.bcl.util.VOUtils;

public class PrivateFieldRead {

	public static void main(String[] args) {
		PrivateFieldBean bean = new PrivateFieldBean();
		Object property = VOUtils.getProperty(bean, "token");
		System.out.println(property);
	}

}
