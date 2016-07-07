import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class WmsLog {
	public static void main(String[] args) throws IOException {
		Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3} \\[.+\\] (?<error>ERROR.+)");
		HashMap<String, Integer> counter = new HashMap<String, Integer>();
		try (Stream<String> lines = Files.lines(Paths.get("/Users/wanglongjiang/Desktop/wms_nolib_trace-all_error.log"));) {
			lines.map(line -> {
				Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					return matcher.group("error");
				}
				return null;
			}).filter(line -> line != null).forEach(line -> {
				if (counter.containsKey(line)) {
					counter.put(line, counter.get(line) + 1);
				} else {
					counter.put(line, 1);
				}
			});
		}

		for (String key : counter.keySet()) {
			System.out.println(key + "\t" + counter.get(key));
		}
	}
}
