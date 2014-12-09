package ozone.gwt.owfgwttest.directcontainers;

import ozone.gwt.owfgwttest.widgets.TableForm;
import ozone.gwt.owfgwttest.widgets.WebForm;

public class FormTabs extends TabbedWidgetContainer {

  @Override
  public void onLoad() {
    addWidget(new TableForm(this), "Table Form");
    addWidget(new WebForm(this), "Web Form");
  }
}
