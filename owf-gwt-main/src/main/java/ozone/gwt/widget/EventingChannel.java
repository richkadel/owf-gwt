package ozone.gwt.widget;

public abstract class EventingChannel {
  
  private WidgetHandle widgetHandle;

  private String channelName;
  
  protected EventingChannel(WidgetHandle widgetHandle) {
    this.widgetHandle = widgetHandle;
    channelName = getClass().getName();
  }
  
  protected WidgetHandle getWidgetHandle() {
    return widgetHandle;
  }

  protected String getChannelName() {
    return channelName;
  }
  
  public void unsubscribe() {
    widgetHandle.unsubscribe(channelName);
  }
}
