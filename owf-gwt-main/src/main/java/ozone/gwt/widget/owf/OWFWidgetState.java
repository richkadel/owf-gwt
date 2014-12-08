package ozone.gwt.widget.owf;

import com.google.gwt.core.client.JavaScriptObject;

public final class OWFWidgetState extends JavaScriptObject {
  
  protected OWFWidgetState() {}
  
  public native void activateWidget() /*-{
    this.activateWidget(this.widgetGuid); // not providing callback...not planning to use one in current OWF GWT implementation
  }-*/;

  //THIS DOESN'T PROVIDE THE CURRENT WIDGET'S UNIQUE GUID. MAYBE IT'S THE SAVED GUID? USE $wnd.OWF.getWidgetGuid()
  public native String getWidgetGuid() /*-{
    return this.widgetGuid;
  }-*/;
}
