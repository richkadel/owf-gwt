package ozone.gwt.widget;

import jsfunction.gwt.JsError;
import jsfunction.gwt.JsFunction;
import jsfunction.gwt.JsReturn;
import jsfunction.gwt.NoArgsFunction;
import jsfunction.gwt.NoArgsFunctionReturn;
import jsfunction.gwt.JsReturnValue;
import jsfunction.gwt.VarArgsFunction;
import jsfunction.gwt.VarArgsFunctionReturn;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayMixed;

public final class WidgetProxyFunction extends JavaScriptObject {

  protected WidgetProxyFunction() {}
  
  static WidgetProxyFunction create(String name, NoArgsFunction fn) {
    return create(name, JsFunction.create(fn));
  }
  
  static <T> WidgetProxyFunction create(String name, NoArgsFunctionReturn<T> fn) {
    return create(name, JsFunction.create(fn));
  }
  
  static WidgetProxyFunction create(String name, VarArgsFunction fn) {
    return create(name, JsFunction.create(fn));
  }
  
  static <T> WidgetProxyFunction create(String name, VarArgsFunctionReturn<T> fn) {
    return create(name, JsFunction.create(fn));
  }
  
  private static native WidgetProxyFunction create(String name, JsFunction fn) /*-{
    return {
      name:name,
      fn:fn
    }
  }-*/;
  
  public native String getName() /*-{
    return this.name
  }-*/;
  
  public native JsFunction getFn() /*-{
    return this.fn
  }-*/;

  public void call(Object[] functionArgs) {
    nativeCall(JsFunction.varArgsToMixedArray(functionArgs));
  }

  public <T extends JavaScriptObject> void call(JsReturn<T> resultCallback, Object[] functionArgs) {
    try {
      JsReturnValue<T> returnValue = nativeCallWithReturn(JsFunction.varArgsToMixedArray(functionArgs));
      resultCallback.onReturn(returnValue);
    } catch (Throwable t) {
      resultCallback.onError(JsError.create(t));
    }
  }

  private native void nativeCall(JsArrayMixed mixedArray) /*-{
    this.fn.apply(null, mixedArray);
  }-*/;

  private native <T extends JavaScriptObject> JsReturnValue<T> nativeCallWithReturn(JsArrayMixed mixedArray) /*-{
    return this.fn.apply(null, mixedArray)
  }-*/;
}
