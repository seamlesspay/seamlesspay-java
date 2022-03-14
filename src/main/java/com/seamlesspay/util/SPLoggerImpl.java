package com.seamlesspay.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class SPLoggerImpl implements SPLogger {

  private final Logger log;

  static {
    loadLogConfig();
  }

  public static void loadLogConfig() {
    Optional<URI> uriOptional = getConfigUri();
    if (!uriOptional.isPresent()) {
      return;
    }
    URI uri = uriOptional.get();

    boolean isFileInJar = uri.toString().contains("!");
    if (!isFileInJar) {
      Path path = Paths.get(uri);
      loadConfig(path);
      return;
    }

    loadConfigFromJar(uri);
  }

  private static void loadConfigFromJar(URI uri) {
    // if app is in the jar then using this solution - https://stackoverflow.com/a/22605905/838444
    final String[] array = uri.toString().split("!");
    final Map<String, String> env = new HashMap<>();
    try (FileSystem fs = FileSystems.newFileSystem(URI.create(array[0]), env)) {
      Path path = fs.getPath(array[1]);
      loadConfig(path);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static Optional<URI> getConfigUri() {
    URL resource = SPLoggerImpl.class.getClassLoader().getResource("logging.properties");
    if (resource == null) {
      System.out.println("failed to load logging configuration");
      return Optional.empty();
    }

    try {
      return Optional.of(resource.toURI());
    } catch (URISyntaxException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  public SPLoggerImpl() {
    this(Logger.getLogger(""));
  }

  public SPLoggerImpl(Logger julLogger) {
    log = julLogger;
    setParentLevel();
  }

  /**
   * By default a new logger gets INFO log level.
   */
  private void setParentLevel() {
    if (log.getParent() == null) {
      return;
    }

    Level parentLevel = log.getParent().getLevel();
    if (log.getLevel() != parentLevel) {
      log.setLevel(parentLevel);
    }
  }

  public static void loadConfig(Path configPath) {
    try {
      InputStream inputStream = Files.newInputStream(configPath);
      LogManager.getLogManager().readConfiguration(inputStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void trace(String format, Object param) {
    log(Level.FINER, format, param);
  }

  @Override
  public void trace(String format, Object param, Object param2) {
    log(Level.FINER, format, param, param2);
  }

  @Override
  public void debug(String format, Object param) {
    log(Level.FINE, format, param);
  }

  @Override
  public void debug(String message, Throwable t) {
    log(Level.FINE, message, t);
  }

  @Override
  public void debug(String format, Object param, Object param2) {
    log(Level.FINE, format, param, param2);
  }

  @Override
  public void debug(String format, Object param, Throwable t) {
    log(Level.INFO, format, param, t);
  }

  @Override
  public void info(String format, Object param) {
    log(Level.INFO, format, param);
  }

  @Override
  public void info(String message, Throwable t) {
    log(Level.INFO, message, t);
  }

  @Override
  public void info(String format, Object param1, Object param2) {
    log(Level.INFO, format, param1, param2);
  }

  @Override
  public void error(String format, Object param) {
    log(Level.SEVERE, format, param);
  }

  private void log(Level level, String format, Object param) {
    String message = String.format(format, param);
    log.log(level, message);
  }

  private void log(Level level, String format, Object param, Object param2) {
    String message = String.format(format, param, param2);
    log.log(level, message);
  }

  private void log(Level level, String message, Throwable t) {
    log.log(level, message, t);
  }
}
