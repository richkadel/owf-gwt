package ozone.gwt.widget;

import ozone.gwt.widget.direct.DirectWidgetFramework;
import ozone.gwt.widget.owf.OZONEWidgetFramework;

import com.google.gwt.user.client.ui.IsWidget;

public abstract class WidgetFramework {
  
  private static WidgetFramework widgetFramework;
  
  public static WidgetFramework getInstance() {
    if (widgetFramework == null) {
      if (OZONEWidgetFramework.isRunningInOWF()) {
        widgetFramework = new OZONEWidgetFramework(){}; // calling protected constructors, to discourage direct construction
      } else {
        widgetFramework = new DirectWidgetFramework(){};
      }
    }
    return widgetFramework;
  }
  
  public static WidgetHandle createWidgetHandle(WidgetContainer widgetContainer, IsWidget gwtIsWidget) {
    return getInstance().createWidgetHandleInstance(widgetContainer, gwtIsWidget);
  }

  public static WidgetHandle getWidgetHandle(IsWidget gwtIsWidget) {
    return getInstance().getWidgetHandleInstance(gwtIsWidget);
  }

  public abstract WidgetHandle createWidgetHandleInstance(WidgetContainer widgetContainer, IsWidget gwtIsWidget);
  
  public abstract WidgetHandle getWidgetHandleInstance(IsWidget gwtIsWidget);
}
