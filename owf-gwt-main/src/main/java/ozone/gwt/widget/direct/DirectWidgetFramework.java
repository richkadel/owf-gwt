package ozone.gwt.widget.direct;

import java.util.Map;
import java.util.HashMap;

import ozone.gwt.widget.WidgetContainer;
import ozone.gwt.widget.WidgetFramework;
import ozone.gwt.widget.WidgetHandle;

import com.google.gwt.user.client.ui.IsWidget;

public class DirectWidgetFramework extends WidgetFramework {
  
  private static final DirectWidgetFramework widgetFramework = new DirectWidgetFramework();

  private static Map<Integer,WidgetHandle> gwtIsWidgetToWidgetHandle = new HashMap<Integer,WidgetHandle>();
  
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
}
