
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
import java.util.regex.Pattern;

public class CopyrightSourceExport {

	public static void main(String[] args) throws IOException {
		String srcFolderPath = "/Users/wanglongjiang/Workset/workspace/cpm/src/main/java";
		//String srcFolderPath = "z:/ss";
		String outputFilePath = "/Users/wanglongjiang/copyrightsource.txt";
		File folder = new File(srcFolderPath);
		List<File> files = eachFolder(folder, new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.getName().endsWith("java") || file.isDirectory();
			}
		});
		try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath))) {
			for (File file : files) {
				writer.println("//" + file.getName());
				printFile(writer, file);
			}
		}
	}

	private static Pattern spaceLine = Pattern.compile("^\\s*$");
	private static Pattern mulitCommentBegin = Pattern.compile(".*/\\*.*");
	private static Pattern mulitCommentEnd = Pattern.compile("\\*/");
	private static Pattern singleComment = Pattern.compile("^\\s*//.*");

	private static void printFile(PrintWriter writer, File file) throws FileNotFoundException, IOException {
		try (LineNumberReader reader = new LineNumberReader(new FileReader(file))) {
			int mulitCommentDeep = 0;
			for (String str = reader.readLine(); str != null; str = reader.readLine()) {
				if (spaceLine.matcher(str).matches()) {
					continue;
				}
				if (singleComment.matcher(str).matches()) {
					continue;
				}
				if (mulitCommentBegin.matcher(str).matches()) {
					mulitCommentDeep++;
				}
				if (mulitCommentEnd.matcher(str).find()) {
					mulitCommentDeep--;
					continue;
				}
				if (mulitCommentDeep <= 0) {
					writer.println(str);
				}
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
