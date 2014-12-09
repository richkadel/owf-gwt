package ozone.gwt.owfgwttest.channels;

import ozone.gwt.owfgwttest.channels.data.DocumentTreeInitializationData;
import ozone.gwt.widget.JavaScriptObjectChannel;
import ozone.gwt.widget.WidgetHandle;

public abstract class DocumentTreeInitializationChannel extends JavaScriptObjectChannel<DocumentTreeInitializationData> {

  public DocumentTreeInitializationChannel(WidgetHandle widgetHandle) {
    super(widgetHandle);
  }
  
  public static void publish(WidgetHandle widgetHandle, DocumentTreeInitializationData data) {
    widgetHandle.publish(DocumentTreeInitializationChannel.class.getName(), data, null);
  }
}
