import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 把npmbs的access log合并为一个
 * 
 * @author wanglongjiang
 *
 */
public class NpmbsLogNto1 {

	private static Pattern pattern = Pattern.compile("(?<ip>[\\d.]+) - - \\[[^\\]]+\\] \"\\w+ (?<url>[^ ]+)[^\"]+\" (?<status>\\d{3})");
	private static Pattern dateP = Pattern.compile("(\\d{4}-\\d{1,2})-\\d{1,2}");
	private static Pattern ap = Pattern.compile("/(?<model>[^/]+)/(?<action>[\\w\\-]+).*");

	public static void main(String[] args) throws IOException {
		try (Stream<Path> stream = Files.walk(Paths.get("/Users/wanglongjiang/Desktop/log")).parallel();
				PrintWriter writer = new PrintWriter(new FileWriter("/Users/wanglongjiang/Desktop/model.csv"));) {
			HashMap<String, Integer> loginCount = new HashMap<String, Integer>();
			stream.filter(path -> !Files.isDirectory(path) && path.toString().endsWith(".txt")).flatMap(NpmbsLogNto1::processFile).forEach(record -> {
				if (loginCount.containsKey(record))
					loginCount.put(record, loginCount.get(record) + 1);
				else {
					loginCount.put(record, 1);
				}
			});
			for (String key : loginCount.keySet()) {
				System.out.println(key + "," + loginCount.get(key));
			}
		}
	}

	private static Stream<String> processFile(Path file) {
		try {
			Stream<String> lines = Files.lines(file, Charset.forName("iso8859-1")).parallel().filter(line -> !line.startsWith("10.10.3.33"));
			Stream<Matcher> ms = lines.map(line -> pattern.matcher(line)).filter(m -> m.find()).filter(m -> m.group("status").equals("200"));
			// ms = ms.filter(m -> m.group("url").equals("/login"));
			ms = ms.filter(m -> !m.group("url").startsWith("/js")).filter(m -> !m.group("url").startsWith("/css"))
					.filter(m -> !m.group("url").startsWith("/img"));
			ms = ms.filter(m -> !m.group("url").startsWith("/favicon.ico")).filter(m -> !m.group("url").equals("/"))
					.filter(m -> !m.group("url").startsWith("/webservice")).filter(m -> !m.group("url").startsWith("/system/notice"))
					.filter(m -> !m.group("url").startsWith("/basicdata/dictionary/")).filter(m -> !m.group("url").startsWith("/system/organization"));
			ms = ms.filter(m -> ap.matcher(m.group("url")).matches());
			Matcher dm = dateP.matcher(file.toString());
			dm.find();
			String date = dm.group(1);
			return ms.map(m -> {
				String url = m.group("url");
				Matcher matcher = ap.matcher(url);
				matcher.find();
				return date + "," + matcher.group("model") + "/" + matcher.group("action");
			});
		} catch (Exception e) {
			System.err.println(file);
			throw new RuntimeException(e);
		}
	}
}
