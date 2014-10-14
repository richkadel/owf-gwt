package ozone.gwt.widget.owf;

import java.util.logging.Logger;

import ozone.gwt.widget.Intent;
import ozone.gwt.widget.OnReceipt;
import ozone.gwt.widget.StringMessage;
import ozone.gwt.widget.WidgetContainer;
import ozone.gwt.widget.WidgetFramework;
import ozone.gwt.widget.WidgetHandle;
import ozone.gwt.widget.WidgetProxy;
import ozone.gwt.widget.WidgetProxyFunction;
import ozone.gwt.widget.WidgetProxyFunctions;
import jsfunction.EventListener;
import jsfunction.JsFunction;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.IsWidget;
//import com.harmonia.sapphire.client.MessageHandler;
//import com.harmonia.sapphire.client.Sapphire;

public class OZONEWidgetFramework extends WidgetFramework implements WidgetHandle { //, MessageHandler {
  
  private static final Logger log = Logger.getLogger(OZONEWidgetFramework.class.getName());
  
  private static boolean isReady = false;
  
  private IsWidget gwtIsWidget;

  private OWFWidgetState widgetState;
  
  private OWFLogger owfLogger = OWFLogger.create();

  public static native boolean isRunningInOWF() /*-{
    if (window.parent.name.indexOf("preferenceLocation\":\"") > 0) {
      return true;
    } else {
      return false;
    }
  }-*/;
  
  protected OZONEWidgetFramework() {
//    Sapphire.setMessageHandler(this);
    widgetState = getWidgetState();
  }

  private native OWFWidgetState getWidgetState() /*-{
    var widgetEventingController = $wnd.Ozone.eventing.Widget.getInstance();
    var widgetState = $wnd.Ozone.state.WidgetState.getInstance({
      widgetEventingController:  widgetEventingController,
//      autoInit: true // this is the default but I don't know if I want to listen for state events yet
//      ,
      autoInit: false
//      ,
//      onStateEventReceived: handleStateEvent
    }); 
    return widgetState
  }-*/;
  
  private static native OWFWidgetData getWidgetData() /*-{
    return JSON.parse(window.parent.name)
  }-*/;
  
  @Override
  public WidgetHandle createWidgetHandleInstance(WidgetContainer widgetContainer, IsWidget gwtIsWidget) {
    if (widgetContainer != null) {
      throw new Error("OZONEWidgetFramework does not support WidgetContainers."
          + " The GWT IsWidget should have been constructed with a no-arg constructor,"
          + " automatically via the GWT EntryPoint.");
    }
    if (this.gwtIsWidget != null) {
      throw new Error("OZONEWidgetFramework does allow multiple gwtIsWidgets to be the OWF WidgetHandles for a single OZONE Widget");
    }
    this.gwtIsWidget = gwtIsWidget;
    return this;
  }

  @Override
  public WidgetHandle getWidgetHandleInstance(IsWidget gwtIsWidget) {
    if (this.gwtIsWidget != gwtIsWidget) {
      throw new Error("OZONEWidgetFramework does allow multiple gwtIsWidgets to be the OWF WidgetHandles for a single OZONE Widget");
    }
    return this;
  }

//  @Override
//  public WidgetHandle getWidgetHandleOrDummy(IsWidget gwtIsWidget) {
//    return getWidgetHandle(gwtIsWidget);
//  }
  
  @Override
  public void activate() {
//    OZONEWidgetFramework.this.nativeActivate();
    widgetState.activateWidget();
  }

//  private native void nativeActivate() /*-{
//    $wnd.OWF.widgetState.activateWidget($wnd.OWF.getWidgetGuid()); // not providing callback...not planning to use one... Is this OK?
//  }-*/;

  @Override
  public void notifyWidgetReady() {
    nativeNotifyWidgetReady();
  }

  public native void nativeNotifyWidgetReady() /*-{
    $wnd.OWF.notifyWidgetReady();
  }-*/;

  @Override
  public IsWidget getGWTIsWidget() {
    return gwtIsWidget;
  }

  @Override
  public void handleDirectMessage(
      EventListener<JavaScriptObject> directMessageCallback) {
    
    handleDirectMessage(JsFunction.create(directMessageCallback));
    
//  OWF.RPC.handleDirectMessage(function(msg) {
//    // do something with the message
//  });
  }

  private native void handleDirectMessage(JsFunction directMessageCallbackFunction) /*-{
    $wnd.OWF.RPC.handleDirectMessage(directMessageCallbackFunction);
  }-*/;

  @Override
  public void registerWidgetProxyFunctions(WidgetProxyFunctions widgetProxyFunctions) {
    this.nativeRegisterWidgetProxyFunctions(widgetProxyFunctions.toJsArray());
  }
  
  private native void nativeRegisterWidgetProxyFunctions(JsArray<WidgetProxyFunction> widgetProxyFunctions) /*-{
    
    $wnd.OWF.RPC.registerFunctions(widgetProxyFunctions);
    
//    Calculator = {
//        add: function() {
//          var args = arguments,
//            val = 0;
//          for(var i = 0, len = args.length; i < len; i++) {
//            val += parseFloat(args[i]);
//          }
//          return val;
//        },
//        multiply: function() {
//          var args = arguments,
//            val = 1;
//          for(var i = 0, len = args.length; i < len; i++) {
//            val *= parseFloat(args[i]);
//          }
//          return val;
//        }
//      };
//      OWF.RPC.registerFunctions([
//        {
//          name: 'add'
//            fn: Calculator.add,
//            scope: Calculator
//          },
//          {
//            name: 'multiply'
//            fn: Calculator.multiply,
//            scope: Calculator
//          }
//        ]);
  }-*/;

//  @Override
//  public void message(String title, String messageText) {
//    owfLogger.info(title+": "+messageText);
//  }

  @Override
  public void publish(String channelName, String message, WidgetProxy dest) {
    String destWidgetId = null;
    if (dest != null) {
      destWidgetId = ((OWFWidgetProxy)dest).getId();
    }
    publish(channelName, message, destWidgetId);
  }
  
  public native void publish(String channelName, String message, String destWidgetId) /*-{
    $wnd.OWF.Eventing.publish(channelName, message, destWidgetId);
  }-*/;

  @Override
  public void subscribe(String channelName, EventListener<StringMessage> handler) {
    subscribe(channelName, JsFunction.create(handler));
  }

  private native void subscribe(String channelName, JsFunction handler) /*-{
    $wnd.OWF.Eventing.publish(channelName, handler);
  }-*/;

  @Override
  public native void unsubscribe(String channelName) /*-{
    $wnd.OWF.Eventing.unsubscribe(channelName);
  }-*/;
  
  private final static class OWFLogger extends JavaScriptObject {
  
    protected OWFLogger() {}
  
    static native OWFLogger create() /*-{
      var logger = $wnd.Ozone.log.getDefaultLogger();
      $wnd.OWF.Log.setEnabled(true);
      var appender = logger.getEffectiveAppenders()[0];
      appender.setThreshold($wnd.log4javascript.Level.INFO);
      return logger;
    }-*/;
  
    void info(String message) {
      this.info(message);
    }
  }
  
  @Override
  public void startActivity(Intent<?> intent, JavaScriptObject data, final OnReceipt onReceipt) {
    JsFunction receiptFunction = null;
    if (onReceipt != null) {
      receiptFunction = JsFunction.create(
        new EventListener<JsArray<OWFWidgetProxy>>() {
          @Override
          public void callback(JsArray<OWFWidgetProxy> dests) {
            // These should already be "ready" proxies
            int len = dests.length();
            for (int i = 0; i < len; i++) {
              onReceipt.intentReceived(dests.get(i));
            }
          }
        }
      );
    }
    nativeStartActivity(intent.getAction(), intent.getDataType(), data, receiptFunction);
  }
  
  private native void nativeStartActivity(String action, String dataType,
      JavaScriptObject data, JsFunction onReceipt) /*-{
    $wnd.OWF.Intents.startActivity(
      {
        action : action, 
        dataType : dataType
      },
      {
        data : data
      },
      onReceipt
//    , not passing in desination widget proxies, which is an OWF option
    );
  }-*/;
  
  @Override
  public void receive(final Intent<?> intent) {
    nativeReceive(
      intent.getAction(),
      intent.getDataType(),
      JsFunction.create(new EventListener<IntentReceived>() {
        @Override
        public void callback(IntentReceived event) {
          intent.coercedIntentReceived(event.getSender(), event.getData());
        }
      })
    );
  }
  
  private native void nativeReceive(String action, String dataType, JsFunction listener) /*-{
    $wnd.OWF.Intents.receive(
      {
        action : action,
        dataType : dataType
      },
      function(sender, intent, data) {
        var senderWidgetProxy = $wnd.OWF.RPC.getWidgetProxy(sender);
        listener({
          senderWidgetProxy : senderWidgetProxy,
          intent : intent,
          data : data.data
        })
      }
    );
  }-*/;
  
  public static final class IntentReceived extends JavaScriptObject {

    protected IntentReceived() {}
    
    public native WidgetProxy getSender() /*-{
      return this.senderWidgetProxy
    }-*/;
    
    public native JavaScriptObject getIntent() /*-{
      return this.intent
    }-*/;
    
    public native JavaScriptObject getData() /*-{
      return this.data
    }-*/;
  }
}
