import com.baic.bcl.util.UUID;


public class UUIDGEN {

	public static void main(String[] args) {
		for (int i = 0; i < 531; i++) {
			System.out.println(UUID.get());
		}
	}

}
