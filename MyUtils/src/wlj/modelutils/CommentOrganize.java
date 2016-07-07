package wlj.modelutils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baic.bcl.Filter;
import com.baic.bcl.Processor;
import com.baic.bcl.util.FileUtils;
import com.baic.bcl.util.StringUtils;

/**
 * 注释整理
 * 
 * @author wanglongjiang
 *
 */
public class CommentOrganize {

	public static final String impComment = "import com.baic.bcl.annotation.Comment;";
	public static final String commentTemplate = "@Comment(\"{0}\")";

	private static final Pattern importLine = Pattern.compile("^import\\s+[\\w.]+;$");

	private static final Pattern classDefLine = Pattern.compile("^public class .*");
	private static final Pattern propertyLine = Pattern
			.compile("^\\s+private\\s+(?<type>[\\w><]+)\\s+(?<property>.+);(\\s*//\\s*(?<lineEndComment>\\S*).*)?$");

	private static final Pattern methodLine = Pattern.compile("^\\s*public [A-Z].*");

	private static final Pattern docCommentLineSt = Pattern.compile("^\\s*/\\*\\*[\\*\\s]*");
	private static final Pattern docCommentLine = Pattern.compile("^\\s*\\*\\s*(?<comment>[^\\s]+).*");
	private static final Pattern docCommentLineEnd = Pattern.compile("^\\s*\\*/");
	private static final Pattern singleDocCommentLine = Pattern.compile("^\\s*/\\*\\*\\s*(?<comment>[^\\s]+)\\s*\\*/");
	private static final Pattern normalCommentLine = Pattern.compile("^\\s*//\\s*(?<comment>[^\\s]+)");

	private static final Pattern commentAnnotationLine = Pattern.compile("^\\s*@Comment\\(\"(?<comment>[^\"]*)\"\\).*");

	public static void main(String[] args) {
		String lineEnd = System.getProperty("line.separator", "\n");
		File srcDir = new File("Z:/workspace/ehr-common-jar/src/main/java/com/baic/hr/model");
		File destDir = new File("Z:/model");
		destDir.mkdirs();
		Map<String, File> parma = new HashMap<>();
		FileUtils.eachFileAndDirs(srcDir, new Processor<File>() {
			@Override
			public void process(File o) {
				if (o.isDirectory()) {
					String thePath = o.getAbsolutePath();
					String rootDir = srcDir.getAbsolutePath();
					String path = thePath.substring(rootDir.length());
					if (StringUtils.notEmpty(path)) {
						String destPath = StringUtils.concatAsPath(destDir.getAbsolutePath(), path);
						File currentDir = new File(destPath);
						currentDir.mkdirs();
						parma.put("currentDir", currentDir);
					} else {
						parma.put("currentDir", destDir);
					}
				} else {
					try (LineNumberReader reader = new LineNumberReader(new FileReader(o));
							Writer writer = new FileWriter(new File(parma.get("currentDir"), o.getName()));) {
						ArrayList<String> fileData = new ArrayList<>();

						int lastImportLineNum = 0;
						boolean hasImportComment = false;
						boolean hasClassCommentAnn = false;
						int lineNum = 0;
						int classDefLineNum = 0;
						boolean docCommentStart = false;
						String docComment = null;
						String singleComment = null;
						boolean hasPropCommentAnn = false;
						boolean end = false;

						for (String line = reader.readLine(); line != null; line = reader.readLine()) {
							fileData.add(line);
							if (end) {
								continue;
							}
							if (classDefLineNum == 0) {
								if (classDefLine.matcher(line).matches()) {
									classDefLineNum = lineNum;
								} else if (importLine.matcher(line).matches()) {
									lastImportLineNum = lineNum;
									if (impComment.equals(line)) {
										hasImportComment = true;
										docComment = null;
									}
								} else if (commentAnnotationLine.matcher(line).matches()) {
									hasClassCommentAnn = true;
									docComment = null;
								} else if (docCommentLineSt.matcher(line).matches()) {
									docCommentStart = true;
									docComment = null;
								} else if (docCommentStart && docCommentLineEnd.matcher(line).matches()) {
									docCommentStart = false;
								} else if (docCommentStart && docComment == null
										&& docCommentLine.matcher(line).matches()) {
									Matcher m = docCommentLine.matcher(line);
									m.find();
									docComment = m.group("comment");
								}
							} else {
								if (!hasImportComment) {
									fileData.add(lastImportLineNum + 1, impComment);
									classDefLineNum++;
									hasImportComment = true;
								}
								if (!hasClassCommentAnn) {
									if (StringUtils.notEmpty(docComment)) {
										fileData.add(classDefLineNum,
												MessageFormat.format(commentTemplate, docComment));
									}
									hasClassCommentAnn = true;
									docComment = null;
								}

								if (methodLine.matcher(line).matches()) {
									end = true;
								}

								if (singleDocCommentLine.matcher(line).matches()) {
									Matcher m = singleDocCommentLine.matcher(line);
									m.find();
									docComment = m.group("comment");
								} else if (docCommentLineSt.matcher(line).matches()) {
									docCommentStart = true;
									docComment = null;
								} else if (docCommentStart && docCommentLineEnd.matcher(line).matches()) {
									docCommentStart = false;
								} else if (docCommentStart && docComment == null
										&& docCommentLine.matcher(line).matches()) {
									Matcher m = docCommentLine.matcher(line);
									m.find();
									docComment = m.group("comment");
								} else if (normalCommentLine.matcher(line).matches()) {
									Matcher m = normalCommentLine.matcher(line);
									m.find();
									singleComment = m.group("comment");
								} else if (commentAnnotationLine.matcher(line).matches()) {
									hasPropCommentAnn = true;
								} else if (propertyLine.matcher(line).matches()) {
									if (!hasPropCommentAnn) {
										Matcher m = propertyLine.matcher(line);
										m.find();
										String comment = m.group("lineEndComment");
										if (StringUtils.notEmpty(docComment)) {
											comment = docComment;
										} else if (StringUtils.notEmpty(singleComment)) {
											comment = singleComment;
										}
										if (StringUtils.notEmpty(comment)) {
											fileData.add(fileData.size() - 1,
													"\t" + MessageFormat.format(commentTemplate, comment));
										}
										docComment = null;
										singleComment = null;
									}
									hasPropCommentAnn = false;
								}
							}
							lineNum++;
						}
						for (String line : fileData) {
							writer.write(line);
							writer.write(lineEnd);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
		}, new Filter<File>() {
			@Override
			public boolean accept(File e) {
				return e.isDirectory() || e.getName().endsWith(".java");
			}
		});
	}
}
