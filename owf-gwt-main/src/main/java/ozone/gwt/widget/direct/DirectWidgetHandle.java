package ozone.gwt.widget.direct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ozone.gwt.widget.EventingChannel;
import ozone.gwt.widget.Intent;
import ozone.gwt.widget.OnReceipt;
import ozone.gwt.widget.StringMessage;
import ozone.gwt.widget.WidgetContainer;
import ozone.gwt.widget.WidgetHandle;
import ozone.gwt.widget.WidgetProxy;
import ozone.gwt.widget.WidgetProxyFunction;
import ozone.gwt.widget.WidgetProxyFunctions;
import ozone.gwt.widget.direct.descriptorutil.SaveableText;
import jsfunction.gwt.functions.EventListener;
import jsfunction.gwt.functions.NoArgsFunction;
import jsfunction.gwt.returns.JsReturn;
import jsfunction.gwt.types.JsError;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.IsWidget;

public class DirectWidgetHandle implements WidgetHandle, WidgetProxy {

  private static final Map<Intent<?>, IntentHandler> intentHandlers = new HashMap<Intent<?>, IntentHandler>();
  private static final Map<Intent<?>, PendingIntent> pendingIntents = new HashMap<Intent<?>, PendingIntent>();

  private static Map<String, List<Subscription>> channels = new HashMap<String, List<Subscription>>();

  private List<Intent<?>> intentsReceived = new ArrayList<Intent<?>>();

  private IsWidget gwtIsWidget;
  private WidgetContainer widgetContainer;

  private EventListener<JavaScriptObject> directMessageCallback;

  private JsArray<WidgetProxyFunction> registeredFunctions;
  
  private List<Intent<?>> intentsAdded = new LinkedList<Intent<?>>();
  private List<EventingChannel> channelsAdded = new LinkedList<EventingChannel>();
  private boolean autoNotifyWidgetReady;
  private boolean notifiedWidgetReady;
  
  private List<EventListener<WidgetProxy>> readyCallbacks;

  public DirectWidgetHandle(WidgetContainer widgetContainer,
      IsWidget gwtIsWidget) {
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
    if (!notifiedWidgetReady) {
      notifiedWidgetReady = true;
      if (readyCallbacks != null) {
        for (EventListener<WidgetProxy> readyCallback : readyCallbacks) {
          readyCallback.callback(this);
        }
      }
      readyCallbacks = null;
    }
  }

  private void addReadyCallback(EventListener<WidgetProxy> callback) {
    if (notifiedWidgetReady) {
      callback.callback(this);
    } else {
      if (readyCallbacks == null) {
        readyCallbacks = new ArrayList<EventListener<WidgetProxy>>();
      }
      readyCallbacks.add(callback);
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
    ensureNotifyWidgetReadyCheck();
    if (this.directMessageCallback != null) {
      throw new Error(
          "Was not expecting handleDirectMessage to be called more than once for the same widget");
    }
    this.directMessageCallback = directMessageCallback;
  }

  @Override
  public void registerWidgetProxyFunctions(
      WidgetProxyFunctions widgetProxyFunctions) {
    ensureNotifyWidgetReadyCheck();
    if (this.registeredFunctions != null) {
      throw new Error(
          "Was not expecting registerWidgetProxyFunctions to be called more than once for the same widget");
    }
    this.registeredFunctions = widgetProxyFunctions.toJsArray();
  }

  @Override
  public void sendMessage(final JavaScriptObject message) {
    if (!notifiedWidgetReady) {
      addReadyCallback(new EventListener<WidgetProxy>() {
        public void callback(WidgetProxy event) {
          sendMessage(message);
        }
      });
    } else {
      if (directMessageCallback != null) {
        directMessageCallback.callback(message);
      }
    }
  }

  @Override
  public void call(final String methodName, final JsReturn<?> resultCallback,
      final Object... functionArgs) {
    if (!notifiedWidgetReady) {
      addReadyCallback(new EventListener<WidgetProxy>() {
        public void callback(WidgetProxy event) {
          call(methodName, resultCallback, functionArgs);
        }
      });
    } else {
      if (registeredFunctions != null) {
        int len = registeredFunctions.length();
        for (int i = 0; i < len; i++) {
          WidgetProxyFunction wpf = registeredFunctions.get(i);
          if (wpf.getName().equals(methodName)) {
            wpf.call(resultCallback, functionArgs);
            return;
          }
        }
      }
      resultCallback.onError(JsError.create(new Error(
          "Attempt to call a non-existent widget proxy function: "
              + gwtIsWidget.getClass() + ":" + methodName)));
    }
  }

  @Override
  public void call(final String methodName, final Object... functionArgs) {
    if (!notifiedWidgetReady) {
      addReadyCallback(new EventListener<WidgetProxy>() {
        public void callback(WidgetProxy event) {
          call(methodName, functionArgs);
        }
      });
    } else {
      if (registeredFunctions != null) {
        int len = registeredFunctions.length();
        for (int i = 0; i < len; i++) {
          WidgetProxyFunction wpf = registeredFunctions.get(i);
          if (wpf.getName().equals(methodName)) {
            wpf.call(functionArgs);
            return;
          }
        }
        DirectWidgetFramework
            .getInstance()
            .getLogger()
            .error(
                "Attempt to call a non-existent widget proxy function: "
                    + gwtIsWidget.getClass() + ":" + methodName);
      }
      // This is "fire and forget", so I'm opting to...
      // ignore if not there rather than throw new
      // Error("Attempt to call a non-existent widget proxy function");
      // If you want to catch the Error, you can call the method with a
      // "JsReturnVoid" instead.
    }
  }

  @Override
  public boolean isSameWidget(WidgetProxy rhs) {
    return this == rhs;
  }

  @Override
  public void publish(String channelName, JavaScriptObject message, WidgetProxy dest) {
    publish (channelName, SaveableText.stringify(message), dest);
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
    ensureNotifyWidgetReadyCheck();
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

    Subscription(DirectWidgetHandle widgetHandle,
        EventListener<StringMessage> messageHandler) {
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

  private class PendingIntent {

    private JavaScriptObject data;
    private OnReceipt onReceipt;

    PendingIntent(JavaScriptObject data, OnReceipt onReceipt) {
      this.data = data;
      this.onReceipt = onReceipt;
    }

    public JavaScriptObject getData() {
      return data;
    }

    public OnReceipt getOnReceipt() {
      return onReceipt;
    }
  }

  @Override
  public void startActivity(Intent<?> intent, JavaScriptObject data,
      OnReceipt onReceipt) {
    intent.setAutoReceive(false); // This is a sent intent, not a received intent
    IntentHandler intentHandler = intentHandlers.get(intent);
    if (intentHandler != null) {
      sendIntent(intentHandler.getRecipient(), intentHandler.getIntent(), data,
          onReceipt);
    } else {
      pendingIntents.put(intent, new PendingIntent(data, onReceipt));
    }
  }

  /**
   * @param data
   * @param onReceipt
   * @param intentHandler
   */
  private void sendIntent(WidgetProxy recipient, Intent<?> intent,
      JavaScriptObject data, OnReceipt onReceipt) {
    intent.coercedIntentReceived(this, data);
    if (onReceipt != null) {
      onReceipt.intentReceived(recipient);
    }
  }

  @Override
  public void receive(Intent<?> intent) {
    ensureNotifyWidgetReadyCheck();
    intentsReceived.add(intent);
    intentHandlers.put(intent, new IntentHandler(intent));
    PendingIntent pendingIntent = pendingIntents.remove(intent);
    if (pendingIntent != null) {
      sendIntent(this, intent, pendingIntent.getData(),
          pendingIntent.getOnReceipt());
    }
  }

  @Override
  public boolean hasWidgetHeader() {
    return false;
  }

  @Override
  public void addWidgetHeaderButton(String buttonType, NoArgsFunction callback)
      throws NoSuchMethodException {
    throw new NoSuchMethodException();
  }

  @Override
  public Collection<Intent<?>> getIntentsReceived() {
    return intentsReceived;
  }

  @Override
  public void addIntent(Intent<?> intent) {
    intentsAdded.add(intent);
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      public void execute() {
        while (intentsAdded.size() > 0) {
          Intent<?> intent = intentsAdded.remove(0);
          if (intent.isAutoReceive()) {
            intent.receive();
          }
        }
      }
    });
  }

  @Override
  public void addEventingChannel(EventingChannel channel) {
    channelsAdded.add(channel);
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      public void execute() {
        while (channelsAdded.size() > 0) {
          EventingChannel channel = channelsAdded.remove(0);
          if (channel.isAutoSubscribe()) {
            channel.subscribe();
          }
        }
      }
    });
  }

  public void ensureNotifyWidgetReadyCheck() {
    if (!notifiedWidgetReady) {
      Scheduler.get().scheduleDeferred(new ScheduledCommand() {
        public void execute() {
          if (autoNotifyWidgetReady) {
            notifyWidgetReady();
          }
        }
      });
    }
  }
  
  /**
   * Defaults to true. Call notifyWidgetReady() automatically, after at least one Intent,
   * WidgetProxyFunction, or EventingChannel is registered, deferred until the next JavaScript event loop.
   */
  public void setAutoNotifyWidgetReady(boolean autoNotifyWidgetReady) {
    this.autoNotifyWidgetReady = false;
  }
  
  public boolean isAutoNotifyWidgetReady() {
    return autoNotifyWidgetReady;
  }
}
