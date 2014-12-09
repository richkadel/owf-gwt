package ozone.gwt.owfgwttest.proxies;

import jsfunction.gwt.returns.JsReturn;
import ozone.gwt.owfgwttest.proxies.data.DocumentTreeSettings;
import ozone.gwt.widget.WidgetProxy;

public class DocumentTreeProxy {
  
  public static final String GET_SETTINGS = "getSettings";
  public static final String SET_SATURATION = "setSaturation";
  public static final String SET_BRIGHTNESS = "setBrightness";
  
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

  public void setSaturation(double saturation) {
    widgetProxy.call(SET_SATURATION, saturation);
  }

  public void setBrightness(double brightness) {
    widgetProxy.call(SET_BRIGHTNESS, brightness);
  }
}
