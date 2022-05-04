package com.riskrieg.core.recoder;

import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;

public interface Encoder<T> {

  void encode(T object, OutputStream outputStream, boolean shouldCloseStream) throws IOException, NoSuchAlgorithmException;

  default void encode(T object, OutputStream outputStream) throws IOException, NoSuchAlgorithmException {
    encode(object, outputStream, true);
  }

}
