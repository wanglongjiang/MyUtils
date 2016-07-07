import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CopyrightSourceExport2 {

	public static void main(String[] args) throws IOException {
		String srcFolderPath = "/Users/wanglongjiang/Workset/workspace/cpm/src/main/webapp";
		// String srcFolderPath = "z:/ss";
		String outputFilePath = "/Users/wanglongjiang/copyrightsource2.txt";
		File folder = new File(srcFolderPath);
		List<File> files = eachFolder(folder, new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.getName().endsWith("jsp") || file.getName().endsWith("html") || file.isDirectory();
			}
		});
		try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath))) {
			for (File file : files) {
				writer.println("//" + file.getName());
				printFile(writer, file);
			}
		}
	}

	private static void printFile(PrintWriter writer, File file) throws FileNotFoundException, IOException {
		try (LineNumberReader reader = new LineNumberReader(new FileReader(file))) {
			for (String str = reader.readLine(); str != null; str = reader.readLine()) {
				writer.println(str);
			}
		}
	}

	private static ArrayList<File> eachFolder(File folder, FileFilter filter) {
		ArrayList<File> result = new ArrayList<>();
		File[] files = folder.listFiles(filter);
		for (File file : files) {
			if (file.isDirectory()) {
				result.addAll(eachFolder(file, filter));
			} else {
				result.add(file);
			}
		}
		return result;
	}
}
