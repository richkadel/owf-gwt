package ozone.gwt.owfgwttest.proxies;

import jsfunction.gwt.returns.JsReturn;
import ozone.gwt.owfgwttest.proxies.data.DocumentTreeSettings;
import ozone.gwt.widget.WidgetProxy;

public class MapViewProxy {
  
  public static final String MORPH_TO_2D = "morphTo2D";
  public static final String MORPH_TO_3D = "morphTo3D";
  public static final String MORPH_TO_COLUMBUS_VIEW = "morphToColumbusView";
  public static final String GET_SETTINGS = "getSettings";
  public static final String SET_SATURATION = "setSaturation";
  public static final String SET_BRIGHTNESS = "setBrightness";
  
  private WidgetProxy widgetProxy;

  public MapViewProxy(WidgetProxy widgetProxy) {
    this.widgetProxy = widgetProxy;
  }
  
  public WidgetProxy getWidgetProxy() {
    return this.widgetProxy;
  }

  public void morphTo2D() {
    widgetProxy.call(MORPH_TO_2D);
  }

  public void morphTo3D() {
    widgetProxy.call(MORPH_TO_3D);
  }

  public void morphToColumbusView() {
    widgetProxy.call(MORPH_TO_COLUMBUS_VIEW);
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
