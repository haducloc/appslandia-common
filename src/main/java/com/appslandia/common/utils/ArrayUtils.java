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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.function.BiFunction;

/**
 *
 * @author Loc Ha
 *
 */
public class ArrayUtils {

  public static byte[] append(byte[] src1, byte[] src2) {
    var arr = new byte[src1.length + src2.length];
    System.arraycopy(src1, 0, arr, 0, src1.length);
    System.arraycopy(src2, 0, arr, src1.length, src2.length);
    return arr;
  }

  public static byte[] append(byte[] src1, byte[] src2, byte[] src3) {
    var arr = new byte[src1.length + src2.length + src3.length];
    System.arraycopy(src1, 0, arr, 0, src1.length);
    System.arraycopy(src2, 0, arr, src1.length, src2.length);
    System.arraycopy(src3, 0, arr, src1.length + src2.length, src3.length);
    return arr;
  }

  public static void copy(byte[] src, byte[] dest1) {
    System.arraycopy(src, 0, dest1, 0, dest1.length);
  }

  public static void copy(byte[] src, byte[] dest1, byte[] dest2) {
    System.arraycopy(src, 0, dest1, 0, dest1.length);
    System.arraycopy(src, dest1.length, dest2, 0, dest2.length);
  }

  public static void copy(byte[] src, byte[] dest1, byte[] dest2, byte[] dest3) {
    System.arraycopy(src, 0, dest1, 0, dest1.length);
    System.arraycopy(src, dest1.length, dest2, 0, dest2.length);
    System.arraycopy(src, dest1.length + dest2.length, dest3, 0, dest3.length);
  }

  public static char[] append(char[] src1, char[] src2) {
    var arr = new char[src1.length + src2.length];
    System.arraycopy(src1, 0, arr, 0, src1.length);
    System.arraycopy(src2, 0, arr, src1.length, src2.length);
    return arr;
  }

  public static char[] append(char[] src1, char[] src2, char[] src3) {
    var arr = new char[src1.length + src2.length + src3.length];
    System.arraycopy(src1, 0, arr, 0, src1.length);
    System.arraycopy(src2, 0, arr, src1.length, src2.length);
    System.arraycopy(src3, 0, arr, src1.length + src2.length, src3.length);
    return arr;
  }

  public static String[] append(String[] src1, String[] src2) {
    var arr = new String[src1.length + src2.length];
    System.arraycopy(src1, 0, arr, 0, src1.length);
    System.arraycopy(src2, 0, arr, src1.length, src2.length);
    return arr;
  }

  public static String[] append(String[] src1, String[] src2, String[] src3) {
    var arr = new String[src1.length + src2.length + src3.length];
    System.arraycopy(src1, 0, arr, 0, src1.length);
    System.arraycopy(src2, 0, arr, src1.length, src2.length);
    System.arraycopy(src3, 0, arr, src1.length + src2.length, src3.length);
    return arr;
  }

  public static int[] copy(int[] src) {
    if ((src == null) || (src.length == 0)) {
      return src;
    }
    var arr = new int[src.length];
    System.arraycopy(src, 0, arr, 0, src.length);
    return arr;
  }

  public static String[] copy(String[] src) {
    if ((src == null) || (src.length == 0)) {
      return src;
    }
    var arr = new String[src.length];
    System.arraycopy(src, 0, arr, 0, src.length);
    return arr;
  }

  public static byte[] copy(byte[] src) {
    if ((src == null) || (src.length == 0)) {
      return src;
    }
    var arr = new byte[src.length];
    System.arraycopy(src, 0, arr, 0, src.length);
    return arr;
  }

  public static char[] copy(char[] src) {
    if ((src == null) || (src.length == 0)) {
      return src;
    }
    var arr = new char[src.length];
    System.arraycopy(src, 0, arr, 0, src.length);
    return arr;
  }

  public static <T> void shuffle(T[] src, Random random) {
    for (var i = src.length; i > 1; i--) {
      swap(src, i - 1, random.nextInt(i));
    }
  }

  public static void shuffle(int[] src, Random random) {
    for (var i = src.length; i > 1; i--) {
      swap(src, i - 1, random.nextInt(i));
    }
  }

  public static void shuffle(char[] src, Random random) {
    for (var i = src.length; i > 1; i--) {
      swap(src, i - 1, random.nextInt(i));
    }
  }

  public static void swap(int[] src, int i, int j) {
    var temp = src[i];
    src[i] = src[j];
    src[j] = temp;
  }

  public static void swap(char[] src, int i, int j) {
    var temp = src[i];
    src[i] = src[j];
    src[j] = temp;
  }

  public static <T> void swap(T[] src, int i, int j) {
    var temp = src[i];
    src[i] = src[j];
    src[j] = temp;
  }

  public static <T> boolean hasElements(T[] elements) {
    return (elements != null) && (elements.length > 0);
  }

  public static void fill(int[][] matrix, int value) {
    for (int[] element : matrix) {
      Arrays.fill(element, value);
    }
  }

  public static <T> T[][] toMatrix(T[] arr, int columns, BiFunction<Integer, Integer, T[][]> matrixCreator) {
    Arguments.notNull(arr);
    Arguments.isTrue(columns > 0);

    var len = arr.length;
    if (len == 0) {
      len = 1;
    }

    var rows = len / columns;
    if (rows * columns < len) {
      rows += 1;
    }
    var matrix = matrixCreator.apply(rows, columns);

    for (var i = 0; i < arr.length; i++) {
      var row = i / columns;
      var col = i % columns;

      matrix[row][col] = arr[i];
    }
    return matrix;
  }

  public static Object[] toArray(Object arr) {
    if (arr == null) {
      return null;
    }
    Arguments.isTrue(arr.getClass().isArray());

    Class<?> elementType = arr.getClass().getComponentType();
    if (!elementType.isPrimitive()) {
      return (Object[]) arr;
    }

    var len = Array.getLength(arr);
    var ca = Array.newInstance(TypeUtils.wrap(elementType), len);

    for (var i = 0; i < len; i++) {
      Array.set(ca, i, Array.get(arr, i));
    }
    return (Object[]) ca;
  }

  @SuppressWarnings("unchecked")
  public static <T> T[] toArray(Enumeration<T> enumer, Class<T> elementType) {
    if (enumer == null) {
      return null;
    }
    List<T> values = new ArrayList<>();
    while (enumer.hasMoreElements()) {
      values.add(enumer.nextElement());
    }
    return values.toArray((T[]) Array.newInstance(elementType, values.size()));
  }

  public static List<Object> toList(Object arr) {
    if (arr == null) {
      return null;
    }
    Arguments.isTrue(arr.getClass().isArray());

    var len = Array.getLength(arr);
    List<Object> list = new ArrayList<>(len);

    for (var i = 0; i < len; i++) {
      list.add(Array.get(arr, i));
    }
    return list;
  }

  public static boolean endsWith(byte[] arr, byte[] suffix, int fromIndex) {
    if (arr == suffix) {
      return fromIndex == 0;
    }
    if (arr == null || suffix == null || (suffix.length + fromIndex != arr.length)) {
      return false;
    }
    for (var i = 0; i < suffix.length; i++) {
      if (suffix[i] != arr[i + fromIndex]) {
        return false;
      }
    }
    return true;
  }

  public static int min(int[] arr) {
    Arguments.notNull(arr);
    if (arr.length == 0) {
      throw new NoSuchElementException("arr is empty.");
    }
    var m = arr[0];
    for (var i = 1; i < arr.length; i++) {
      m = Math.min(m, arr[i]);
    }
    return m;
  }

  public static int max(int[] arr) {
    Arguments.notNull(arr);
    if (arr.length == 0) {
      throw new NoSuchElementException("arr is empty.");
    }
    var m = arr[0];
    for (var i = 1; i < arr.length; i++) {
      m = Math.max(m, arr[i]);
    }
    return m;
  }

  public static <T> T min(T[] arr, Comparator<T> comparator) {
    Arguments.notNull(arr);
    if (arr.length == 0) {
      throw new NoSuchElementException("arr is empty.");
    }
    var m = arr[0];
    for (var i = 1; i < arr.length; i++) {
      m = comparator.compare(m, arr[i]) <= 0 ? m : arr[i];
    }
    return m;
  }

  public static <T> T max(T[] arr, Comparator<T> comparator) {
    Arguments.notNull(arr);
    if (arr.length == 0) {
      throw new NoSuchElementException("arr is empty.");
    }
    var m = arr[0];
    for (var i = 1; i < arr.length; i++) {
      m = comparator.compare(m, arr[i]) <= 0 ? arr[i] : m;
    }
    return m;
  }

  public static class ArrayIteratorObj implements Iterator<Object> {
    final Object array;
    final int len;
    int i = 0;

    public ArrayIteratorObj(Object array) {
      Arguments.notNull(array);
      Arguments.isTrue(array.getClass().isArray());

      this.array = array;
      this.len = Array.getLength(array);
    }

    @Override
    public boolean hasNext() {
      return this.i < this.len;
    }

    @Override
    public Object next() {
      return Array.get(this.array, this.i++);
    }
  }

  public static class ArrayIterableObj implements Iterable<Object> {
    final Object array;

    public ArrayIterableObj(Object array) {
      Arguments.notNull(array);
      Arguments.isTrue(array.getClass().isArray());

      this.array = array;
    }

    @Override
    public Iterator<Object> iterator() {
      return new ArrayIteratorObj(this.array);
    }
  }

  public static class ArrayIterator<T> implements Iterator<T> {
    final T[] array;
    int i = 0;

    public ArrayIterator(T[] array) {
      this.array = Arguments.notNull(array);
    }

    @Override
    public boolean hasNext() {
      return this.i < this.array.length;
    }

    @Override
    public T next() {
      return this.array[i++];
    }
  }

  public static class ArrayIterable<T> implements Iterable<T> {
    private T[] array;

    public ArrayIterable(T[] array) {
      this.array = Arguments.notNull(array);
    }

    @Override
    public Iterator<T> iterator() {
      return new ArrayIterator<>(this.array);
    }
  }
}
