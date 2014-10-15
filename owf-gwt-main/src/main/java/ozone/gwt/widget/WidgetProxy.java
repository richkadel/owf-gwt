package ozone.gwt.widget;

//import jsfunction.gwt.BooleanResult;
//import jsfunction.gwt.DoubleResult;
//import jsfunction.gwt.EventListener;
//import jsfunction.gwt.IntResult;
//import jsfunction.gwt.JsResult;
//import jsfunction.gwt.StringResult;

import jsfunction.gwt.JsReturn;
import jsfunction.gwt.NoArgsFunction;

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

  public void onReady(NoArgsFunction noArgsFunction);
}
