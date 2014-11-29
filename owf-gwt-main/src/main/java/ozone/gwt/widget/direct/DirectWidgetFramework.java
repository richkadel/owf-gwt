package ozone.gwt.widget.direct;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

import jsfunction.gwt.JsFunctionUtils;
import ozone.gwt.widget.WidgetContainer;
import ozone.gwt.widget.WidgetDescriptor;
import ozone.gwt.widget.WidgetFramework;
import ozone.gwt.widget.WidgetHandle;
import ozone.gwt.widget.WidgetLogger;
import ozone.gwt.widget.direct.descriptorutil.SaveableText;
import ozone.gwt.widget.direct.descriptorutil.WidgetDescriptorText;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayMixed;
import com.google.gwt.user.client.ui.IsWidget;

public class DirectWidgetFramework extends WidgetFramework {
  
  private static final DirectWidgetFramework widgetFramework = new DirectWidgetFramework();

  private static Map<Integer,WidgetHandle> gwtIsWidgetToWidgetHandle = new HashMap<Integer,WidgetHandle>();

  private WidgetLogger directLogger = new DirectLogger();
  
  protected DirectWidgetFramework() {
    registerJavaScriptGlobals();
  }

  public static DirectWidgetFramework getInstance() {
    return widgetFramework;
  }
  
  public Collection<WidgetHandle> getWidgetHandles() {
    return gwtIsWidgetToWidgetHandle.values();
  }
  
  @Override
  public WidgetHandle createWidgetHandleInstance(WidgetContainer widgetContainer, IsWidget gwtIsWidget) {
    if (gwtIsWidgetToWidgetHandle.get(gwtIsWidget.hashCode()) != null) {
      throw new Error("Attempt to create a WidgetHandle for a GWT IsWidget that already has one!");
    }
    WidgetHandle widgetHandle = new DirectWidgetHandle(widgetContainer, gwtIsWidget);
    gwtIsWidgetToWidgetHandle.put(gwtIsWidget.hashCode(), widgetHandle);
    return widgetHandle;
  }

  public WidgetHandle getWidgetHandleInstance(IsWidget gwtIsWidget) {
    return gwtIsWidgetToWidgetHandle.get(gwtIsWidget.hashCode());
  }
  
  public static void removeWidget(IsWidget gwtIsWidget) {
    gwtIsWidgetToWidgetHandle.remove(gwtIsWidget.hashCode());
  }

  @Override
  public WidgetLogger getLogger() {
    return directLogger;
  }
  
  private static class DirectLogger implements WidgetLogger {

    @Override
    public void trace(Object... messages) {
      trace(JsFunctionUtils.varArgsToMixedArray(messages));
    }

    public native void trace(JsArrayMixed messages) /*-{
      if (console.trace) {
        console.trace.apply(console, messages);
      } else {
        console.log.apply(console, messages);
      }
    }-*/;

    @Override
    public void debug(Object... messages) {
      debug(JsFunctionUtils.varArgsToMixedArray(messages));
    }

    public native void debug(JsArrayMixed messages) /*-{ // according to Mozilla standards documentation, this is an alias for "log"
      if (console.log) {
        console.log.apply(console, messages);
      } else {
        console.log.apply(console, messages);
      }
    }-*/;

    @Override
    public void info(Object... messages) {
      info(JsFunctionUtils.varArgsToMixedArray(messages));
    }

    public native void info(JsArrayMixed messages) /*-{
      if (console.info) {
        console.info.apply(console, messages);
      } else {
        console.log.apply(console, messages);
      }
    }-*/;

    @Override
    public void warn(Object... messages) {
      warn(JsFunctionUtils.varArgsToMixedArray(messages));
    }

    public native void warn(JsArrayMixed messages) /*-{
      if (console.warn) {
        console.warn.apply(console, messages);
      } else {
        console.log.apply(console, messages);
      }
    }-*/;

    @Override
    public void error(Object... messages) {
      error(JsFunctionUtils.varArgsToMixedArray(messages));
    }

    public native void error(JsArrayMixed messages) /*-{
      if (console.error) {
        console.error.apply(console, messages);
      } else {
        console.log.apply(console, messages);
      }
    }-*/;

    @Override
    public void fatal(Object... messages) {
      fatal(JsFunctionUtils.varArgsToMixedArray(messages));
    }
    
    public native void fatal(JsArrayMixed messages) /*-{ // according to Mozilla standards documentation, there is no "fatal()"
      if (console.error) {
        console.error.apply(console, messages);
      } else {
        console.log.apply(console, messages);
      }
    }-*/;
  }

  public String getWidgetDescriptorGeneratorScript(String displayGroupName) {
    
    StringBuffer script = new StringBuffer();

    script.append(WidgetDescriptorText.INSTANCE.widgetDescriptorScriptPreamble().getText().replaceAll("\\r\\n", "\n"));
    
    Collection<WidgetHandle> widgetHandles = DirectWidgetFramework.getInstance().getWidgetHandles();
    for (WidgetHandle widgetHandle : widgetHandles) {
      
      WidgetDescriptor widgetDescriptor = WidgetDescriptor.create(widgetHandle, displayGroupName);
      
      script.append(WidgetDescriptorText.INSTANCE.widgetDescriptorSedCommand().getText().replaceAll("\\r\\n", "\n"));
      script.append(widgetDescriptor.getWidgetName());
      script.append(".html\n");
      script.append(WidgetDescriptorText.INSTANCE.widgetDescriptorPreamble().getText().replaceAll("\\r\\n", "\n"));
      
      String descriptorString = stringifyPretty(widgetDescriptor);
      script.append("var data = ");
      script.append(descriptorString);
      script.append(";\n");
      
      script.append(WidgetDescriptorText.INSTANCE.widgetDescriptorPostamble().getText().replaceAll("\\r\\n", "\n"));
    }
    
    script.append(WidgetDescriptorText.INSTANCE.widgetDescriptorScriptPostamble().getText().replaceAll("\\r\\n", "\n"));
    
    return script.toString();
  }
  
  private static native String stringifyPretty(JavaScriptObject jso) /*-{
    return JSON.stringify(jso, null, '\t');
  }-*/;
  
  public String saveDescriptors() {
  	String script = getWidgetDescriptorGeneratorScript("SAPPHIRE");
    SaveableText.create(script).saveAs("mkdesc.sh");
    return "Your browser should be downloading a script that will create OWF widget \n"
        + "descriptor files for all widgets active in this DirectWidgetFramework.\n"
        + "Execute this script from a Unix terminal, to save the widgets to /tmp/widgetdescriptors,\n"
        + "then move them to an accessible URL (e.g., into your WAR file).\n"
        + "Make the file executable or \"bash mkdesc.sh\" \n"
        + "(With no arguments, the script will print usage details and examples.)\n"
        + "\n"
        + "You may also want to save this script in your software code repository to\n"
        + "create new widget descriptors for new environments you may need to deploy\n"
        + "to in the future.";
  }

  private native void registerJavaScriptGlobals() /*-{
    var outerThis = this;
    $wnd.__OwfGwtSaveDescriptors = function() {
      return outerThis.@ozone.gwt.widget.direct.DirectWidgetFramework::saveDescriptors()()
    }
    console.log("The global function __OwfGwtSaveDescriptors() is available to generate OWF descriptor files.");
  }-*/;
}
