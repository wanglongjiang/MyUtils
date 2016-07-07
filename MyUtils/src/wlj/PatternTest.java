package wlj;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternTest {
//	private static Pattern str = Pattern.compile("'(.*?(?<!\\\\))'");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Pattern WIN_DRV_PATH = Pattern.compile("\\p{Alpha}:[\\\\/].*");
		System.out.println(WIN_DRV_PATH.matcher("z:/e").matches());
		System.out.println(WIN_DRV_PATH.matcher("z:\\e").matches());
		// String s = "'555'efefe'ssss";
		// Matcher m = str.matcher(s);
		// System.out.println(m.lookingAt());
		// System.out.println(m.group(1));
		// System.out.println("adfa\\tadfsa".replaceAll("\\\\(t)", "\\$1"));
		//
		// ArrayDeque<String> deque = new ArrayDeque<>();
		// deque.add(null);
		// Pattern p = Pattern.compile("(\\p{Alpha}+)\\d+");
		// String str = "A51+B51*100+50%";
		// String result = p.matcher(str).replaceAll("$1"+"123");
		// System.out.println(result);
		String str = "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/icon.css\">\n"
				+ "<script type=\"text/javascript\">\n" + "var time=1800;\n"
				+ "window.parent.frames[\"user\"].document.form.time.value=time;\n" + "</script>\n"
				+ "<script type=\"text/javascript\" src=\"js/jquery-1.10.1.min.js\"></script>\n"
				+ "<script type=\"text/javascript\" src=\"js/jquery.easyui.min.js\"></script>\n"
				+ "<script type=\"text/javascript\" src=\"js/locale/easyui-lang-zh_CN.js\"></script>\n"
				+ "<script type=\"text/javascript\" src=\"js/common.js\"></script>\n"
				+ "<script type=\"text/javascript\">\n" + "var supplierSize = eval('3');\n" + "var rowObj = null;\n"
				+ "</script>\n" + "<style type=\"text/css\">";
		StringBuilder sb = new StringBuilder(str);
		StringBuilder sb2 = new StringBuilder();
		Pattern script = Pattern.compile("<script.*?</script>", Pattern.DOTALL);
		Matcher m = script.matcher(sb);
		int pos = 0;
		while (m.find()) {
			sb2.append(sb.subSequence(pos, m.start()));
			pos = m.end();
		}
		if (pos < sb.length())
			sb2.append(sb.substring(pos));
		System.out.println(sb2);
	}

}
