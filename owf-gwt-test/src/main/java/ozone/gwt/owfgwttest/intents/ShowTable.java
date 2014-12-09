package ozone.gwt.owfgwttest.intents;

import ozone.gwt.owfgwttest.intents.data.TableData;
import ozone.gwt.widget.OnReceipt;
import ozone.gwt.widget.WidgetHandle;
import ozone.gwt.widget.WidgetProxy;

public abstract class ShowTable extends OwfGwtTestIntent<TableData> {
  
  public ShowTable(WidgetHandle widgetHandle) {
    super(widgetHandle);
  }

  @Override
  public String getDataType() {
    return OwfGwtTestIntent.PREFIX+"metatable";
  }
  
  public static void startActivity(WidgetHandle widgetHandle, TableData data) {
    startActivity(widgetHandle, data, null);
  }
  
  public static void startActivity(WidgetHandle widgetHandle, TableData data, OnReceipt onReceipt) {
    widgetHandle.startActivity(
      new ShowTable(widgetHandle){
        public void intentReceived(WidgetProxy sender, TableData data) {}},
      data, onReceipt);
  }
}
