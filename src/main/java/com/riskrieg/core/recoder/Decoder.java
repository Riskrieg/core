package com.riskrieg.core.recoder;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

public interface Decoder<T> {

  T decode(Path path) throws IOException, NoSuchAlgorithmException;

  T decode(URL url) throws IOException, NoSuchAlgorithmException;

  T decode(byte[] data) throws IOException, NoSuchAlgorithmException;

}
