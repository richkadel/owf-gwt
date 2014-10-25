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
import jsfunction.gwt.functions.EventListener;
import jsfunction.gwt.returns.JsReturn;
import jsfunction.gwt.types.JsError;

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
  
  @Override
  public void call(String methodName, JsReturn<?> resultCallback,
      Object... functionArgs) {
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
    resultCallback.onError(JsError.create(new Error("Attempt to call a non-existent widget proxy function")));
  }
  
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
    // This is "fire and forget", so I'm opting to...
    // ignore if not there rather than throw new Error("Attempt to call a non-existent widget proxy function");
    // If you want to catch the Error, you can call the method with a "JsReturnVoid" instead.
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
