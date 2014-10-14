package ozone.gwt.widget.owf;

import com.google.gwt.core.client.JavaScriptObject;

public final class OWFWidgetData extends JavaScriptObject {

  protected OWFWidgetData() {}
  
  public native String getId() /*-{ // the widget id (instance id?)
    return this.id;
  }-*/;
  
  public native String getContainerVersion() /*-{ // e.g., "7.0.1-GA"
    return this.containerVersion;
  }-*/;
  
  public native String getWebContextPath() /*-{ // e.g., "/owf"
    return this.webContextPath;
  }-*/;
  
  public native String getPreferenceLocation() /*-{ // e.g., "https://localhost:8443/owf/prefs"
    return this.preferenceLocation;
  }-*/;
  
  public native String getRelayUrl() /*-{ // e.g., "https://localhost:8443/owf/js/eventing/rpc_relay.uncompressed.html"
    return this.relayUrl;
  }-*/;
  
  public native String getLang() /*-{ // e.g., "en_US"
    return this.lang;
  }-*/;
  
  public native String getCurrentTheme() /*-{ // e.g., a JSON string with themeContract string, themeName, etc.
    return this.currentTheme;
  }-*/;
  
  public native boolean getOwf() /*-{ // e.g., true
    return this.owf;
  }-*/;
  
  public native String getLayout() /*-{ // e.g., "desktop"
    return this.layout;
  }-*/;
  
  public native String getUrl() /*-{ // the widget URL, e.g., "http://localhost:8888/treeview.html"
    return this.url;
  }-*/;
  
  public native String getGuid() /*-{ // the widget GUID
    return this.guid;
  }-*/;
  
  public native int getVersion() /*-{ // the widget version?, e.g., 1
    return this.version;
  }-*/;
  
  public native boolean getLocked() /*-{ // e.g., false
    return this.locked;
  }-*/;
}
