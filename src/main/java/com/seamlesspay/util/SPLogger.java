package com.seamlesspay.util;

import java.util.logging.Logger;

public interface SPLogger {

  static SPLogger get() {
    Logger julLogger = Logger.getLogger("default");
    return new SPLoggerImpl(julLogger);
  }

  static SPLogger get(Class<?> clazz) {
    Logger julLogger = Logger.getLogger(clazz.getName());
    return new SPLoggerImpl(julLogger);
  }

  void trace(String format, Object param);

  void trace(String format, Object param, Object param2);

  void debug(String format, Object param);

  void debug(String message, Throwable t);

  void debug(String format, Object param, Object param2);
  void debug(String format, Object param, Throwable t);

  void info(String format, Object param);

  void info(String message, Throwable t);

  void info(String format, Object param1, Object param2);

  void error(String format, Object param);
}
