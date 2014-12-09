package ozone.gwt.owfgwttest.directcontainers;

import ozone.gwt.widget.WidgetContainer;
import ozone.gwt.widget.WidgetHandle;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TabLayoutPanel;

public abstract class TabbedWidgetContainer extends TabLayoutPanel implements WidgetContainer {

  public TabbedWidgetContainer() {
    super(1.5, Unit.EM);
  }

  @Override
  public void activate(WidgetHandle widgetHandle) {
    selectTab(widgetHandle.getGWTIsWidget());
  }

  protected <T extends IsWidget> T addWidget(T isWidget, String text) {
    add(isWidget, text);
    return isWidget;
  }
}