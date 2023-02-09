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
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SourceFixer {

    final String extension;
    final Path srcPath;

    public SourceFixer(String extension, String path) {
	this.extension = extension;
	this.srcPath = Paths.get(path);
    }

    public static void main(String[] args) {
	try {
	    String path = "TODO";

	    new SourceFixer(".java", STR.fmt(path, "main")).fixSource();
	    new SourceFixer(".java", STR.fmt(path, "test")).fixSource();

	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    public void fixSource() throws Exception {
	final AtomicInteger seq = new AtomicInteger();

	Files.walkFileTree(this.srcPath, new SimpleFileVisitor<Path>() {

	    @Override
	    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		return FileVisitResult.CONTINUE;
	    }

	    @Override
	    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		if (!file.toFile().getName().endsWith(SourceFixer.this.extension)) {
		    return FileVisitResult.CONTINUE;
		}

		System.out.println(String.format("[%05d]-Processing file: %s", seq.incrementAndGet(), file.toString()));

		// All lines
		List<String> lines = Files.readAllLines(file);

		// Two consecutive empty lines

		for (int i = lines.size() - 1; i >= 0; i--) {
		    if (lines.get(i).trim().isEmpty()) {

			while ((i - 1 >= 0) && lines.get(i - 1).trim().isEmpty())
			    lines.remove(i - 1);
		    }
		}

		// Remove empty lines before line }

		for (int i = lines.size() - 1; i >= 0; i--) {
		    if (lines.get(i).trim().equals("}")) {

			while ((i - 1 >= 0) && lines.get(i - 1).trim().isEmpty())
			    lines.remove(i - 1);
		    }
		}

		// Add {} to if

		for (int i = lines.size() - 1; i >= 0; i--) {
		    String line = lines.get(i).trim();

		    // if statements
		    if ((line.startsWith("if (") || line.startsWith("else if (") || line.startsWith("} else if (")) && !line.endsWith("{")) {

			// Find line j from i + 1
			// NOT: Empty OR starts with // || && , . (

			int j = i + 1;
			while ((j < lines.size()) && (lines.get(j).trim().isEmpty() || lines.get(j).trim().startsWith("//") || lines.get(j).trim().startsWith("||")
				|| lines.get(j).trim().startsWith("&&") || lines.get(j).trim().startsWith(",") || lines.get(j).trim().startsWith(".")
				|| lines.get(j).trim().startsWith("(")))
			    j++;

			// The line j-1 is still belonging to line i
			// if lines[j-1] doesn't end with {
			// Will add {}

			if ((j - 1 < lines.size()) && !lines.get(j - 1).trim().endsWith("{")) {

			    // Find line k to add {
			    int k = j - 1;
			    while ((k >= 0) && (lines.get(k).trim().isEmpty() || lines.get(k).trim().startsWith("//")))
				k--;

			    if ((k >= 0) && !lines.get(k + 1).trim().startsWith("HtmlUtils.")) {

				// Add {
				lines.set(k, lines.get(k) + " {");

				// Find line l to add }
				int l = j;
				while ((l < lines.size()) && (lines.get(l).trim().isEmpty() || lines.get(l).trim().startsWith("//")))
				    l++;

				// Add }
				lines.add(l + 1, "}");
			    }
			}
		    }
		}

		// Add {} to else

		for (int i = lines.size() - 1; i >= 0; i--) {
		    String line = lines.get(i).trim();

		    // else statements
		    if (line.equals("} else") || line.equals("else")) {

			// Add {
			lines.set(i, line + " {");

			// Find line l to add }
			int j = i + 1;
			while ((j < lines.size()) && (lines.get(j).trim().isEmpty() || lines.get(j).trim().startsWith("//")))
			    j++;

			// Add }
			lines.add(j + 1, "}");
		    }
		}

		// Remove the last blank line.
		// Files.write will add it.

		if (lines.get(lines.size() - 1).strip().isBlank()) {
		    lines.remove(lines.size() - 1);
		}

		// Update the file
		Files.write(file, lines);

		return FileVisitResult.CONTINUE;
	    }
	});

	System.out.println("Done.");
    }
}
