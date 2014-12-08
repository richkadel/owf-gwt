package ozone.gwt.widget.direct;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

import ozone.gwt.widget.DirectLogger;
import ozone.gwt.widget.IntentDescriptor;
import ozone.gwt.widget.WidgetContainer;
import ozone.gwt.widget.WidgetDescriptor;
import ozone.gwt.widget.WidgetFramework;
import ozone.gwt.widget.WidgetHandle;
import ozone.gwt.widget.WidgetLogger;
import ozone.gwt.widget.direct.descriptorutil.SaveableText;
import ozone.gwt.widget.direct.descriptorutil.WidgetDescriptorText;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.IsWidget;

public class DirectWidgetFramework extends WidgetFramework {

  private static final DirectWidgetFramework widgetFramework = new DirectWidgetFramework();

  private static Map<Integer, WidgetHandle> gwtIsWidgetToWidgetHandle = new HashMap<Integer, WidgetHandle>();

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
  public WidgetHandle createWidgetHandleInstance(
      WidgetContainer widgetContainer, IsWidget gwtIsWidget) {
    if (gwtIsWidgetToWidgetHandle.get(gwtIsWidget.hashCode()) != null) {
      throw new Error(
          "Attempt to create a WidgetHandle for a GWT IsWidget that already has one!");
    }
    WidgetHandle widgetHandle = new DirectWidgetHandle(widgetContainer,
        gwtIsWidget);
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

  public String getWidgetDescriptorGeneratorScript(String displayGroupName, String prefix) {

    StringBuffer script = new StringBuffer();

    script.append(WidgetDescriptorText.INSTANCE
        .widgetDescriptorScriptPreamble().getText().replaceAll("\\r\\n", "\n"));

    JSONObject intentsMap = new JSONObject();

    Collection<WidgetHandle> widgetHandles = DirectWidgetFramework
        .getInstance().getWidgetHandles();
    for (WidgetHandle widgetHandle : widgetHandles) {

      WidgetDescriptor widgetDescriptor = WidgetDescriptor.create(widgetHandle,
          displayGroupName, prefix);

      String universalName = widgetDescriptor.getUniversalName();
      JsArray<IntentDescriptor> receivedIntents = widgetDescriptor
          .getReceivedIntents();
      int intentsLen = receivedIntents.length();
      for (int j = 0; j < intentsLen; j++) {
        IntentDescriptor intent = receivedIntents.get(j);
        String key = SaveableText.stringify(intent);
        JSONValue recipient = intentsMap.get(key);
        if (recipient != null) {
          // multiple recipients for the same intent, so make this map to empty
          // string and force OWF intents
          intentsMap.put(key, new JSONString(""));
        } else {
          intentsMap.put(key, new JSONString(universalName));
        }
      }

      script.append(WidgetDescriptorText.INSTANCE.widgetDescriptorSedCommand()
          .getText().replaceAll("\\r\\n", "\n"));
      script.append(widgetDescriptor.getWidgetName());
      script.append(".html\n");
      script.append(WidgetDescriptorText.INSTANCE.widgetDescriptorPreamble()
          .getText().replaceAll("\\r\\n", "\n"));

      String descriptorString = SaveableText.stringifyPretty(widgetDescriptor);
      script.append("var data = ");
      script.append(descriptorString);
      script.append(";\n");

      script.append(WidgetDescriptorText.INSTANCE.widgetDescriptorPostamble()
          .getText().replaceAll("\\r\\n", "\n"));
    }

    script.append("cat << EOF > intentsMap.js\n");
    script.append("var " + OWF_GWT_INTENTS_MAP_GLOBALVAR + " = ");
    script.append(SaveableText.stringifyPretty(intentsMap.getJavaScriptObject()));
    script.append(";\n");
    script.append("EOF\n\n");

    script
        .append(WidgetDescriptorText.INSTANCE.widgetDescriptorScriptPostamble()
            .getText().replaceAll("\\r\\n", "\n"));

    return script.toString();
  }

  public String saveDescriptors(String prefix) {
    String script = getWidgetDescriptorGeneratorScript("SAPPHIRE", prefix);
    String filename = "mkdesc.sh";
    if (prefix != null) {
      filename = prefix+"-"+filename;
    }
    SaveableText.create(script).saveAs(filename);
    return "Your browser should be downloading a script that will create OWF widget \n"
        + "descriptor files for all widgets active in this DirectWidgetFramework.\n"
        + "Execute this script from a Unix terminal, to save the widgets to /tmp/widgetdescriptors,\n"
        + "then move them to an accessible URL (e.g., into your WAR file).\n"
        + "Make the file executable or \"bash "+filename+"\" \n"
        + "(With no arguments, the script will print usage details and examples.)\n"
        + "\n"
        + "You may also want to save this script in your software code repository to\n"
        + "create new widget descriptors for new environments you may need to deploy\n"
        + "to in the future.";
  }

  private native void registerJavaScriptGlobals() /*-{
    var outerThis = this;
    $wnd.__OwfGwtSaveDescriptors = function(prefix) {
      return outerThis.@ozone.gwt.widget.direct.DirectWidgetFramework::saveDescriptors(Ljava/lang/String;)(prefix)
    }
    console.log("The global function __OwfGwtSaveDescriptors([prefix]) is available to generate OWF descriptor files.");
  }-*/;
}
