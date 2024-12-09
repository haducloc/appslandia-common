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

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.appslandia.common.base.InitializeObject;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class TextTransform extends InitializeObject {

  private CaseType caseType;
  private Locale locale;
  private WordTransform wordTransform;

  @Override
  protected void init() throws Exception {
    this.caseType = ValueUtils.valueOrAlt(this.caseType, CaseType.TITLE);
    this.locale = ValueUtils.valueOrAlt(this.locale, Locale.ROOT);
  }

  public String transform(String str) {
    initialize();

    str = StringUtils.trimToNull(str);
    if (str == null) {
      return null;
    }

    CaseTransform transform = getTransform(this.caseType);
    return transform.doTransform(str);
  }

  public TextTransform setCaseType(CaseType caseType) {
    assertNotInitialized();
    this.caseType = caseType;
    return this;
  }

  public TextTransform setLocale(Locale locale) {
    assertNotInitialized();
    this.locale = locale;
    return this;
  }

  public TextTransform setWordTransform(WordTransform wordTransform) {
    assertNotInitialized();
    this.wordTransform = wordTransform;
    return this;
  }

  protected CaseTransform getTransform(CaseType caseType) {
    switch (caseType) {
    case TITLE:
      return new TitleTransform();
    case UPPER:
      return new UpperTransform();
    case LOWER:
      return new LowerTransform();
    case CAMEL:
      return new CamelTransform();
    case PASCAL:
      return new PascalTransform();
    case SNAKE:
      return new SnakeTransform();
    case SCREAMING_SNAKE:
      return new ScreamingSnakeTransform();
    case KEBAB:
      return new KebabTransform();
    case TRAIN:
      return new TrainTransform();
    default:
      throw new IllegalArgumentException("Unsupported case type: " + caseType);
    }
  }

  interface CaseTransform {
    String doTransform(String str);
  }

  interface WordTransform {
    String doTransform(String word);
  }

  enum CaseType {
    // The Quick Brown Fox Jumps Over the Lazy Dog
    TITLE, UPPER, LOWER,

    // theQuickBrownFoxJumpsOverTheLazyDog
    CAMEL,

    // TheQuickBrownFoxJumpsOverTheLazyDog
    PASCAL,

    // the_quick_brown_fox_jumps_over_the_lazy_dog
    SNAKE,

    // THE_QUICK_BROWN_FOX_JUMPS_OVER_THE_LAZY_DOG
    SCREAMING_SNAKE,

    // the-quick-brown-fox-jumps-over-the-lazy-dog
    KEBAB,

    // The-Quick-Brown-Fox-Jumps-Over-The-Lazy-Dog
    TRAIN
  }

  // Transform Implementations
  // The Quick Brown Fox Jumps Over the Lazy Dog

  class TitleTransform implements CaseTransform {

    private static final Set<String> EXCLUDED_WORDS;

    static {
      Set<String> excluded = new HashSet<>();

      excluded.add("a");
      excluded.add("an");
      excluded.add("and");
      excluded.add("as");
      excluded.add("at");
      excluded.add("but");
      excluded.add("by");
      excluded.add("for");
      excluded.add("if");
      excluded.add("in");
      excluded.add("nor");
      excluded.add("of");
      excluded.add("on");
      excluded.add("or");
      excluded.add("so");
      excluded.add("the");
      excluded.add("to");
      excluded.add("up");
      excluded.add("yet");
      excluded.add("with");

      EXCLUDED_WORDS = Collections.unmodifiableSet(excluded);
    }

    @Override
    public String doTransform(String str) {
      String[] words = str.split("\\s+");
      StringBuilder result = new StringBuilder(str.length());

      for (String word : words) {
        if (!word.equals(word.toUpperCase(locale))) {

          if (result.length() == 0) {
            word = StringUtils.firstUpperCase(word, locale);
          } else {

            if (EXCLUDED_WORDS.contains(word.toLowerCase(locale))) {
              word = word.toLowerCase(locale);
            } else {
              word = StringUtils.firstUpperCase(word, locale);
            }
          }
        }

        if (wordTransform != null) {
          word = wordTransform.doTransform(word);
        }
        if (word == null)
          continue;

        if (result.length() == 0) {
          result.append(word);
        } else {
          result.append(" ").append(word);
        }
      }
      return result.length() > 0 ? result.toString() : null;
    }
  }

  class UpperTransform implements CaseTransform {
    @Override
    public String doTransform(String str) {
      String[] words = str.toUpperCase(locale).split("\\s+");
      StringBuilder result = new StringBuilder(str.length());

      for (String word : words) {

        if (wordTransform != null) {
          word = wordTransform.doTransform(word);
        }
        if (word == null)
          continue;

        if (result.length() == 0) {
          result.append(word);
        } else {
          result.append(" ").append(word);
        }
      }
      return result.length() > 0 ? result.toString().trim() : null;
    }
  }

  class LowerTransform implements CaseTransform {
    @Override
    public String doTransform(String str) {
      String[] words = str.toLowerCase(locale).split("\\s+");
      StringBuilder result = new StringBuilder(str.length());

      for (String word : words) {

        if (wordTransform != null) {
          word = wordTransform.doTransform(word);
        }
        if (word == null)
          continue;

        if (result.length() == 0) {
          result.append(word);
        } else {
          result.append(" ").append(word);
        }
      }
      return result.length() > 0 ? result.toString().trim() : null;
    }
  }

  // theQuickBrownFoxJumpsOverTheLazyDog

  class CamelTransform implements CaseTransform {
    @Override
    public String doTransform(String str) {
      String[] words = str.split("\\s+");
      StringBuilder result = new StringBuilder(str.length());

      for (String word : words) {
        if (!word.equals(word.toUpperCase(locale))) {

          if (result.length() > 0) {
            word = word.toLowerCase(locale);
            word = StringUtils.firstUpperCase(word, locale);
          }
        }

        if (wordTransform != null) {
          word = wordTransform.doTransform(word);
        }
        if (word == null)
          continue;

        result.append(word);
      }
      return result.length() > 0 ? result.toString() : null;
    }
  }

  // TheQuickBrownFoxJumpsOverTheLazyDog

  class PascalTransform implements CaseTransform {
    @Override
    public String doTransform(String str) {
      String[] words = str.split("\\s+");
      StringBuilder result = new StringBuilder(str.length());

      for (String word : words) {

        if (!word.equals(word.toUpperCase(locale))) {
          word = word.toLowerCase(locale);
          word = StringUtils.firstUpperCase(word, locale);
        }

        if (wordTransform != null) {
          word = wordTransform.doTransform(word);
        }
        if (word == null)
          continue;

        result.append(word);
      }
      return result.length() > 0 ? result.toString() : null;
    }
  }

  // the_quick_brown_fox_jumps_over_the_lazy_dog

  class SnakeTransform implements CaseTransform {
    @Override
    public String doTransform(String str) {
      String[] words = str.toLowerCase(locale).split("\\s+");
      StringBuilder result = new StringBuilder(str.length());

      for (String word : words) {

        if (wordTransform != null) {
          word = wordTransform.doTransform(word);
        }
        if (word == null)
          continue;

        if (result.length() == 0) {
          result.append(word);
        } else {
          result.append("_").append(word);
        }
      }
      return result.length() > 0 ? result.toString() : null;
    }
  }

  // THE_QUICK_BROWN_FOX_JUMPS_OVER_THE_LAZY_DOG

  class ScreamingSnakeTransform implements CaseTransform {
    @Override
    public String doTransform(String str) {
      String[] words = str.toUpperCase(locale).split("\\s+");
      StringBuilder result = new StringBuilder(str.length());

      for (String word : words) {

        if (wordTransform != null) {
          word = wordTransform.doTransform(word);
        }
        if (word == null)
          continue;

        if (result.length() == 0) {
          result.append(word);
        } else {
          result.append("_").append(word);
        }
      }
      return result.length() > 0 ? result.toString() : null;
    }
  }

  // the-quick-brown-fox-jumps-over-the-lazy-dog

  class KebabTransform implements CaseTransform {
    @Override
    public String doTransform(String str) {
      String[] words = str.toLowerCase(locale).split("\\s+");
      StringBuilder result = new StringBuilder(str.length());

      for (String word : words) {

        if (wordTransform != null) {
          word = wordTransform.doTransform(word);
        }
        if (word == null)
          continue;

        if (result.length() == 0) {
          result.append(word);
        } else {
          result.append("-").append(word);
        }
      }
      return result.length() > 0 ? result.toString() : null;
    }
  }

  // The-Quick-Brown-Fox-Jumps-Over-The-Lazy-Dog

  class TrainTransform implements CaseTransform {
    @Override
    public String doTransform(String str) {
      String[] words = str.split("\\s+");
      StringBuilder result = new StringBuilder(str.length());

      for (String word : words) {
        if (!word.equals(word.toUpperCase(locale))) {

          word = word.toLowerCase(locale);
          word = StringUtils.firstUpperCase(word, locale);
        }

        if (wordTransform != null) {
          word = wordTransform.doTransform(word);
        }
        if (word == null)
          continue;

        if (result.length() == 0) {
          result.append(word);
        } else {
          result.append("-").append(word);
        }
      }
      return result.length() > 0 ? result.toString() : null;
    }
  }
}
