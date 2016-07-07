package wlj;

import java.io.File;

import com.baic.bcl.util.FileCodeConverter;

public class FileConvert {
	public static void main(String[] args) {
		File src = new File("Z:\\workflow");
		File destDir = new File("Z:\\workflow2");
		FileCodeConverter.convert(src, "utf-8", destDir, "gbk");
	}
}
