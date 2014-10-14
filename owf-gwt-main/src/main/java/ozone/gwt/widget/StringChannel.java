package ozone.gwt.widget;

import jsfunction.EventListener;

public abstract class StringChannel extends EventingChannel {
  
  protected StringChannel(WidgetHandle widgetHandle) {
    super(widgetHandle);
  }

  protected abstract void messageReceived(WidgetProxy sender, String message);
    
  public void subscribe() {
    getWidgetHandle().subscribe(getChannelName(), new EventListener<StringMessage>() {
      public void callback(StringMessage event) {
        messageReceived(event.getSender(), event.getMessage());
      }
    });
  }
}
