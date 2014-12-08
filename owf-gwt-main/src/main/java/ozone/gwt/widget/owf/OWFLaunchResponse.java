package ozone.gwt.widget.owf;

import com.google.gwt.core.client.JavaScriptObject;

public final class OWFLaunchResponse extends JavaScriptObject {

  protected OWFLaunchResponse() {}

  // EXAMPLE:
//error: false,
//newWidgetLaunched: false, 
//message: "An instance of the specified widget already exists.", 
//uniqueId: "6aa6a53c-5d78-e08a-7459-0354af458d12"
  
  public native boolean isError() /*-{
    return this.error;
  }-*/;
  
  public native boolean getNewWidgetLaunched() /*-{
    return this.newWidgetLaunched;
  }-*/;
  
  public native String getMessage() /*-{
    return this.message;
  }-*/;
  
  public native String getUniqueWidgetId() /*-{
    return this.uniqueId;
  }-*/;
  
}
