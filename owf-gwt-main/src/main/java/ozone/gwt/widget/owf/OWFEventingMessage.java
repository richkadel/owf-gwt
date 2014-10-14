package ozone.gwt.widget.owf;

import com.google.gwt.core.client.JavaScriptObject;

public final class OWFEventingMessage extends JavaScriptObject {

  protected OWFEventingMessage() {}
  
  public static native OWFEventingMessage create(String sender, String msg, String channel) /*-{
    return {
      sender : sender,
      msg : msg,
      channel : channel
    }
  }-*/;
  
  public native String getSenderWidgetId() /*-{ return this.sender }-*/;
  public native String getMessage() /*-{ return this.msg }-*/;
  public native String getChannel() /*-{ return this.channel }-*/;
}
