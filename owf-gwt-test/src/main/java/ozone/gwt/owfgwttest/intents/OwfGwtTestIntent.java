package ozone.gwt.owfgwttest.intents;

import ozone.gwt.widget.Intent;
import ozone.gwt.widget.WidgetHandle;

import com.google.gwt.core.client.JavaScriptObject;

public abstract class OwfGwtTestIntent<T extends JavaScriptObject> extends Intent<T> {

  protected static String PREFIX = "application/vnd.ozone.gwt.owfgwttest.";

  public OwfGwtTestIntent(WidgetHandle widgetHandle) {
    super(widgetHandle);
  }
}