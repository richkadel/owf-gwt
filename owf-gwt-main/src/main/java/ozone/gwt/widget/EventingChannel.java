package ozone.gwt.widget;

public abstract class EventingChannel {
  
  private WidgetHandle widgetHandle;

  private String channelName;

  private boolean autoSubscribe = true;
  
  protected EventingChannel(WidgetHandle widgetHandle) {
    Class<?> subclass = getClass();
    while (subclass.getName().indexOf('$') >= 0) { 
      subclass = subclass.getSuperclass();
    }
    channelName = subclass.getName();
    
    this.widgetHandle = widgetHandle;
    widgetHandle.addEventingChannel(this);
  }
  
  protected WidgetHandle getWidgetHandle() {
    return widgetHandle;
  }

  protected String getChannelName() {
    return channelName;
  }
  
  public abstract <S extends EventingChannel> S subscribe();
  
  public void unsubscribe() {
    widgetHandle.unsubscribe(channelName);
  }

  /**
   * Defaults to true. Call subscribe() automatically, deferred until the next JavaScript event loop.
   */
  @SuppressWarnings("unchecked")
  public <S extends EventingChannel> S setAutoSubscribe(boolean autoSubscribe) {
    this.autoSubscribe = false;
    return (S) this;
  }
  
  public boolean isAutoSubscribe() {
    return autoSubscribe;
  }
}
