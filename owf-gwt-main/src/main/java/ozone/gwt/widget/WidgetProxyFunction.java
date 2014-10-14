package ozone.gwt.widget;

import jsfunction.JsFunction;
import jsfunction.NoArgsFunction;
import jsfunction.JsReturn;
import jsfunction.NoArgsFunctionReturn;
import jsfunction.ReturnValue;
import jsfunction.VarArgsFunction;
import jsfunction.VarArgsFunctionReturn;

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

  @SuppressWarnings("unchecked")
  public <T> void call(JsReturn<T> resultCallback, Object[] functionArgs) {
    resultCallback.onReturn((ReturnValue<T>) nativeCallWithReturn(JsFunction.varArgsToMixedArray(functionArgs)));
  }

  private native void nativeCall(JsArrayMixed mixedArray) /*-{
    this.fn.apply(null, mixedArray);
  }-*/;

  private native <T> ReturnValue<T> nativeCallWithReturn(JsArrayMixed mixedArray) /*-{
    return this.fn.apply(null, mixedArray)
  }-*/;
}
