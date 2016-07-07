import java.io.File;


public class FilePathTest {

	public static void main(String[] args) {
		File file =new File("Desktop/url.txt");
		System.out.println(file.getPath());
		System.out.println(file.getAbsolutePath());
	}

}
