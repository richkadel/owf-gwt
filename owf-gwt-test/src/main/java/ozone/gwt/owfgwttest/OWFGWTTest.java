package ozone.gwt.owfgwttest;

import jsfunction.gwt.JsFunction;
import jsfunction.gwt.NoArgsFunction;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JsArrayMixed;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class OWFGWTTest implements EntryPoint {
  
  private static int count = 1;

  @Override
  public void onModuleLoad() {
    
    /////////////////////////////////////
    /***********************************/
    /////////////////////////////////////
    /***********************************/
    // THIS NEEDS TO BE REPLACED WITH A TEST HARNESS FOR THE OWF LIBRARY
    // (For now, this is being tested as part of the Harmonia SAPPHIRE development under their ONR efforts.)
    /***********************************/
    /////////////////////////////////////
    /***********************************/
    /////////////////////////////////////
    
    testWindowOnclick(new NoArgsFunction() {
      public void callback() {
        log("Made it", count++);
      }
    });
    
    RootPanel.get().add(new Label("Open the browser console and click this window to see test output."));
  }
  
  private void testWindowOnclick(NoArgsFunction noArgsFunction) {
    nativeWindowOnClick(JsFunction.create(noArgsFunction));
  }

  private native void nativeWindowOnClick(JsFunction callback) /*-{
    $wnd.onclick = callback
  }-*/;

  public void log(Object... varargs) {
    nativeLog(JsFunction.varArgsToMixedArray(varargs));
  }
  
  public native void nativeLog(JsArrayMixed arguments) /*-{
    console.log.apply(console, arguments);
  }-*/;
}
