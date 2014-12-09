package ozone.gwt.owfgwttest.proxies.data;

import com.google.gwt.core.client.JavaScriptObject;

public final class DocumentTreeSettings extends JavaScriptObject {

  protected DocumentTreeSettings() {}
  
  public static DocumentTreeSettings create(
        double saturation,
        double brightness
      ) {
    return nativeCreate(saturation, brightness);
  }
  
  private static native DocumentTreeSettings nativeCreate(
        double saturation,
        double brightness
      ) /*-{
    return {
      saturation : saturation,
      brightness : brightness
    }
  }-*/;
  
  public native double getSaturation() /*-{ return this.saturation; }-*/;
  public native double getBrightness() /*-{ return this.brightness; }-*/;
}
