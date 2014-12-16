package ozone.gwt.owfgwttest.proxies;

import jsfunction.gwt.returns.JsReturn;
import ozone.gwt.owfgwttest.proxies.data.DocumentTreeSettings;
import ozone.gwt.widget.WidgetProxy;

public class DocumentTreeProxy {
  
  public static final String GET_SETTINGS = "getSettings";
  public static final String ADD_FILES = "addFiles";
  
  private WidgetProxy widgetProxy;

  public DocumentTreeProxy(WidgetProxy widgetProxy) {
    this.widgetProxy = widgetProxy;
  }
  
  public WidgetProxy getWidgetProxy() {
    return this.widgetProxy;
  }

  public void getSettings(JsReturn<DocumentTreeSettings> callback) {
    widgetProxy.call(GET_SETTINGS, callback);
  }

  public void addFiles(int numFiles) {
    widgetProxy.call(ADD_FILES, numFiles);
  }
}
