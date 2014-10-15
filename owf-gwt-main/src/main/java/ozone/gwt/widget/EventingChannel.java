package ozone.gwt.widget;

public abstract class EventingChannel {
  
  private WidgetHandle widgetHandle;

  private String channelName;
  
  protected EventingChannel(WidgetHandle widgetHandle) {
    Class<?> subclass = getClass();
    while (subclass.getName().indexOf('$') >= 0) { 
      subclass = subclass.getSuperclass();
    }
    channelName = subclass.getName();
    
    this.widgetHandle = widgetHandle;
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
