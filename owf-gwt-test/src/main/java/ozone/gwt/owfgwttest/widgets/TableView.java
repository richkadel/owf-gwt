package ozone.gwt.owfgwttest.widgets;

import ozone.gwt.widget.WidgetContainer;
import ozone.gwt.widget.WidgetFramework;
import ozone.gwt.widget.WidgetHandle;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

public class TableView extends ScrollPanel implements IsWidget, EntryPoint {

  private WidgetHandle widgetHandle;

  public TableView() {
    this(null);
  }
  
  public TableView(WidgetContainer widgetContainer) {
    widgetHandle = WidgetFramework.createWidgetHandle(widgetContainer, this);
    // do not use until Widget.onLoad() or IsWidget.asWidget() are called
  }
  
  @Override
  public void onLoad() {
    
    Grid grid = new Grid(50, 5);
    // Put some values in the grid cells.
    for (int row = 0; row < 50; ++row) {
      for (int col = 0; col < 5; ++col)
        grid.setText(row, col, "" + row + ", " + col);
    }

    // Just for good measure, let's put a button in the center.
    //grid.setWidget(2, 2, new Button("Does nothing, but could"));

    setWidget(grid);

    setSize("100%", "100%");
  }

  @Override
  public void onModuleLoad() {
    RootPanel.get().add(this);
  }
}
