package ozone.gwt.widget;


public final class StringMessage {

  private WidgetProxy sender;
  private String message;
  
  public StringMessage(WidgetProxy sender, String message) {
    this.sender = sender;
    this.message = message;
  }
  
  public WidgetProxy getSender() {
    return sender;
  }
  
  public String getMessage() { 
    return message;
  }
}
