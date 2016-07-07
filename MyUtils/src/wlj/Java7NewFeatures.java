package wlj;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;

public class Java7NewFeatures {
	public static void main(String[] args) {
		int a = 0b1111;
		System.out.printf("二进制数字 0X%x = %d\n", a, a);
		long b = 0x1fff_ffff_ffff_ffffL;
		System.out.println(b);
		long c = 123_456_789;
		System.out.println(c);
		String s = "bbc";
		switch (s) {
		case "abc":
			System.out.println("is abc");
			break;
		case "bbc":
			System.out.println("is bbc");
			break;
		default:
			break;
		}

		char l = 5;
		switch (l) {
		case 1:
			System.out.println("is abc");
			break;
		case 2:
			System.out.println("is bbc");
			break;
		default:
			break;
		}

		List<String> list = new ArrayList<>();
		list.add("A");
		// list.addAll(new ArrayList<>());
		List<? extends String> list2 = new ArrayList<>();
		list.addAll(list2);

		char[] chs = "I love you?  ".toCharArray();
		reverse(chs);
		System.out.println(chs);
	}

	public static void writeToFileZipFileContents(String zipFileName, String outputFileName) throws java.io.IOException {

		java.nio.charset.Charset charset = java.nio.charset.Charset.forName("US-ASCII");
		java.nio.file.Path outputFilePath = java.nio.file.Paths.get(outputFileName);

		// Open zip file and create output file with try-with-resources
		// statement

		try (java.util.zip.ZipFile zf = new java.util.zip.ZipFile(zipFileName);
				java.io.BufferedWriter writer = java.nio.file.Files.newBufferedWriter(outputFilePath, charset)) {

			// Enumerate each entry

			for (Enumeration<? extends ZipEntry> entries = zf.entries(); entries.hasMoreElements();) {

				// Get the entry name and write it to the output file

				String newLine = System.getProperty("line.separator");
				String zipEntryName = ((java.util.zip.ZipEntry) entries.nextElement()).getName() + newLine;
				writer.write(zipEntryName, 0, zipEntryName.length());
			}
		}
	}

	public void rethrowException(String exceptionName) throws Exception {
		try {
			if (exceptionName.equals("First")) {
				throw new FirstException();
			} else {
				throw new SecondException();
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public void rethrowException2(String exceptionName) throws FirstException, SecondException {
		try {
			if (exceptionName.equals("First")) {
				throw new FirstException();
			} else {
				throw new SecondException();
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public void rethrowException3(String exceptionName) throws FirstException, SecondException {
		try {
			if (exceptionName.equals("First")) {
				throw new FirstException();
			} else {
				throw new SecondException();
			}
		} catch (FirstException | SecondException e) {
			// logger.log(e);
			throw e;
		}
	}

	public static void reverse(char[] chs) {
		// 第一个循环，字符为单位全部倒序
		reverse(chs, 0, chs.length);
		// 第二个循环，单词中的字符倒序
		for (int i = 0, j = 0; j <= chs.length; j++) {
			if (j == chs.length || chs[j] == ' ') {
				reverse(chs, i, j);
				i = j+1;
			}
		}
	}

	//对子数组进行倒序
	private static void reverse(char[] chs, int start, int end) {
		for (int i = start, j = end - 1; i < j; i++, j--) {
			char tmp = chs[i];
			chs[i] = chs[j];
			chs[j] = tmp;
		}
	}
}

class FirstException extends Exception {
	private static final long serialVersionUID = 4665392250127723251L;
}

class SecondException extends Exception {
	private static final long serialVersionUID = -1334602408958415635L;
}
