package ozone.gwt.widget.owf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import ozone.gwt.widget.Intent;
import ozone.gwt.widget.OnReceipt;
import ozone.gwt.widget.StringMessage;
import ozone.gwt.widget.WidgetContainer;
import ozone.gwt.widget.WidgetFramework;
import ozone.gwt.widget.WidgetHandle;
import ozone.gwt.widget.WidgetLogger;
import ozone.gwt.widget.WidgetProxy;
import ozone.gwt.widget.WidgetProxyFunction;
import ozone.gwt.widget.WidgetProxyFunctions;
import jsfunction.gwt.JsFunction;
import jsfunction.gwt.JsFunctionUtils;
import jsfunction.gwt.functions.EventListener;
import jsfunction.gwt.functions.NoArgsFunction;
import jsfunction.gwt.functions.VarArgsFunction;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayMixed;
import com.google.gwt.user.client.ui.IsWidget;
//import com.harmonia.sapphire.client.MessageHandler;
//import com.harmonia.sapphire.client.Sapphire;

public class OZONEWidgetFramework extends WidgetFramework implements WidgetHandle { 
  
  private static final Logger log = Logger.getLogger(OZONEWidgetFramework.class.getName());
  
  private static boolean isReady = false;
  
  private IsWidget gwtIsWidget;

  private OWFWidgetState widgetState;
  
  private OWFLogger owfLogger = OWFLogger.create();

  private List<Intent<?>> intentsReceived = new ArrayList<Intent<?>>();

  public static native boolean isRunningInOWF() /*-{
    if (window.parent.name.indexOf("preferenceLocation\":\"") > 0) {
      return true;
    } else {
      return false;
    }
  }-*/;
  
  protected OZONEWidgetFramework() {
    widgetState = getWidgetState();
  }
  
//  @Override
//  public void addSaveWidgetCallback(NoArgsFunction callback) {
//    addWidgetHeaderButton("save", callback);
//  }
  
  @Override
  public boolean hasWidgetHeader() {
    return true;
  }
  
  /**
   * If hasWidgetHeader() is true, this method can add a button to the header to perform
   * a callback on the widget.
   * 
   * It appears that OWF supports the tool types from EXT/JS 4, documented here:
   * 
   * http://www.objis.com/formationextjs/lib/extjs-4.0.0/docs/api/Ext.panel.Tool.html
   * 
   * This includes the following list:
   * 
   * <ul>
   * <li>close</ul>
   * <li>collapse</ul>
   * <li>down</ul>
   * <li>expand</ul>
   * <li>gear</ul>
   * <li>help</ul>
   * <li>left</ul>
   * <li>maximize</ul>
   * <li>minimize</ul>
   * <li>minus</ul>
   * <li>move</ul>
   * <li>next</ul>
   * <li>pin</ul>
   * <li>plus</ul>
   * <li>prev</ul>
   * <li>print</ul>
   * <li>refresh</ul>
   * <li>resize</ul>
   * <li>restore</ul>
   * <li>right</ul>
   * <li>save</ul>
   * <li>search</ul>
   * <li>toggle</ul>
   * <li>unpin</ul>
   * <li>up</ul>
   * </ol>
   */
  @Override
  public void addWidgetHeaderButton(String type, NoArgsFunction callback) {
    nativeAddWidgetHeaderButton(type, JsFunction.create(callback));
  }
  
  private native void nativeAddWidgetHeaderButton(String type, JsFunction callback) /*-{
    $wnd.OWF.ready(function() {
      // Note OWF uses a Dojo isArray() method that does not work on arrays created "from a different window".
      // I think the fact that GWT uses an iFrame (and I have to invoke OWF via $wnd.OWF instead of the
      // global "OWF") means we are createing these arrays from a different "window" so the isArray
      // does not work!  Therefore, we can't use arrays here, BUT since we only have one element, and
      // the reason "isArray" is called is so OWF can decide if it should wrap the object in an array,
      // We pass in a single object ONLY, and allow OWF to wrap it.  If we don't do this, and isArray()
      // returns false, it double-wraps the array, causing OWF to fail to get the configuration object
      // properties down the line.  Note if we ever neeed to pass in an array of multiple values,
      // maybe a workaround would be to declare a dummy function in $wnd and execute it, so it can
      // create the arrays and execute the method. Is that possible?
      $wnd.OWF.Chrome.removeHeaderButtons({
        items:{
          itemId: type
        }
      });
      $wnd.OWF.Chrome.insertHeaderButtons({
        items:{
          type: type, // standard EXT/JS tool
          itemId: type,
          handler: callback // function(sender, data) { do something; }
        }
      });
    })
  }-*/;

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

  @Override
  public WidgetLogger getLogger() {
    return owfLogger;
  }
  
  @Override
  public void activate() {
    widgetState.activateWidget();
  }

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
    
// This method supports the equivalent of the following JavaScript example:
//
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
    
// This method supports the equivalent of the following JavaScript example:
//
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

  public void message(String title, String messageText) {
    owfLogger.info(title+": "+messageText);
  }

  @Override
  public void publish(String channelName, String message, WidgetProxy dest) {
    String destWidgetId = null;
    if (dest != null) {
      destWidgetId = ((OWFWidgetProxy)dest).getId();
    }
    publish(channelName, message, destWidgetId);
  }
  
  public native void publish(String channelName, String message, String destWidgetId) /*-{
    if (destWidgetId) {
      $wnd.OWF.Eventing.publish(channelName, message, destWidgetId);
    } else {
      $wnd.OWF.Eventing.publish(channelName, message);
    }
  }-*/;

  @Override
  public void subscribe(String channelName, final EventListener<StringMessage> handler) {
    subscribe(
      channelName, 
      JsFunction.create(new VarArgsFunction() {
        public void callback(final JsArrayMixed args) {
          String senderWidgetId = args.getString(0);
          OWFWidgetProxy.getWidgetProxy(senderWidgetId, new EventListener<WidgetProxy>() {
            public void callback(final WidgetProxy sender) {
//              sender.onReady(new NoArgsFunction() {
//                public void callback() {
                  String message = args.getString(1);
                  handler.callback(new StringMessage(sender, message)); // ignore third parameter, channel name
                }
//              });
//            }
          });
        }
      })
    );
  }

  private native void subscribe(String channelName, JsFunction handler) /*-{
    $wnd.OWF.Eventing.subscribe(channelName, handler);
  }-*/;

  @Override
  public native void unsubscribe(String channelName) /*-{
    $wnd.OWF.Eventing.unsubscribe(channelName);
  }-*/;
  
  @Override
  public void startActivity(Intent<?> intent, JavaScriptObject data, final OnReceipt onReceipt) {
    JsFunction receiptFunction = null;
    if (onReceipt != null) {
      receiptFunction = JsFunction.create(
        new EventListener<JsArray<OWFWidgetProxy>>() {
          @Override
          public void callback(JsArray<OWFWidgetProxy> dests) {
            int len = dests.length();
            for (int i = 0; i < len; i++) {
              
//              callWhenReady(onReceipt, dests.get(i));
// These should already be "ready" proxies
if (!OWFWidgetProxy.isReady(dests.get(i))) {
  throw new Error("Given WidgetProxy is not ready! It should be. Wrap in OWFWidgetProxy.getWidgetProxy() callback method before returning.");
}
// I can remove this check after some testing
              onReceipt.intentReceived(dests.get(i));
            }
          }
        }
      );
    }
    nativeStartActivity(intent.getAction(), intent.getDataType(), data, receiptFunction);
  }
  
// I can remove this method after some testing
//  private void callWhenReady(final OnReceipt onReceipt, final OWFWidgetProxy owfWidgetProxy) {
//    owfWidgetProxy.onReady(new NoArgsFunction() {
//      public void callback() {
//        onReceipt.intentReceived(owfWidgetProxy);
//      }
//    });
//  }

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
    intentsReceived.add(intent);
    nativeReceive(
      intent.getAction(),
      intent.getDataType(),
      JsFunction.create(new EventListener<IntentReceived>() {
        @Override
        public void callback(IntentReceived event) {
//          callWhenReady(intent, event.getSender(), event.getData());
// sender should already be a "ready" proxy
if (!OWFWidgetProxy.isReady(event.getSender())) {
  throw new Error("Given WidgetProxy is not ready! It should be. Wrap in OWFWidgetProxy.getWidgetProxy() callback method before returning.");
}
// I can remove this check after some testing
          intent.coercedIntentReceived(event.getSender(), event.getData());
        }
      })
    );
  }
  
// I can remove this method after some testing
//  private void callWhenReady(final Intent<?> intent, final WidgetProxy sender, final JavaScriptObject data) {
//    sender.onReady(new NoArgsFunction() {
//      public void callback() {
//        intent.coercedIntentReceived(sender, data);
//      }
//    });
//  }
  
  private native void nativeReceive(String action, String dataType, JsFunction listener) /*-{
    $wnd.OWF.Intents.receive(
      {
        action : action,
        dataType : dataType
      },
      function(sender, intent, data) {
        $wnd.OWF.RPC.getWidgetProxy
            (sender, function(senderWidgetProxy) {
          senderWidgetProxy.onReady(function() {
            listener({
              senderWidgetProxy : senderWidgetProxy,
              intent : intent,
              data : data.data
            })
          })
        })
      }
    );
  }-*/;
  
  private final static class OWFLogger extends JavaScriptObject implements WidgetLogger {
  
    protected OWFLogger() {}
  
    static native OWFLogger create() /*-{
      var logger = $wnd.Ozone.log.getDefaultLogger();
      $wnd.OWF.Log.setEnabled(true);
      var appender = logger.getEffectiveAppenders()[0];
      appender.setThreshold($wnd.log4javascript.Level.INFO);
      return logger;
    }-*/;
  
    @Override
    public void trace(Object... messages) {
      trace(JsFunctionUtils.varArgsToMixedArray(messages));
    }

    public native void trace(JsArrayMixed messages) /*-{
      this.trace.apply(this, messages);
    }-*/;

    @Override
    public void debug(Object... messages) {
      debug(JsFunctionUtils.varArgsToMixedArray(messages));
    }

    public native void debug(JsArrayMixed messages) /*-{
      this.debug.apply(this, messages);
    }-*/;

    @Override
    public void info(Object... messages) {
      info(JsFunctionUtils.varArgsToMixedArray(messages));
    }

    public native void info(JsArrayMixed messages) /*-{
      this.info.apply(this, messages);
    }-*/;

    @Override
    public void warn(Object... messages) {
      warn(JsFunctionUtils.varArgsToMixedArray(messages));
    }

    public native void warn(JsArrayMixed messages) /*-{
      this.warn.apply(this, messages);
    }-*/;

    @Override
    public void error(Object... messages) {
      error(JsFunctionUtils.varArgsToMixedArray(messages));
    }

    public native void error(JsArrayMixed messages) /*-{
      this.error.apply(this, messages);
    }-*/;

    @Override
    public void fatal(Object... messages) {
      fatal(JsFunctionUtils.varArgsToMixedArray(messages));
    }
    
    public native void fatal(JsArrayMixed messages) /*-{
      this.fatal.apply(this, messages);
    }-*/;
  }

  private static final class IntentReceived extends JavaScriptObject {

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

  @Override
  public Collection<Intent<?>> getIntentsReceived() {
    return intentsReceived;
  }
}
