package ozone.gwt.widget.owf;

import com.google.gwt.core.client.JavaScriptObject;

public final class IntentsMap extends JavaScriptObject {

  protected IntentsMap() {}
  
  /**
   * @param stringifiedIntent - typically SaveableText.stringify(some Intent<?>.getIntentDescriptor)
   * @return the universal name of the one widget receiving this intent, or null
   */
  public native String getWidgetUniversalName(String stringifiedIntent) /*-{
    var universalName = this[stringifiedIntent];
    if (universalName === "") {
      universalName = null;
    }
    return universalName;
  }-*/;
}
