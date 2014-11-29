package ozone.gwt.widget;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public final class WidgetDescriptor extends JavaScriptObject {

  protected WidgetDescriptor() {}

  public static WidgetDescriptor create(WidgetHandle widgetHandle) {
    return create(widgetHandle, null);
  }
  
  public static WidgetDescriptor create(WidgetHandle widgetHandle, String displayGroupName) {
    IsWidget isWidget = widgetHandle.getGWTIsWidget();
    
    String widgetName = isWidget.getClass().getSimpleName();
    
    StringBuffer universalName = new StringBuffer();
    
    String[] components = isWidget.getClass().getName().split("\\.");
    for (int i = components.length-1; i >= 0; i--) {
      universalName.append(components[i]);
      if (i > 0) {
        universalName.append('.');
      }
    }
    
    StringBuffer  displayName = new StringBuffer();
    
    int len = widgetName.length();
    for (int i = 1; i < len; i++) {
      char prev = widgetName.charAt(i-1);
      char ch = widgetName.charAt(i);
      displayName.append(prev);
      if (i < len) {
        char next = widgetName.charAt(i+1);
        if (   ( Character.isUpperCase(ch) && !Character.isUpperCase(next))
            || (!Character.isLetter(ch)   &&   Character.isLetter(next)) ) {
          displayName.append(' ');
        }
      }
    }
    displayName.append(widgetName.charAt(widgetName.length()-1));
    
    if (displayGroupName != null) {
      displayName.append(" - ");
      displayName.append(displayGroupName);
    }
    
    Widget gwtWidget = isWidget.asWidget();
    
    int height = 0;
    int width = 0;
    
    while ((height == 0 || width == 0) && gwtWidget != null) {
      height = gwtWidget.getOffsetHeight();
      width = gwtWidget.getOffsetWidth();
      gwtWidget = gwtWidget.getParent();
    }
    
    // OWF typically adds some chrome and title/security bars, so we reduce the size a little to accommodate.
    // Also, OWF's Widget Editor requires widget height/width to be at least 200 pixels.
    height = (int) Math.max(200, (height*0.90));
    width = (int) Math.max(200, (width*0.90));
    
    JsArray<IntentDescriptor> intentDescriptors = JsArray.createArray().cast();
    for (Intent<?> intent : widgetHandle.getIntentsReceived()) {
      intentDescriptors.push(IntentDescriptor.create(intent.getAction(), intent.getDataType()));
    }
    
    return nativeCreate(
      widgetName,
      universalName.toString(),
      displayName.toString(),
      height,
      width,
      intentDescriptors
    );
  }
  
  private static native WidgetDescriptor nativeCreate(
        String widgetName,
        String universalName,
        String displayName,
        int height,
        int width,
        JsArray<IntentDescriptor> intentDescriptors
      ) /*-{
    return {
      _widgetName: widgetName, // not part of standard OWF widget descriptor
      widgetUrl: "%appUrlPrefix%owfGwtWidget.html#"+widgetName+"%appParams%",
      imageUrlLarge: "%appUrlPrefix%owfWidgets/icons/"+widgetName+"128.png",
      imageUrlSmall: "%appUrlPrefix%owfWidgets/icons/"+widgetName+"128.png",
      universalName: universalName,
      displayName: displayName,
      height: height,
      width: width,
      visible: true,
      singleton: false,
      background: false,
      widgetTypes: ["standard"],
      intents: {
        receive: intentDescriptors
      }
    };
  }-*/;
  
  private static final class IntentDescriptor extends JavaScriptObject {
    
    protected IntentDescriptor() {}
    
    public static native IntentDescriptor create(String action, String dataType) /*-{
      return {
        action:action,
        dataTypes:[dataType]
      }
    }-*/;
  }

  public native String getWidgetName() /*-{
    return this._widgetName;
  }-*/;
}
