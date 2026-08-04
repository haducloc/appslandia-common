// Licensed under the MIT License.
// See LICENSE file in the project root for details.

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

import com.appslandia.common.base.InitializingObject;

/**
 *
 * @author Loc Ha
 *
 */
public class SourceCodeFixer extends InitializingObject {

  private String sourceDir;
  private Function<String, Boolean> srcExt;
  private Consumer<List<String>> srcTransformer;

  @Override
  protected void init() throws Exception {
    Arguments.notNull(sourceDir);
    Arguments.notNull(srcExt);
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
    initialize();
    final var seq = new AtomicInteger();

    Files.walk(Paths.get(sourceDir)).filter(Files::isRegularFile)
        .filter(path -> srcExt.apply(FileNameUtils.toUnixPath(path.toString())))
        .forEach(scPath -> processFile(scPath, seq));
  }

  private void processFile(Path scPath, AtomicInteger seq) {
    try {
      var unixPath = FileNameUtils.toUnixPath(scPath.toString());
      System.out.println(STR.fmt("[{}] Handling {}", seq.incrementAndGet(), unixPath));

      var lines = Files.readAllLines(scPath, StandardCharsets.UTF_8);
      lines = lines.stream().map(l -> l.stripTrailing()).collect(Collectors.toList());

      if (srcTransformer != null) {
        srcTransformer.accept(lines);
      }

      lines.add(System.lineSeparator());
      var linesAsStr = String.join(System.lineSeparator(), lines);

      Files.write(scPath, linesAsStr.getBytes(StandardCharsets.UTF_8));

    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }
}
