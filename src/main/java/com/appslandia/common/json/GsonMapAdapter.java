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

package com.appslandia.common.json;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Function;

import com.appslandia.common.utils.ObjectUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 *
 * @author Loc Ha
 *
 */
public class GsonMapAdapter<T extends Map<String, Object>> implements JsonDeserializer<T> {

  final JsonObjectParser jsonObjectParser = new JsonObjectParser()
      .setJsonValueConverter(GsonJsonValueConverter.INSTANCE);

  public GsonMapAdapter(Function<Map<String, Object>, T> rootConverter) {
    this.jsonObjectParser.setRootConverter(ObjectUtils.cast(rootConverter));
  }

  public <F, V> GsonMapAdapter<T> setValueConverter(String[] pathOrPatterns, Function<F, V> converter) {
    this.jsonObjectParser.setValueConverter(pathOrPatterns, converter);
    return this;
  }

  public <F, V> GsonMapAdapter<T> setMapConverter(Function<Map<String, Object>, Object> mapConverter) {
    this.jsonObjectParser.setMapConverter(mapConverter);
    return this;
  }

  @Override
  public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    var value = this.jsonObjectParser.parse(json);
    return ObjectUtils.cast(value);
  }
}
