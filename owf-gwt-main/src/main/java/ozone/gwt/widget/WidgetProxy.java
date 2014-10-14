package ozone.gwt.widget;

import jsfunction.JsReturn;
//import jsfunction.BooleanResult;
//import jsfunction.DoubleResult;
//import jsfunction.EventListener;
//import jsfunction.IntResult;
//import jsfunction.JsResult;
//import jsfunction.StringResult;

import com.google.gwt.core.client.JavaScriptObject;

public interface WidgetProxy {
  
  public void sendMessage(JavaScriptObject message);
  
//  public void call(String methodName, BooleanResult resultCallback, Object... functionArgs);
//  
//  public void call(String methodName, IntResult resultCallback, Object... functionArgs);
//  
//  public void call(String methodName, DoubleResult resultCallback, Object... functionArgs);
//  
//  public void call(String methodName, StringResult resultCallback, Object... functionArgs);
//  
//  public void call(String methodName, JsResult<?> resultCallback, Object... functionArgs);
  
  public void call(String methodName, JsReturn<?> resultCallback, Object... functionArgs);

  public void call(String methodName, Object... functionArgs);
}
