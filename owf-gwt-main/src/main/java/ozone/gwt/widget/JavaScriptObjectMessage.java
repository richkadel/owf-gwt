package ozone.gwt.widget;

import com.google.gwt.core.client.JavaScriptObject;

public class JavaScriptObjectMessage {

  private WidgetProxy sender;
  private JavaScriptObject message;

  JavaScriptObjectMessage(WidgetProxy sender, JavaScriptObject message) {
    this.sender = sender;
    this.message = message;
  }

  /**
   * @return the sender
   */
  public WidgetProxy getSender() {
    return sender;
  }

  /**
   * @return the message
   */
  public JavaScriptObject getMessage() {
    return message;
  }
}
