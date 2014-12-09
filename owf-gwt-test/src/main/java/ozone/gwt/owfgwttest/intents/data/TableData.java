package ozone.gwt.owfgwttest.intents.data;

import com.google.gwt.core.client.JavaScriptObject;

public final class TableData extends JavaScriptObject {
  
  protected TableData() {}
  
  public static native TableData create(
      String statePrefix, 
      String jsMetaTable
      ) /*-{
    return {
      statePrefix : statePrefix,
      jsMetaTable : jsMetaTable
    }
  }-*/;
  
  public native String getStatePrefix() /*-{
    return this.statePrefix;
  }-*/;
  
  public native String getJsMetaTable() /*-{
    return this.jsMetaTable;
  }-*/;
}
