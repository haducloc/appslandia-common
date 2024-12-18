// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.appslandia.common.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Pattern;

import com.appslandia.common.base.InitializeObject;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SourceCodeBrFixer extends InitializeObject {

  private String sourceDir;
  private Function<String, Boolean> srcExt;

  @Override
  protected void init() throws Exception {
    Asserts.notNull(this.sourceDir);
    Asserts.notNull(this.srcExt);
  }

  public SourceCodeBrFixer setSourceDir(String sourceDir) {
    assertNotInitialized();
    this.sourceDir = sourceDir;
    return this;
  }

  public SourceCodeBrFixer setSrcExt(Function<String, Boolean> srcExt) {
    assertNotInitialized();
    this.srcExt = srcExt;
    return this;
  }

  public void execute() throws IOException {
    this.initialize();

    final AtomicInteger seq = new AtomicInteger();

    Files.walk(Paths.get(this.sourceDir)).filter(Files::isRegularFile)
        .filter(path -> this.srcExt.apply(FileNameUtils.toUnixPath(path.toString())))
        .forEach(scPath -> processFile(scPath, seq));
  }

  static String pattern = "^(public|private|protected)?\\s*(static\\s+)?[^\s]+\\s+[^\s]+\\s*\\(\\s*([^\s]+\\s+[^\s]+(\\s*,\\s*[^\s]+\\s+[^\s]+)*)?\\s*\\)(\\s+throws\\s+[^\s]+(\\s*,\\s*[^\s]+)*)?\\s*\\{$";

  private void processFile(Path scPath, AtomicInteger seq) {
    try {
      String unixPath = FileNameUtils.toUnixPath(scPath.toString());
      System.out.println(STR.fmt("[{}] Handling {}", seq.incrementAndGet(), unixPath));

      List<String> lines = Files.readAllLines(scPath, StandardCharsets.UTF_8);

      Pattern p = Pattern.compile(pattern);

      int i = 0;
      while (true) {
        if (i == lines.size()) {
          break;
        }
        String line_i = lines.get(i).strip();

        if (p.matcher(line_i).matches()) {
          int j = i + 1;
          while (true) {
            if (j == lines.size()) {
              break;
            }

            String line_j = lines.get(j).strip();
            if (line_j.isEmpty() || line_j.equals("initialize();") || line_j.equals("this.initialize();")
                || line_j.equals("super.initialize();")) {
              j++;
            } else if (line_j.startsWith("Asserts.")) {
              lines.set(j, "Arguments." + line_j.substring(8));
              j++;
            } else {
              break;
            }
          }
          i = j + 1;
        } else {
          i++;
        }
      }

      if (!lines.get(lines.size() - 1).strip().isEmpty()) {
        lines.add(System.lineSeparator());
      }

      String linesAsStr = String.join(System.lineSeparator(), lines);
      Files.write(scPath, linesAsStr.getBytes(StandardCharsets.UTF_8));

    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }

  public static void main(String[] args) {
    try {
      SourceCodeBrFixer fixer = new SourceCodeBrFixer();

      fixer.setSourceDir("C:\\Workspace2\\javaProjects\\appslandia-common");
      fixer.setSrcExt(fn -> fn.endsWith(".java") && !fn.contains("SourceCodeBrFixer"));
      fixer.execute();

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
