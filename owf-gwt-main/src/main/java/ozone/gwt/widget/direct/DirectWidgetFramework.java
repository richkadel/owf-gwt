package ozone.gwt.widget.direct;

import java.util.Map;
import java.util.HashMap;

import jsfunction.gwt.JsFunctionUtils;
import ozone.gwt.widget.WidgetContainer;
import ozone.gwt.widget.WidgetFramework;
import ozone.gwt.widget.WidgetHandle;
import ozone.gwt.widget.WidgetLogger;

import com.google.gwt.core.client.JsArrayMixed;
import com.google.gwt.user.client.ui.IsWidget;

public class DirectWidgetFramework extends WidgetFramework {
  
  private static final DirectWidgetFramework widgetFramework = new DirectWidgetFramework();

  private static Map<Integer,WidgetHandle> gwtIsWidgetToWidgetHandle = new HashMap<Integer,WidgetHandle>();

  private WidgetLogger directLogger = new DirectLogger();
  
  protected DirectWidgetFramework() {
  }

  public static DirectWidgetFramework getInstance() {
    return widgetFramework;
  }
  
  @Override
  public WidgetHandle createWidgetHandleInstance(WidgetContainer widgetContainer, IsWidget gwtIsWidget) {
    if (gwtIsWidgetToWidgetHandle.get(gwtIsWidget.hashCode()) != null) {
      throw new Error("Attempt to create a WidgetHandle for a GWT IsWidget that already has one!");
    }
    WidgetHandle widgetHandle = new DirectWidgetHandle(widgetContainer, gwtIsWidget);
    gwtIsWidgetToWidgetHandle.put(gwtIsWidget.hashCode(), widgetHandle);
    return widgetHandle;
  }

  public WidgetHandle getWidgetHandleInstance(IsWidget gwtIsWidget) {
    return gwtIsWidgetToWidgetHandle.get(gwtIsWidget.hashCode());
  }
  
  public static void removeWidget(IsWidget gwtIsWidget) {
    gwtIsWidgetToWidgetHandle.remove(gwtIsWidget.hashCode());
  }

  @Override
  public WidgetLogger getLogger() {
    return directLogger;
  }
  
  private static class DirectLogger implements WidgetLogger {

    @Override
    public void trace(Object... messages) {
      trace(JsFunctionUtils.varArgsToMixedArray(messages));
    }

    public native void trace(JsArrayMixed messages) /*-{
      if (console.trace) {
        console.trace.apply(console, messages);
      } else {
        console.log.apply(console, messages);
      }
    }-*/;

    @Override
    public void debug(Object... messages) {
      debug(JsFunctionUtils.varArgsToMixedArray(messages));
    }

    public native void debug(JsArrayMixed messages) /*-{ // according to Mozilla standards documentation, this is an alias for "log"
      if (console.log) {
        console.log.apply(console, messages);
      } else {
        console.log.apply(console, messages);
      }
    }-*/;

    @Override
    public void info(Object... messages) {
      info(JsFunctionUtils.varArgsToMixedArray(messages));
    }

    public native void info(JsArrayMixed messages) /*-{
      if (console.info) {
        console.info.apply(console, messages);
      } else {
        console.log.apply(console, messages);
      }
    }-*/;

    @Override
    public void warn(Object... messages) {
      warn(JsFunctionUtils.varArgsToMixedArray(messages));
    }

    public native void warn(JsArrayMixed messages) /*-{
      if (console.warn) {
        console.warn.apply(console, messages);
      } else {
        console.log.apply(console, messages);
      }
    }-*/;

    @Override
    public void error(Object... messages) {
      error(JsFunctionUtils.varArgsToMixedArray(messages));
    }

    public native void error(JsArrayMixed messages) /*-{
      if (console.error) {
        console.error.apply(console, messages);
      } else {
        console.log.apply(console, messages);
      }
    }-*/;

    @Override
    public void fatal(Object... messages) {
      fatal(JsFunctionUtils.varArgsToMixedArray(messages));
    }
    
    public native void fatal(JsArrayMixed messages) /*-{ // according to Mozilla standards documentation, there is no "fatal()"
      if (console.error) {
        console.error.apply(console, messages);
      } else {
        console.log.apply(console, messages);
      }
    }-*/;
  }
}
