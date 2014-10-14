package ozone.gwt.widget.direct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import ozone.gwt.widget.Intent;
import ozone.gwt.widget.OnReceipt;
import ozone.gwt.widget.StringMessage;
import ozone.gwt.widget.WidgetContainer;
import ozone.gwt.widget.WidgetHandle;
import ozone.gwt.widget.WidgetProxy;
import ozone.gwt.widget.WidgetProxyFunction;
import ozone.gwt.widget.WidgetProxyFunctions;
//import jsfunction.DoubleResult;
//import jsfunction.IntResult;
//import jsfunction.BooleanResult;
//import jsfunction.JsResult;
//import jsfunction.StringResult;
import jsfunction.EventListener;
import jsfunction.JsReturn;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.IsWidget;

public class DirectWidgetHandle implements WidgetHandle, WidgetProxy {

  private static final Map<Intent<?>,IntentHandler> intentHandlers = new HashMap<Intent<?>,IntentHandler>();
  
  private static Map<String,List<Subscription>> channels = new HashMap<String,List<Subscription>>();
  
  private IsWidget gwtIsWidget;
  private WidgetContainer widgetContainer;
  
  private EventListener<JavaScriptObject> directMessageCallback;
  private boolean ready;
  
  private  List<EventListener<WidgetProxy>> readyCallbacks;
  
  private JsArray<WidgetProxyFunction> registeredFunctions;
  
  public DirectWidgetHandle(WidgetContainer widgetContainer, IsWidget gwtIsWidget) {
    this.widgetContainer = widgetContainer;
    this.gwtIsWidget = gwtIsWidget;
  }

  @Override
  public void activate() {
    if (widgetContainer != null) {
      widgetContainer.activate(this);
    }
  }

  @Override
  public void notifyWidgetReady() {
    if (!ready) {
      ready = true;
      if (readyCallbacks != null) {
        for (EventListener<WidgetProxy> readyCallback : readyCallbacks) {
          readyCallback.callback(this);
        }
      }
      readyCallbacks = null;
    }
  }
  
  public IsWidget getGWTIsWidget() {
    return gwtIsWidget;
  }
  
  public void destroy() {
    DirectWidgetFramework.removeWidget(gwtIsWidget);
  }

  @Override
  public void handleDirectMessage(
      EventListener<JavaScriptObject> directMessageCallback) {
    if (this.directMessageCallback != null) {
      throw new Error("Was not expecting handleDirectMessage to be called more than once for the same widget");
    }
    this.directMessageCallback = directMessageCallback;
  }

  @Override
  public void registerWidgetProxyFunctions(WidgetProxyFunctions widgetProxyFunctions) {
    if (this.registeredFunctions != null) {
      throw new Error("Was not expecting registerWidgetProxyFunctions to be called more than once for the same widget");
    }
    this.registeredFunctions = widgetProxyFunctions.toJsArray();
  }
  
  @Override
  public void sendMessage(JavaScriptObject message) {
    if (directMessageCallback != null) {
      directMessageCallback.callback(message);
    }
  }
  
//  @Override
//  public void call(String methodName, BooleanResult resultCallback,
//      Object... functionArgs) {
//    callWithResult(methodName, resultCallback, functionArgs);
//  }
//  
//  @Override
//  public void call(String methodName, IntResult resultCallback,
//      Object... functionArgs) {
//    callWithResult(methodName, resultCallback, functionArgs);
//  }
//  
//  @Override
//  public void call(String methodName, DoubleResult resultCallback,
//      Object... functionArgs) {
//    callWithResult(methodName, resultCallback, functionArgs);
//  }
//  
//  @Override
//  public void call(String methodName, StringResult resultCallback,
//      Object... functionArgs) {
//    callWithResult(methodName, resultCallback, functionArgs);
//  }
//  
//  @Override
//  public void call(String methodName, JsResult<?> resultCallback,
//      Object... functionArgs) {
//    callWithResult(methodName, resultCallback, functionArgs);
//  }
  
  @Override
  public void call(String methodName, JsReturn<?> resultCallback,
      Object... functionArgs) {
//    callWithResult(methodName, resultCallback, functionArgs);
    if (registeredFunctions != null) {
      int len = registeredFunctions.length();
      for (int i = 0; i < len; i++) {
        WidgetProxyFunction wpf = registeredFunctions.get(i);
        if (wpf.getName().equals(methodName)) {
          wpf.call(resultCallback, functionArgs);
          break;
        }
      }
    }
    // Ignore if not there? -- throw new Error("Attempt to call a non-existent widget proxy function");
  }
  
//  private void callWithResult(String methodName, JsReturn resultCallback,
//      Object[] functionArgs) {
//    if (registeredFunctions != null) {
//      int len = registeredFunctions.length();
//      for (int i = 0; i < len; i++) {
//        WidgetProxyFunction wpf = registeredFunctions.get(i);
//        if (wpf.getName().equals(methodName)) {
//          wpf.call(resultCallback, functionArgs);
//          break;
//        }
//      }
//    }
//    // Ignore if not there? -- throw new Error("Attempt to call a non-existent widget proxy function");
//  }
  
  @Override
  public void call(String methodName, Object... functionArgs) {
    if (registeredFunctions != null) {
      int len = registeredFunctions.length();
      for (int i = 0; i < len; i++) {
        WidgetProxyFunction wpf = registeredFunctions.get(i);
        if (wpf.getName().equals(methodName)) {
          wpf.call(functionArgs);
        }
      }
    }
//ignore if not there? --    throw new Error("Attempt to call a non-existent widget proxy function");
  }
  
  @Override
  public void publish(String channelName, String message, WidgetProxy dest) {
    StringMessage stringMessage = new StringMessage(this, message);
    List<Subscription> subscriptions = channels.get(channelName);
    if (subscriptions != null) {
      for (Subscription subscription : subscriptions) {
        subscription.getMessageHandler().callback(stringMessage);
      }
    }
  }

  @Override
  public void subscribe(String channelName, EventListener<StringMessage> handler) {
    List<Subscription> subscriptions = channels.get(channelName);
    if (subscriptions == null) {
      subscriptions = new ArrayList<Subscription>();
      channels.put(channelName, subscriptions);
    }
    subscriptions.add(new Subscription(this, handler));
  }

  @Override
  public void unsubscribe(String channelName) {
    List<Subscription> subscriptions = channels.get(channelName);
    if (subscriptions != null) {
      Iterator<Subscription> iterator = subscriptions.iterator();
      while (iterator.hasNext()) {
        Subscription subscription = iterator.next();
        if (subscription.getWidgetHandle() == this) {
          iterator.remove();
        }
      }
    }
  }
  
  private class Subscription {
    
    private DirectWidgetHandle widgetHandle;
    private EventListener<StringMessage> messageHandler;

    Subscription(DirectWidgetHandle widgetHandle, EventListener<StringMessage> messageHandler) {
      this.widgetHandle = widgetHandle;
      this.messageHandler = messageHandler;
    }
    
    DirectWidgetHandle getWidgetHandle() {
      return widgetHandle;
    }
    
    EventListener<StringMessage> getMessageHandler() {
      return messageHandler;
    }
  }
  
//  public void startIntent(Intent intent, JavaScriptObject data, final OnReceipt onReceipt) {
//    if (onReceipt == null) {
//      nativeStartIntent(action, dataType, data, null);
//    } else {
//      EventListener<JsArray<OWFWidgetProxy>> nativeReceipt = new EventListener<JsArray<OWFWidgetProxy>>() {
//        @Override
//        public void callback(JsArray<OWFWidgetProxy> dests) {
//          // These should already be "ready" proxies
//          int len = dests.length();
//          for (int i = 0; i < len; i++) {
//            onReceipt.intentReceived(dests.get(i));
//          }
//        }
//      };
//      nativeStartIntent(action, dataType, data, JsFunction.create(nativeReceipt));
//    }
//  }
//  
//  private native void nativeStartIntent(String action, String dataType,
//      JavaScriptObject data, JsFunction onReceipt) /*-{
//    $wnd.OWF.Intents.startIntent(
//      {
//        action : action, 
//        dataType : dataType
//      },
//      {
//        data : data
//      },
//      onReceipt
//// , not passing in desination widget proxies, which is an OWF option
//    );
//  }-*/;
//  
//  public void receive(final Intent intent) {
//    nativeReceive(
//      intent.getAction(),
//      intent.getDataType(),
//      JsFunction.create(new EventListener<IntentReceived>() {
//        @Override
//        public void callback(IntentReceived event) {
//          intent.intentReceived(event.getSender(), event.getData());
//        }
//      })
//    );
//  }
//  
//  private native void nativeReceive(String action, String dataType, JsFunction listener) /*-{
//    $wnd.OWF.Intents.receive(
//      {
//        action : action,
//        dataType : dataType
//      },
//      function(sender, intent, data) {
//        var senderWidgetProxy = $wnd.OWF.RPC.getWidgetProxy(sender);
//        listener({
//          senderWidgetProxy : senderWidgetProxy,
//          intent : intent,
//          data : data.data
//        })
//      }
//    );
//  }-*/;
  
  private class IntentHandler {

    private Intent<?> intent;

    IntentHandler(Intent<?> intent) {
      this.intent = intent;
    }

    public WidgetProxy getRecipient() {
      return DirectWidgetHandle.this;
    }

    public Intent<?> getIntent() {
      return intent;
    }
  }
  
  @Override
  public void startActivity(Intent<?> intent, JavaScriptObject data, OnReceipt onReceipt) {
    IntentHandler intentHandler = intentHandlers.get(intent);
    if (intentHandler != null) {
      intentHandler.getIntent().coercedIntentReceived(this, data);
      if (onReceipt != null) {
        onReceipt.intentReceived(intentHandler.getRecipient());
      }
    }
  }
  
  @Override
  public void receive(Intent<?> intent) {
    intentHandlers.put(intent, new IntentHandler(intent));
  }
}
