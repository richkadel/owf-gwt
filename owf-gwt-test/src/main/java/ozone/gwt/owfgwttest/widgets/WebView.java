package ozone.gwt.owfgwttest.widgets;

import ozone.gwt.widget.WidgetContainer;
import ozone.gwt.widget.WidgetFramework;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;

public class WebView extends Frame implements IsWidget, EntryPoint {

  private Object widgetHandle;

  public WebView() {
    this(null);
  }
  
  public WebView(WidgetContainer widgetContainer) {
    super("http://www.gwtproject.org/");
    widgetHandle = WidgetFramework.createWidgetHandle(widgetContainer, this);
    // do not use until Widget.onLoad() or IsWidget.asWidget() are called
  }

  @Override
  public void onLoad() {
    setSize("100%", "100%");
  }
  
  @Override
  public void onModuleLoad() {
    RootPanel.get().add(this);
  }
}
