package ozone.gwt.owfgwttest.channels.data;

import com.google.gwt.core.client.JavaScriptObject;

public final class DocumentTreeInitializationData extends JavaScriptObject {
  
  protected DocumentTreeInitializationData() {}
  
  public static native DocumentTreeInitializationData create(
      String statePrefix, 
      String publisherMessage
      ) /*-{
    return {
      statePrefix : statePrefix,
      publisherMessage : publisherMessage
    }
  }-*/;
  
  public native String getStatePrefix() /*-{
    return this.statePrefix;
  }-*/;
  
  public native String getPublisherMessage() /*-{
    return this.publisherMessage;
  }-*/;
}
