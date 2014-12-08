package ozone.gwt.widget;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

public final class IntentDescriptor extends JavaScriptObject {
    
  protected IntentDescriptor() {}
    
  public static native IntentDescriptor create(String action, String dataType) /*-{
    return {
      action:action,
      dataTypes:[dataType]
    }
  }-*/;
  
  public native String getAction() /*-{
    return this.action;
  }-*/;
  
  public native JsArrayString getDataTypes() /*-{
    return this.dataTypes;
  }-*/;
}
