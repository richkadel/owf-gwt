package ozone.gwt.owfgwttest.widgets;

import java.util.Arrays;
import java.util.List;

import ozone.gwt.widget.WidgetContainer;
import ozone.gwt.widget.WidgetFramework;
import ozone.gwt.widget.WidgetHandle;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class TableForm extends ScrollPanel implements EntryPoint {
  
  /**
   * The list of data to display.
   */
  private static final List<String> DAYS = Arrays.asList("Sunday", "Monday",
      "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

  private WidgetHandle widgetHandle;

  public TableForm() {
    this(null);
  }
  
  public TableForm(WidgetContainer widgetContainer) {
    widgetHandle = WidgetFramework.createWidgetHandle(widgetContainer, this);
  }
  
  @Override
  public void onLoad() {

 // Create a cell to render each value.
    TextCell textCell = new TextCell();

    // Create a CellList that uses the cell.
    CellList<String> cellList = new CellList<String>(textCell);
    cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

    // Add a selection model to handle user selection.
    final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();
    cellList.setSelectionModel(selectionModel);
    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      public void onSelectionChange(SelectionChangeEvent event) {
        String selected = selectionModel.getSelectedObject();
        if (selected != null) {
          Window.alert("You selected: " + selected);
        }
      }
    });

    // Set the total row count. This isn't strictly necessary, but it affects
    // paging calculations, so its good habit to keep the row count up to date.
    cellList.setRowCount(DAYS.size(), true);

    // Push the data into the widget.
    cellList.setRowData(0, DAYS);

    setWidget(cellList);
    
    setSize("100%", "100%");
  }

  @Override
  public void onModuleLoad() {
    RootPanel.get().add(this);
  }
}
