package wlj;

import com.baic.bcl.util.UUID;

public class UUIDgen {

	public static void main(String[] args) {
		for (int i = 0; i < 18; i++) {
			System.out.println(UUID.get());
		}
	}

}
