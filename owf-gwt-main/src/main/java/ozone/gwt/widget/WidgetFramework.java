package ozone.gwt.widget;

import ozone.gwt.widget.direct.DirectWidgetFramework;
import ozone.gwt.widget.owf.OZONEWidgetFramework;

import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;

public abstract class WidgetFramework {
  
  protected static final String OWF_GWT_INTENTS_MAP_GLOBALVAR = "__OwfGwtIntentsMap";
  
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
    
    // One-stop shopping to disable context menu for all widgets.
    // I noticed that if a widget with custom context menus was on top of another widget that did not
    // disable context menus, the context menu would still show up.  Prime example is the OWF
    // "Tab Layout". However, this call below also seems to prevent me adding a custom contextmenu
    // EventListener via JavaScript, so I changed my EventListener to show the menu when I get a
    // mouse right-click.  
    
    RootPanel.get().addDomHandler(new ContextMenuHandler() {

      @Override public void onContextMenu(ContextMenuEvent event) {
        event.preventDefault();
        event.stopPropagation();
      }
    }, ContextMenuEvent.getType());
    
    return getInstance().createWidgetHandleInstance(widgetContainer, gwtIsWidget);
  }

  public static WidgetHandle getWidgetHandle(IsWidget gwtIsWidget) {
    return getInstance().getWidgetHandleInstance(gwtIsWidget);
  }

  public abstract WidgetHandle createWidgetHandleInstance(WidgetContainer widgetContainer, IsWidget gwtIsWidget);
  
  public abstract WidgetHandle getWidgetHandleInstance(IsWidget gwtIsWidget);
  
  public abstract WidgetLogger getLogger();
}
