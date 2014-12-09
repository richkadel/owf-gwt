package ozone.gwt.owfgwttest.widgets;

import ozone.gwt.owfgwttest.channels.DocumentTreeInitializationChannel;
import ozone.gwt.owfgwttest.channels.data.DocumentTreeInitializationData;
import ozone.gwt.owfgwttest.intents.ShowTable;
import ozone.gwt.owfgwttest.intents.data.TableData;
import ozone.gwt.owfgwttest.proxies.DocumentTreeProxy;
import ozone.gwt.widget.WidgetContainer;
import ozone.gwt.widget.WidgetFramework;
import ozone.gwt.widget.WidgetHandle;
import ozone.gwt.widget.WidgetProxy;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

public class TableView extends ScrollPanel implements IsWidget, EntryPoint {

  private WidgetHandle widgetHandle;
  
  DocumentTreeProxy documentTreeProxy;

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
    Button button = new Button("Imbedded Button");
    grid.setWidget(2, 2, button);
    
    button.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent arg0) {
//TODO
        if (documentTreeProxy != null) {
          documentTreeProxy.setBrightness(2.0);
        }
      }
    });

    setWidget(grid);

    setSize("100%", "100%");
    
    new ShowTable(widgetHandle) {
      protected void intentReceived(WidgetProxy sender, TableData data) {
        documentTreeProxy = new DocumentTreeProxy(sender);
//TODO something with data
      }
    };
    
//TODO
    DocumentTreeInitializationChannel.publish(widgetHandle, 
        DocumentTreeInitializationData.create(
            "something", "This is the TableView"));
  }

  @Override
  public void onModuleLoad() {
    RootPanel.get().add(this);
  }
}
