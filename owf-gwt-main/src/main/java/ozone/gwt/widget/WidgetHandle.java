package ozone.gwt.widget;

import jsfunction.gwt.EventListener;
import jsfunction.gwt.VarArgsFunction;

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
}
