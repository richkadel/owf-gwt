package ozone.gwt.widget;

import jsfunction.gwt.JsFunctionUtils;

import com.google.gwt.core.client.JsArrayMixed;

public class DirectLogger implements WidgetLogger {

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