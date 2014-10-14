package ozone.gwt.widget.owf;

import com.google.gwt.core.client.JavaScriptObject;

public final class OWFWidgetState extends JavaScriptObject {
  
  protected OWFWidgetState() {}
  
  public native void activateWidget() /*-{
    this.activateWidget($wnd.OWF.getWidgetGuid()); // not providing callback...not planning to use one... Is this OK?
  }-*/;
}
