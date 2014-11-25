package ozone.gwt.widget;

import jsfunction.gwt.functions.EventListener;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;

public abstract class JavaScriptObjectChannel<T extends JavaScriptObject> extends EventingChannel {
  
  protected JavaScriptObjectChannel(WidgetHandle widgetHandle) {
    super(widgetHandle);
  }

  protected abstract void messageReceived(WidgetProxy sender, T message);
  
  @SuppressWarnings("unchecked")
  public <S extends JavaScriptObjectChannel<T>> S subscribe() {
    getWidgetHandle().subscribe(getChannelName(), new EventListener<StringMessage>() {
      public void callback(StringMessage event) {
        messageReceived(event.getSender(), JsonUtils.safeEval(event.getMessage()).<T>cast());
      }
    });
    return (S) this;
  }
}
