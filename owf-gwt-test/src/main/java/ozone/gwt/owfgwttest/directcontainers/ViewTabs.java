package ozone.gwt.owfgwttest.directcontainers;

import ozone.gwt.owfgwttest.widgets.TableView;
import ozone.gwt.owfgwttest.widgets.WebView;

public class ViewTabs extends TabbedWidgetContainer {

  @Override
  public void onLoad() {
    addWidget(new TableView(this), "Table View");
    addWidget(new WebView(this), "Web View");
  }
}
