package ozone.gwt.owfgwttest.channels.data;

import com.google.gwt.core.client.JavaScriptObject;

public final class TableItem extends JavaScriptObject {
  
  protected TableItem() {}
  
  public static native TableItem create(
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
