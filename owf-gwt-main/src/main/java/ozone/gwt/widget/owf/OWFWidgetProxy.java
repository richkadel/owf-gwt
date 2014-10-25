package ozone.gwt.widget.owf;

//import jsfunction.gwt.BooleanResult;
//import jsfunction.gwt.DoubleResult;
//import jsfunction.gwt.IntResult;
//import jsfunction.gwt.JsResult;
//import jsfunction.gwt.StringResult;
import ozone.gwt.widget.WidgetProxy;
import jsfunction.gwt.JsFunction;
import jsfunction.gwt.JsFunctionUtils;
import jsfunction.gwt.functions.EventListener;
import jsfunction.gwt.functions.NoArgsFunction;
import jsfunction.gwt.returns.JsResultOrError;
import jsfunction.gwt.returns.JsReturn;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayMixed;

public final class OWFWidgetProxy extends JavaScriptObject implements WidgetProxy {
  
  protected OWFWidgetProxy() {}
  
  native String getId() /*-{
    return this.id
  }-*/;
  
  
  public native void sendMessage(JavaScriptObject message) /*-{
    this.sendMessage(message);
    
// This method supports the equivalent of the following JavaScript example:
//
////      var widgetProxy = OWF.RPC.getWidgetProxy(id);
////      widgetProxy.sendMessage({data:'foo'});
  }-*/;
  
// I am not implementing this because OWF GWT is not supporting non-ready widget proxies.
// If you want a WidgetProxy, you can get it with getWidgetProxy(id, callback), and the proxy will be ready
// If the native underlying OWF code gets a non-ready widgetProxy, it should not return it, but instead
// it should call the getWidgetProxy method, and when returned, the wrapping method should return the
// ready version only.  Since most OWF functions are asynchronous with deferred results, this
// additional level of deferred execution is not visible to the caller.
//
//  public native boolean isReady() /*-{
//    return (this.isReady == true) // if "undefined", this still returns false, but "return isReady" returns null and breaks, I think
//  }-*/;
//
//  public void getWidgetProxy(NoArgsFunction readyCallback) {
//    onReady(JsFunction.create(readyCallback));
//  }
  
  static void getWidgetProxy(String widgetId, EventListener<WidgetProxy> readyCallback) {
    getWidgetProxy(widgetId, JsFunction.create(readyCallback));
    
// This method supports the equivalent of the following JavaScript example:
//
////    OWF.RPC.getWidgetProxy('instanceGuid of widgetA', function(widgetA) {
////
////      widgetA.add(1,2,3, function(result) {
////        console.log(result); // log the result
////      })
////
////      widgetA.sendMessage('some secret message');
////
////    });
  }
  
  private static native void getWidgetProxy(String widgetId, JsFunction readyCallback) /*-{
    $wnd.OWF.RPC.getWidgetProxy(widgetId, readyCallback);
  }-*/;
  
  public void call(String methodName, JsReturn<?> resultCallback, Object... functionArgs) {
    nativeCall(methodName, JsFunction.create(resultCallback), JsFunctionUtils.varArgsToMixedArray(functionArgs));
    
// This method supports the equivalent of the following JavaScript example:
//
//// JavaScript apply sends array as varargs:    fun.apply(thisArg[, argsArray])
//    // square brackets above are documentation syntax for "optional", but see below...
//    //
//    //   widgetProxy.setColor(color)
//    // I think same as: 
//    //   widgetProxy["setColor"].apply(widgetProxy, [color])
//    // (the square brackets are literal in this case, not syntax for optional
//  }-*/;
  }

  public void call(String methodName, Object... functionArgs) {
    nativeCall(methodName, null, JsFunctionUtils.varArgsToMixedArray(functionArgs));
  }

  /**
   * Designing around an OWF flaw: a proxy method can return a value, but the value is returned to
   * only a single proxy object, and a single callback per proxy method. If the method is called from
   * two places in the JavaScript (or GWT) execution thread, and then the result is returned 
   * asynchronously in a new execution thread, the results will be returned only to the context
   * of the last method invoked. So if we want to maintain caller context, we need to manage
   * the returned results, and dispatch results to the right matching contexts.
   * @param methodName
   * @param jsResultOrError
   * @param args
   */
  private native void nativeCall(String methodName, JsResultOrError jsResultOrError, JsArrayMixed args) /*-{
    var widgetProxy = this;
    if (jsResultOrError != null) {
      if (!widgetProxy.scopedCallbacks) {
        widgetProxy.scopedCallbacks = {};
      }
      var scopedCallback = widgetProxy.scopedCallbacks[methodName];
      if (!scopedCallback) {
        scopedCallback = function(result) {
          var scopedCallback = widgetProxy.scopedCallbacks[methodName];
          scopedCallback.queue[0].result(result) // calling the "result" function from the jsResultOrError
          scopedCallback.queue.shift()
        };
        scopedCallback.queue = []; // start a queue of jsResultOrError objects
        widgetProxy.scopedCallbacks[methodName] = scopedCallback;
      }
      scopedCallback.queue.push(jsResultOrError); // jsResultOrError has properties "result" and "error", that are functions
      args.push(scopedCallback);
    }
    if (widgetProxy.isReady) {
      if (widgetProxy[methodName]) {
        widgetProxy[methodName].apply(widgetProxy, args);
      } else {
        var scopedCallback = widgetProxy.scopedCallbacks[methodName];
        scopedCallback.queue[0].error(new ReferenceError("Unsupported method: "+methodName))
        scopedCallback.queue.shift()
      }
    } else {
      $wnd.OWF.RPC.getWidgetProxy(widgetProxy.id, function(readyWidgetProxy) {
        if (widgetProxy.scopedCallbacks && !readyWidgetProxy.scopedCallbacks) {
          readyWidgetProxy.scopedCallbacks = widgetProxy.scopedCallbacks;
        }
        if (readyWidgetProxy[methodName]) {
          readyWidgetProxy[methodName].apply(readyWidgetProxy, args);
        } else {
          var scopedCallback = readyWidgetProxy.scopedCallbacks[methodName];
          scopedCallback.queue[0].error(new ReferenceError("Unsupported method: "+methodName))
          scopedCallback.queue.shift()
        }
      });
    }
  }-*/;

  public void onReady(NoArgsFunction noArgsFunction) {
    onReady(JsFunction.create(noArgsFunction));
  }

  private native void onReady(JsFunction create) /*-{
    this.onReady(create);
  }-*/;

  // Just for internal inspection
  static native boolean isReady(WidgetProxy sender) /*-{
    return (sender.isReady == true) // safe way to return boolean from JavaScript to GWT Java
  }-*/;
}
