package ozone.gwt.widget.owf;

//import jsfunction.gwt.BooleanResult;
//import jsfunction.gwt.DoubleResult;
//import jsfunction.gwt.IntResult;
//import jsfunction.gwt.JsResult;
//import jsfunction.gwt.StringResult;
import ozone.gwt.widget.WidgetProxy;
import jsfunction.gwt.EventListener;
import jsfunction.gwt.JsFunction;
import jsfunction.gwt.JsReturn;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayMixed;

public final class OWFWidgetProxy extends JavaScriptObject implements WidgetProxy {
  
  protected OWFWidgetProxy() {}
  
  native String getId() /*-{
    return this.id
  }-*/;
  
//  public static native OWFWidgetProxy create(String widgetId) /*-{
//    return {
//      id : widgetId
//    }
//  }-*/;
  
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
//// JavaScript apply sends array as varargs:    fun.apply(thisArg[, argsArray])
//    // square brackets above are documentation syntax for "optional", but see below...
//    //
//    //   widgetProxy.setColor(color)
//    // I think same as: 
//    //   widgetProxy["setColor"].apply(widgetProxy, [color])
//    // (the square brackets are literal in this case, not syntax for optional
//  }-*/;
  
  public native void sendMessage(JavaScriptObject message) /*-{
    this.sendMessage(message);
////      var widgetProxy = OWF.RPC.getWidgetProxy(id);
////      widgetProxy.sendMessage({data:'foo'});
  }-*/;
  
//  public native boolean isReady() /*-{
//    return (this.isReady == true) // if "undefined", this still returns false, but "return isReady" returns null and breaks, I think
//  }-*/;
  
//  public void getWidgetProxy(NoArgsFunction readyCallback) {
//    onReady(JsFunction.create(readyCallback));
//  }
  
  static void getWidgetProxy(String widgetId, EventListener<WidgetProxy> readyCallback) {
    getWidgetProxy(widgetId, JsFunction.create(readyCallback));
  }
  
  private static native void getWidgetProxy(String widgetId, JsFunction readyCallback) /*-{
    $wnd.OWF.RPC.getWidgetProxy(id, readyCallback);
  }-*/;
  
//  private void callWithResult(String methodName, JsReturn resultCallback, Object[] functionArgs) {
//    nativeCall(methodName, JsFunction.create(resultCallback), JsFunction.varArgsToMixedArray(functionArgs));
//  }

//  public void call(String methodName, BooleanResult resultCallback, Object... functionArgs) {
//    callWithResult(methodName, resultCallback, functionArgs);
//  }
//
//  public void call(String methodName, IntResult resultCallback, Object... functionArgs) {
//    callWithResult(methodName, resultCallback, functionArgs);
//  }
//
//  public void call(String methodName, DoubleResult resultCallback, Object... functionArgs) {
//    callWithResult(methodName, resultCallback, functionArgs);
//  }
//
//  public void call(String methodName, StringResult resultCallback, Object... functionArgs) {
//    callWithResult(methodName, resultCallback, functionArgs);
//  }
//
//  public void call(String methodName, JsResult<?> resultCallback, Object... functionArgs) {
//    callWithResult(methodName, resultCallback, functionArgs);
//  }

  public void call(String methodName, JsReturn<?> resultCallback, Object... functionArgs) {
//    callWithResult(methodName, resultCallback, functionArgs);
    nativeCall(methodName, JsFunction.create(resultCallback), JsFunction.varArgsToMixedArray(functionArgs));
  }

  public void call(String methodName, Object... functionArgs) {
    nativeCall(methodName, null, JsFunction.varArgsToMixedArray(functionArgs));
  }

  /**
   * Designing around an OWF flaw: a proxy method can return a value, but the value is returned to
   * only a single proxy object, and a single callback per proxy method. If the method is called from
   * two places in the JavaScript (or GWT) execution thread, and then the result is returned 
   * asynchronously in a new execution thread, the results will be returned only to the context
   * of the last method invoked. So if we want to maintain caller context, we need to manage
   * the returned results, and dispatch results to the right matching contexts.
   * @param methodName
   * @param resultCallback
   * @param args
   */
  private native void nativeCall(String methodName, JsFunction resultCallback, JsArrayMixed args) /*-{
    var widgetProxy = this;
    if (resultCallback != null) {
//      args.push(resultCallback); // can't do this anymore because context is lost in OWF
      if (!widgetProxy.scopedCallbacks) {
        widgetProxy.scopedCallbacks = {};
      }
      var scopedCallback = widgetProxy.scopedCallbacks[methodName];
      if (!scopedCallback) {
        scopedCallback = function(result) {
          scopedCallback.queue[0](result)
          scopedCallback.queue.shift()
        };
        scopedCallback.queue = [];
        widgetProxy.scopedCallbacks[methodName] = scopedCallback;
      }
      scopedCallback.queue.push(resultCallback);
      args.push(scopedCallback);
    }
    if (widgetProxy.isReady) {
      widgetProxy[methodName].apply(widgetProxy, args);
    } else {
      $wnd.OWF.RPC.getWidgetProxy(widgetProxy.id, function(readyWidgetProxy) {
        if (widgetProxy.scopedCallbacks && !readyWidgetProxy.scopedCallbacks) {
          readyWidgetProxy.scopedCallbacks = widgetProxy.scopedCallbacks;
        }
        readyWidgetProxy[methodName].apply(readyWidgetProxy, args);
      });
    }
  }-*/;
}
