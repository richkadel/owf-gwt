package ozone.gwt.widget;

import java.util.Collection;
import java.util.List;

import jsfunction.gwt.functions.EventListener;
import jsfunction.gwt.functions.NoArgsFunction;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.IsWidget;

public interface WidgetHandle {
  
  public IsWidget getGWTIsWidget();
  
  public void activate();

  public void notifyWidgetReady();
  
  public void handleDirectMessage(EventListener<JavaScriptObject> directMessageCallback);
  
  public void registerWidgetProxyFunctions(WidgetProxyFunctions widgetProxyFunctions);
  
  public abstract void publish(String channelName, String message, WidgetProxy dest);

  public abstract void subscribe(String channelName, EventListener<StringMessage> handler);

  public abstract void unsubscribe(String channelName);
  
  public void startActivity(Intent<?> activity, JavaScriptObject data, OnReceipt onReceipt);
  
  public void receive(Intent<?> activity);
  
  public boolean hasWidgetHeader();
  
  /**
   * If hasWidgetHeader() returns false, this method throws an exception. If true,
   * the button is added to the Widget Chrome, per the JavaDoc at
   * OZONEWidgetFramework#addWidgetHeaderButton().
   * @param buttonType see OZONEWidgetFramework
   * @param callback
   * @throws NoSuchMethodException
   */
  public void addWidgetHeaderButton(String buttonType, NoArgsFunction callback) throws NoSuchMethodException;

  public Collection<Intent<?>> getIntentsReceived();
}
