package ozone.gwt.widget;

import com.google.gwt.core.client.JavaScriptObject;

public abstract class Intent<T extends JavaScriptObject> {
  
  private String action;
  
  private WidgetHandle widgetHandle;

  private boolean autoReceive = true;

  public Intent(WidgetHandle widgetHandle) {
    Class<?> subclass = getClass();
    while (getSimpleName(subclass).indexOf('$') >= 0) { 
      subclass = subclass.getSuperclass();
    }
    action = getSimpleName(subclass);

    this.widgetHandle = widgetHandle;
    widgetHandle.addIntent(this);
  }
  
  /**
   * There are differences between GWT DevMode and the other modes
   * (Production and SuperDevMode pretty much work the same).
   * Class getSimpleName() is inconsistent.
   */
  private static String getSimpleName(Class<?> clazz) {
    String name = clazz.getName();
    return name.substring(name.lastIndexOf(".")+1); // strip the package name
  }
  
  public IntentDescriptor getIntentDescriptor() {
    return IntentDescriptor.create(getAction(), getDataType());
  }
  
  /**
   * Override this method to handle received Intent
   * @param sender
   * @param data
   */
  protected abstract void intentReceived(WidgetProxy sender, T data);
  
  /**
   * Override intentReceived() to handle the message, with the correct subclass of JavaScriptObject
   * @param sender
   * @param data
   */
  @SuppressWarnings("unchecked")
  public final void coercedIntentReceived(WidgetProxy sender, JavaScriptObject data) {
    intentReceived(sender, (T)data);
  }
  
  public void receive() {
    widgetHandle.receive(this);
  }
  
  public String getAction() {
    return action;
  }
  
  public abstract String getDataType();
  
  @Override
  public int hashCode() {
    return getAction().hashCode()
          ^getDataType().hashCode();
  }
  
  @Override
  public boolean equals(Object rhs) {
    return rhs instanceof Intent
      && getAction().equals(((Intent<?>)rhs).getAction()) 
      && getDataType().equals(((Intent<?>)rhs).getDataType());
  }

  /**
   * Defaults to true. Call receive() automatically, deferred until the next JavaScript event loop.
   */
  @SuppressWarnings("unchecked")
  public <S extends Intent<?>> S setAutoReceive(boolean autoReceive) {
    this.autoReceive = false;
    return (S) this;
  }
  
  public boolean isAutoReceive() {
    return autoReceive;
  }
}
