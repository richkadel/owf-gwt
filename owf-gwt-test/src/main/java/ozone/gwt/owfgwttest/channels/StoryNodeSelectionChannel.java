package ozone.gwt.owfgwttest.channels;

import ozone.gwt.owfgwttest.channels.data.TableItem;
import ozone.gwt.widget.JavaScriptObjectChannel;
import ozone.gwt.widget.WidgetHandle;

import com.google.gwt.core.client.JsArray;

public abstract class StoryNodeSelectionChannel extends JavaScriptObjectChannel<JsArray<TableItem>> {

  public StoryNodeSelectionChannel(WidgetHandle widgetHandle) {
    super(widgetHandle);
  }
  
  public static void publish(WidgetHandle widgetHandle, JsArray<TableItem> selected) {
    widgetHandle.publish(StoryNodeSelectionChannel.class.getName(), selected, null);
  }
}
