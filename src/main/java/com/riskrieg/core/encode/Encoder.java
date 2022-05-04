package com.riskrieg.core.encode;

import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;

public sealed interface Encoder<T> permits RkmEncoder {

  void encode(T object, OutputStream outputStream, boolean shouldCloseStream) throws IOException, NoSuchAlgorithmException;

  default void encode(T object, OutputStream outputStream) throws IOException, NoSuchAlgorithmException {
    encode(object, outputStream, true);
  }

}
