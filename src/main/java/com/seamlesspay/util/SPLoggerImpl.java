package com.seamlesspay.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class SPLoggerImpl implements SPLogger {

  private final Logger log;

  static {

    URL resource = SPLoggerImpl.class.getClassLoader().getResource("logging.properties");
    try {
      loadConfig(Paths.get(resource.toURI()));
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  public SPLoggerImpl(Logger log) {
    this.log = log;
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
