// The MIT License (MIT)
// Copyright © 2015 Loc Ha

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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.appslandia.common.base.InitializeObject;

/**
 *
 * @author Loc Ha
 *
 */
public class SourceCodeFixer extends InitializeObject {

  private String sourceDir;
  private Function<String, Boolean> srcExt;
  private Consumer<List<String>> srcTransformer;

  @Override
  protected void init() throws Exception {
    Arguments.notNull(this.sourceDir);
    Arguments.notNull(this.srcExt);
  }

  public SourceCodeFixer setSourceDir(String sourceDir) {
    assertNotInitialized();
    this.sourceDir = sourceDir;
    return this;
  }

  public SourceCodeFixer setSrcExt(Function<String, Boolean> srcExt) {
    assertNotInitialized();
    this.srcExt = srcExt;
    return this;
  }

  public SourceCodeFixer setSrcTransformer(Consumer<List<String>> srcTransformer) {
    assertNotInitialized();
    this.srcTransformer = srcTransformer;
    return this;
  }

  public void execute() throws IOException {
    this.initialize();
    final var seq = new AtomicInteger();

    Files.walk(Paths.get(this.sourceDir)).filter(Files::isRegularFile)
        .filter(path -> this.srcExt.apply(FileNameUtils.toUnixPath(path.toString())))
        .forEach(scPath -> processFile(scPath, seq));
  }

  private void processFile(Path scPath, AtomicInteger seq) {
    try {
      var unixPath = FileNameUtils.toUnixPath(scPath.toString());
      System.out.println(STR.fmt("[{}] Handling {}", seq.incrementAndGet(), unixPath));

      var lines = Files.readAllLines(scPath, StandardCharsets.UTF_8);
      lines = lines.stream().map(l -> l.stripTrailing()).collect(Collectors.toList());

      if (this.srcTransformer != null) {
        this.srcTransformer.accept(lines);
      }

      lines.add(System.lineSeparator());
      var linesAsStr = String.join(System.lineSeparator(), lines);

      Files.write(scPath, linesAsStr.getBytes(StandardCharsets.UTF_8));

    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }
}
