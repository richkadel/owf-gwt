package ozone.gwt.widget;

public interface WidgetLogger {

  void trace(Object... messages);
  void debug(Object... messages);
  void info(Object... messages);
  void warn(Object... messages);
  void error(Object... messages);
  void fatal(Object... messages);
}
