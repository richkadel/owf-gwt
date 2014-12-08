package ozone.gwt.widget.owf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import ozone.gwt.widget.DirectLogger;
import ozone.gwt.widget.EventingChannel;
import ozone.gwt.widget.Intent;
import ozone.gwt.widget.OnReceipt;
import ozone.gwt.widget.StringMessage;
import ozone.gwt.widget.WidgetContainer;
import ozone.gwt.widget.WidgetDescriptor;
import ozone.gwt.widget.WidgetFramework;
import ozone.gwt.widget.WidgetHandle;
import ozone.gwt.widget.WidgetLogger;
import ozone.gwt.widget.WidgetProxy;
import ozone.gwt.widget.WidgetProxyFunction;
import ozone.gwt.widget.WidgetProxyFunctions;
import ozone.gwt.widget.direct.descriptorutil.SaveableText;
import jsfunction.gwt.JsFunction;
import jsfunction.gwt.JsFunctionUtils;
import jsfunction.gwt.functions.EventListener;
import jsfunction.gwt.functions.NoArgsFunction;
import jsfunction.gwt.functions.VarArgsFunction;
import jsfunction.gwt.functions.VarArgsFunctionReturn;
import jsfunction.gwt.returns.JsReturn;
import jsfunction.gwt.types.JsError;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayMixed;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.IsWidget;

public class OZONEWidgetFramework extends WidgetFramework implements WidgetHandle { 
  
  private static final Logger log = Logger.getLogger(OZONEWidgetFramework.class.getName());

  private static final String INTENT_FROM_MAP = "owfGwtIntentFromMap";
  
  private static boolean isReady = false;
  
  private IsWidget gwtIsWidget;

  private OWFWidgetState widgetState;
  
  private IntentsMap intentsMap;
  
  private WidgetLogger owfLogger = new DirectLogger();;
  
  private String universalName;

  private List<Intent<?>> intentsReceived = new ArrayList<Intent<?>>();

  private Map<String,String> universalNameToWidgetId = new HashMap<String,String>();
  
  private Map<String,OWFWidgetProxy> universalNameToWidgetProxy = new HashMap<String,OWFWidgetProxy>();
  
  private Map<String,Intent<?>> intentsMapKeyToIntent = new HashMap<String,Intent<?>>();
  
  private List<Intent<?>> intentsAdded = new LinkedList<Intent<?>>();
  private List<EventingChannel> channelsAdded = new LinkedList<EventingChannel>();

  private boolean autoNotifyWidgetReady = true;

  private boolean notifiedWidgetReady;

  public static native boolean isRunningInOWF() /*-{
    if (window.parent.name.indexOf("preferenceLocation\":\"") > 0) {
      return true;
    } else {
      return false;
    }
  }-*/;
  
  protected OZONEWidgetFramework() {
    onReady(new NoArgsFunction() {
      public void callback() {
        if (isLogEnabled()) {
          owfLogger = OWFLogger.create();
        }
        widgetState = getWidgetState();
        intentsMap = getIntentsMap(OWF_GWT_INTENTS_MAP_GLOBALVAR);
        // create default widget proxy function
        registerWidgetProxyFunctions(new WidgetProxyFunctions(), false);
      }
    });
  }
  
  private void onReady(NoArgsFunction callback) {
    nativeOnReady(JsFunction.create(callback));
  }

  private native void nativeOnReady(JsFunction callback) /*-{
    $wnd.OWF.ready(function() {
      callback();
    }, this);
  }-*/;

  private native IntentsMap getIntentsMap(String globalVarName) /*-{
    return $wnd[globalVarName];
  }-*/;

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
    }, this);
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
    universalName = WidgetDescriptor.makeUniversalName(this);
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
  
  private native boolean isLogEnabled() /*-{
    return $wnd.Ozone.log.logEnabled;
  }-*/;
  
  @Override
  public void activate() {
    widgetState.activateWidget();
  }

  @Override
  public void notifyWidgetReady() {
    if (!notifiedWidgetReady) {
      notifiedWidgetReady = true;
      nativeNotifyWidgetReady();
    }
  }

  public native void nativeNotifyWidgetReady() /*-{
    $wnd.OWF.ready(function() {
      $wnd.OWF.notifyWidgetReady();
    }, this);
  }-*/;

  @Override
  public IsWidget getGWTIsWidget() {
    return gwtIsWidget;
  }

  @Override
  public void handleDirectMessage(
      EventListener<JavaScriptObject> directMessageCallback) {
    ensureNotifyWidgetReadyCheck();
    handleDirectMessage(JsFunction.create(directMessageCallback));
    
// This method supports the equivalent of the following JavaScript example:
//
//  OWF.RPC.handleDirectMessage(function(msg) {
//    // do something with the message
//  });
  }

  private native void handleDirectMessage(JsFunction directMessageCallbackFunction) /*-{
    $wnd.OWF.ready(function() {
      $wnd.OWF.RPC.handleDirectMessage(directMessageCallbackFunction);
    }, this);
  }-*/;

  @Override
  public void registerWidgetProxyFunctions(WidgetProxyFunctions widgetProxyFunctions) {
    registerWidgetProxyFunctions(widgetProxyFunctions, true); // this should queue up notifyWidgetReady
  }

  public void registerWidgetProxyFunctions(WidgetProxyFunctions widgetProxyFunctions, boolean fromApp) {
    if (fromApp) {
      ensureNotifyWidgetReadyCheck();
    }
    widgetProxyFunctions.add(INTENT_FROM_MAP, new VarArgsFunctionReturn<JavaScriptObject>() {
      public JavaScriptObject callback(JsArrayMixed args) {
        String senderWidgetId = args.getString(0);
        String intentsMapKey = args.getString(1);
        final JavaScriptObject data = args.getObject(2).cast();
        final Intent<?> intent = intentsMapKeyToIntent.get(intentsMapKey);
        if (intent == null) {
          return null; // null is our marker that the requested intent is not registered to be received by this widget
        } else {
          OWFWidgetProxy.getWidgetProxy(senderWidgetId, new EventListener<OWFWidgetProxy>() {
            public void callback(OWFWidgetProxy senderWidgetProxy) {
              intent.coercedIntentReceived(senderWidgetProxy, data);
            }
          });
          return JavaScriptObject.createObject(); // non-null means the intent for the given intentsMapKey was received
        }
      }
    });
    this.nativeRegisterWidgetProxyFunctions(widgetProxyFunctions.toJsArray());
  }
  
  private native void nativeRegisterWidgetProxyFunctions(JsArray<WidgetProxyFunction> widgetProxyFunctions) /*-{
    
    $wnd.OWF.ready(function() {
      $wnd.OWF.RPC.registerFunctions(widgetProxyFunctions);
    }, this);
    
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
  
  private native void publish(String channelName, String message, String destWidgetId) /*-{
    // This assumes destWidgetId is READY.  Safest to use the public method above with a valid WidgetProxy
    $wnd.OWF.ready(function() {
      if (destWidgetId) {
        $wnd.OWF.Eventing.publish(channelName, message, destWidgetId);
      } else {
        $wnd.OWF.Eventing.publish(channelName, message);
      }
    }, this);
  }-*/;

  @Override
  public void subscribe(String channelName, final EventListener<StringMessage> handler) {
    ensureNotifyWidgetReadyCheck();
    subscribe(
      channelName, 
      JsFunction.create(new VarArgsFunction() {
        public void callback(final JsArrayMixed args) {
          String senderWidgetId = args.getString(0);
          OWFWidgetProxy.getWidgetProxy(senderWidgetId, new EventListener<OWFWidgetProxy>() {
            public void callback(final OWFWidgetProxy sender) {
              String message = args.getString(1);
              handler.callback(new StringMessage(sender, message)); // ignore third parameter, channel name
            }
          });
        }
      })
    );
  }

  private native void subscribe(String channelName, JsFunction handler) /*-{
    $wnd.OWF.ready(function() {
      $wnd.OWF.Eventing.subscribe(channelName, handler);
    }, this);
  }-*/;

  @Override
  public native void unsubscribe(String channelName) /*-{
    $wnd.OWF.ready(function() {
      $wnd.OWF.Eventing.unsubscribe(channelName);
    }, this);
  }-*/;
  
  @Override
  public void startActivity(final Intent<?> intent, final JavaScriptObject data, final OnReceipt onReceipt) {
    startActivity(intent, data, onReceipt, false);
  }
  
  public void startActivity(final Intent<?> intent, final JavaScriptObject data, final OnReceipt onReceipt, final boolean secondAttempt) {
    
    final String intentsMapKey = SaveableText.stringify(intent.getIntentDescriptor());
    final String directRecipient = getDirectRecipientName(intentsMapKey);
    
    if (directRecipient != null) {
      getWidgetProxyFromUniversalName(directRecipient, new EventListener<OWFWidgetProxy>() {
        public void callback(final OWFWidgetProxy widgetProxy) {
          if (widgetProxy == null) {
            if (onReceipt != null) {
              onReceipt.intentNotReceived();
            }
          } else {
            String universalId = universalNameToWidgetId.get(directRecipient);
            launchWidget(universalId, new EventListener<OWFLaunchResponse>() {
              public void callback(OWFLaunchResponse response) {
                if (response.isError()) {
                  log.warning("Error launching widget "+directRecipient+" for Intent "+intentsMapKey);
                } else {
                  if (response.getNewWidgetLaunched()) {
                    universalNameToWidgetProxy.remove(directRecipient);
                    // Need to re-get the proxy and try again
                    startActivity(intent, data, onReceipt, true);
                  } else {
                    widgetProxy.call(
                      INTENT_FROM_MAP, 
                      new JsReturn<JavaScriptObject>() {
                        public void onReturn(JavaScriptObject returnValue) {
                          if (onReceipt != null) {
                            if (returnValue == null) { // null is our marker that the intentsMapKey did not match a received intent
                              onReceipt.intentNotReceived();
                            } else {
                              onReceipt.intentReceived(widgetProxy);
                            }
                          }
                        }
        
                        public void onError(JsError jsError) {
                          universalNameToWidgetProxy.remove(directRecipient);
                          if (secondAttempt) {
                            onReceipt.intentNotReceived();
                          } else {
                            // try again but we'll need to re-get the proxy
                            startActivity(intent, data, onReceipt, true);
                          }
                        }
                      },
                      getWidgetState().getWidgetGuid(),
                      intentsMapKey,
                      data
                    );
                  }
                }
              }
            });
          }
        }
      });
    } else {
      JsFunction receiptFunction = null;
      if (onReceipt != null) {
        receiptFunction = JsFunction.create(
          new EventListener<JsArray<OWFWidgetProxy>>() {
            @Override
            public void callback(JsArray<OWFWidgetProxy> dests) {
              int len = dests.length();
              if (len == 0) {
                onReceipt.intentNotReceived();
              } else {
                for (int i = 0; i < len; i++) {
                  // These should already be "ready" proxies
                  if (!OWFWidgetProxy.isReady(dests.get(i))) {
                    throw new Error("Given WidgetProxy is not ready! It should be. Wrap in OWFWidgetProxy.getWidgetProxy() callback method before returning.");
                  }
                  onReceipt.intentReceived(dests.get(i));
                }
              }
            }
          }
        );
      }
      nativeStartActivity(intent.getAction(), intent.getDataType(), data, receiptFunction);
    }
  }
  
  public void launchWidget(String widgetId, EventListener<OWFLaunchResponse> responseListener) {
    launchWidget(widgetId, null, responseListener);
  }
  
  public void launchWidget(String widgetId, String data, EventListener<OWFLaunchResponse> responseListener) {
    JsFunction responseListenerFunc = null;
    if (responseListener != null) {
      responseListenerFunc = JsFunction.create(responseListener);
    }
    nativeLaunchWidget(widgetId, data, responseListenerFunc);
  }

  private native void nativeLaunchWidget(String widgetId, String data, JsFunction responseListener) /*-{
    $wnd.OWF.Launcher.launch(
      {
        guid: widgetId,
        launchOnlyIfClosed: true,
        data: data
      },
      function(response) { // Object {error: false, newWidgetLaunched: false, message: "An instance of the specified widget already exists.", uniqueId: "6aa6a53c-5d78-e08a-7459-0354af458d12"}
        // The documentation doesn't say this function is optional, so I will implement it
        if (responseListener) {
          responseListener(response);
        }
      }
    );
  }-*/;

  /**
   * @param universalName
   * @param returnWidgetId may return null if not found/registered
   */
  private void getWidgetIdFromUniversalName(final String universalName,
      final EventListener<String> returnWidgetId) {
    String widgetId = universalNameToWidgetId.get(universalName);
    if (widgetId != null) {
      returnWidgetId.callback(widgetId);
    } else {
      nativeUniversalNameToWidgetId(universalName, 
        JsFunction.create(new EventListener<String>() {
          public void callback(String widgetId) {
            universalNameToWidgetId.put(universalName, widgetId);
            returnWidgetId.callback(widgetId);
          }
        })
      );
    }
  }

  /**
   * @param universalName
   * @param returnWidgetProxy may return null if not found/registered
   */
  private void getWidgetProxyFromUniversalName(final String universalName,
      final EventListener<OWFWidgetProxy> returnWidgetProxy) {
    getWidgetProxyFromUniversalName(universalName, returnWidgetProxy, false);
  }

  /**
   * @param universalName
   * @param returnWidgetProxy may return null if not found/registered
   */
  private void getWidgetProxyFromUniversalName(final String universalName,
      final EventListener<OWFWidgetProxy> returnWidgetProxy, final boolean secondAttempt) {
    
    OWFWidgetProxy widgetProxy = universalNameToWidgetProxy.get(universalName);
    if (widgetProxy != null) { // && !widgetProxy.isClosed()) { // OWF state listener bug... I can't tell if closed or not
      returnWidgetProxy.callback(widgetProxy);
    } else {
      getWidgetIdFromUniversalName(universalName, new EventListener<String>() {
        public void callback(final String widgetId) {
          if (widgetId == null) {
            returnWidgetProxy.callback(null);
          } else {
            launchWidget(widgetId, new EventListener<OWFLaunchResponse>() {
              public void callback(OWFLaunchResponse response) {
                if (response.isError()) {
                  universalNameToWidgetId.remove(universalName);
                  if (secondAttempt) {
                    returnWidgetProxy.callback(null);
                  } else {
                    // try again but we'll need to re-get the widgetId
                    getWidgetProxyFromUniversalName(universalName, returnWidgetProxy, true);
                  }
                } else {
                  OWFWidgetProxy.getWidgetProxy(response.getUniqueWidgetId(), new EventListener<OWFWidgetProxy>() {
                    public void callback(OWFWidgetProxy widgetProxy) {
                      universalNameToWidgetProxy.put(universalName, widgetProxy);
                      returnWidgetProxy.callback(widgetProxy);
                    }
                  });
                }
              }
            });
          }
        }
      });
    }
  }

  private native void nativeUniversalNameToWidgetId(String universalName, JsFunction returnWidgetId) /*-{
    $wnd.OWF.Preferences.getWidget({
      universalName: universalName,
      onSuccess: function(result) {
        returnWidgetId(result.path); // guid is not there, but path and id are
      },
      onFailure: function(err) { 
        returnWidgetId(null);
      }
    });
  }-*/;

  private native void nativeStartActivity(String action, String dataType,
      JavaScriptObject data, JsFunction onReceipt) /*-{
    $wnd.OWF.ready(function() {
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
    }, this);
  }-*/;
  
  private String getDirectRecipientName(String stringifiedIntent) {
    if (intentsMap != null) {
      return intentsMap.getWidgetUniversalName(stringifiedIntent);
    }
    return null;
  }
  
  private boolean universalNameIsMe(String directRecipient) {
    if (this.universalName.equals(directRecipient)) {
      return true;
    } else {
      int firstDot = directRecipient.indexOf('.');
      if (firstDot >= 0) {
        directRecipient = directRecipient.substring(firstDot+1);
      }
      if (this.universalName.equals(directRecipient)) {
        return true;
      }
    }
    return false;
  }
  
  @Override
  public void receive(final Intent<?> intent) {
    ensureNotifyWidgetReadyCheck();
    
    String intentsMapKey = SaveableText.stringify(intent.getIntentDescriptor());
    String directRecipient = getDirectRecipientName(intentsMapKey);
    
    if (directRecipient != null && universalNameIsMe(directRecipient)) {
      intentsMapKeyToIntent.put(intentsMapKey, intent);
    } else {
      intentsReceived.add(intent);
      nativeReceive(
        intent.getAction(),
        intent.getDataType(),
        JsFunction.create(new EventListener<IntentReceived>() {
          @Override
          public void callback(IntentReceived event) {
            // sender should already be a "ready" proxy
            if (!OWFWidgetProxy.isReady(event.getSender())) {
              throw new Error("Given WidgetProxy is not ready! It should be. Wrap in OWFWidgetProxy.getWidgetProxy() callback method before returning.");
            }
            intent.coercedIntentReceived(event.getSender(), event.getData());
          }
        })
      );
    }
  }
  
  private native void nativeReceive(String action, String dataType, JsFunction listener) /*-{
    $wnd.OWF.ready(function() {
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
    }, this);
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

  /**
   * Defaults to true.
   * This must be called right after OZONEWidgetFramework construction, or in other words, 
   * after the first call to WidgetFramework.getInstance(). 
   * If false, and IntentsMap is available, force it off (and default to normal Widget Intents
   * behavior).
   * If this method is not called, the intents map can only be used if provided in
   * the widget descriptor parameters.
   * @param useIntentsMap
   */
  public void setUseIntentsMap(boolean useIntentsMap) {
    if (!useIntentsMap) {
      this.intentsMap = null;
    }
  }
}
