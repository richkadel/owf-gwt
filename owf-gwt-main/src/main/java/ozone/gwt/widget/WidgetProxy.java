package ozone.gwt.widget;

import jsfunction.gwt.JsReturn;

import com.google.gwt.core.client.JavaScriptObject;

public interface WidgetProxy {
  
  public void sendMessage(JavaScriptObject message);
  
  /**
   * You can call a method that returns a value, or even one that doesn't, using JsReturnVoid,
   * which will give you the opportunity to catch any Error exceptions, including the
   * possibility that the method is not actually registered with the widget being called.
   * 
   * @param methodName
   * @param resultCallback
   * @param functionArgs
   */
  public void call(String methodName, JsReturn<?> resultCallback, Object... functionArgs);

  /**
   * Fire and forget. If the method is not registered for the widget behind this proxy,
   * this call will simply be ignored.
   * 
   * @param methodName
   * @param functionArgs
   */
  public void call(String methodName, Object... functionArgs);
}
