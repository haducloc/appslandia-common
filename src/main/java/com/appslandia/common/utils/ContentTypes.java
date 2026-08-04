// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Loc Ha
 */
public class ContentTypes {

  public static final String TEXT_PLAIN = "text/plain";
  public static final String TEXT_PLAIN_UTF8 = "text/plain;charset=utf-8";

  public static final String TEXT_HTML = "text/html";
  public static final String TEXT_HTML_UTF8 = "text/html;charset=utf-8";

  public static final String TEXT_CSS = "text/css";
  public static final String TEXT_CSS_UTF8 = "text/css;charset=utf-8";

  public static final String TEXT_JAVASCRIPT = "text/javascript";
  public static final String TEXT_JAVASCRIPT_UTF8 = "text/javascript;charset=utf-8";
  public static final String APP_JAVASCRIPT = "application/javascript";
  public static final String APP_JAVASCRIPT_UTF8 = "application/javascript;charset=utf-8";

  public static final String APP_JSON = "application/json";
  public static final String APP_JSON_UTF8 = "application/json;charset=utf-8";
  public static final String APP_JSON_PROBLEM = "application/problem+json";

  public static final String APP_XML = "application/xml";
  public static final String APP_XML_UTF8 = "application/xml;charset=utf-8";
  public static final String APP_XML_PROBLEM = "application/problem+xml";

  public static final String APP_CSV = "application/csv";
  public static final String APP_CSV_UTF8 = "application/csv;charset=utf-8";

  public static final String APP_PDF = "application/pdf";

  public static final String APP_XLS = "application/vnd.ms-excel";
  public static final String APP_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

  public static final String APP_DOC = "application/msword";
  public static final String APP_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

  public static final String APP_PPT = "application/vnd.ms-powerpoint";
  public static final String APP_PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation";

  public static final String IMAGE_PNG = "image/png";
  public static final String IMAGE_JPEG = "image/jpeg";
  public static final String IMAGE_GIF = "image/gif";
  public static final String IMAGE_SVG = "image/svg+xml";
  public static final String IMAGE_WEBP = "image/webp";

  public static final String FONT_WOFF = "font/woff";
  public static final String FONT_WOFF2 = "font/woff2";
  public static final String FONT_TTF = "font/ttf";
  public static final String FONT_OTF = "font/otf";

  public static final String APP_OCTET_STREAM = "application/octet-stream";
  public static final String APP_FORM_URLENCODED = "application/x-www-form-urlencoded";
  public static final String MULTIPART_FORM_DATA = "multipart/form-data";

  public static String probeContentType(String fileName) {
    Arguments.notNull(fileName);
    return probeContentType(Paths.get(fileName));
  }

  public static String probeContentType(Path filePath) {
    Arguments.notNull(filePath);
    try {
      var ct = Files.probeContentType(filePath);
      return (ct != null) ? ct : APP_OCTET_STREAM;
    } catch (IOException ex) {
      return APP_OCTET_STREAM;
    }
  }
}
