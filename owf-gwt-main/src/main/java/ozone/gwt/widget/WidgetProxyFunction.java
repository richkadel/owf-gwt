package ozone.gwt.widget;

import jsfunction.gwt.JsFunction;
import jsfunction.gwt.JsFunctionUtils;
import jsfunction.gwt.functions.NoArgsFunction;
import jsfunction.gwt.functions.NoArgsFunctionReturn;
import jsfunction.gwt.functions.VarArgsFunction;
import jsfunction.gwt.functions.VarArgsFunctionReturn;
import jsfunction.gwt.returns.JsReturn;
import jsfunction.gwt.returns.advanced.JsReturnValue;
import jsfunction.gwt.types.JsError;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayMixed;

public final class WidgetProxyFunction extends JavaScriptObject {

  protected WidgetProxyFunction() {}
  
  static WidgetProxyFunction create(String name, NoArgsFunction fn) {
    return create(name, JsFunction.create(fn));
  }
  
  static <T> WidgetProxyFunction create(String name, NoArgsFunctionReturn<T> fn) {
    return createWithReturn(name, JsFunction.create(fn));
  }
  
  static WidgetProxyFunction create(String name, VarArgsFunction fn) {
    return create(name, JsFunction.create(fn));
  }
  
  static <T> WidgetProxyFunction create(String name, VarArgsFunctionReturn<T> fn) {
    return createWithReturn(name, JsFunction.create(fn));
  }
  
  private static native WidgetProxyFunction create(String name, JsFunction fn) /*-{
    return {
      name:name,
      fn:fn
    }
  }-*/;
  
  private static native WidgetProxyFunction createWithReturn(String name, JsFunction fn) /*-{
    var wpf = {
      name:name,
      fn:function() {
        return fn.apply(wpf, arguments).value // unwrap the value from the JsFunction API's ReturnValue result
      }
    }
    
    return wpf;
  }-*/;
  
  public native String getName() /*-{
    return this.name
  }-*/;
  
  public native JsFunction getFn() /*-{
    return this.fn
  }-*/;

  public void call(Object[] functionArgs) {
    nativeCall(JsFunctionUtils.varArgsToMixedArray(functionArgs));
  }

  public <T extends JavaScriptObject> void call(JsReturn<T> resultCallback, Object[] functionArgs) {
    try {
      JsReturnValue<T> returnValue = nativeCallWithReturn(JsFunctionUtils.varArgsToMixedArray(functionArgs));
      resultCallback.onReturn(returnValue);
    } catch (Throwable t) {
      resultCallback.onError(JsError.create(t));
    }
  }

  private native void nativeCall(JsArrayMixed mixedArray) /*-{
    this.fn.apply(this, mixedArray);
  }-*/;

  private native <T extends JavaScriptObject> JsReturnValue<T> nativeCallWithReturn(JsArrayMixed mixedArray) /*-{
    // a JsReturn value is an object with one property "value"
    return {value : this.fn.apply(this, mixedArray)}
  }-*/;
}
