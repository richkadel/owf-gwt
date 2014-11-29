package ozone.gwt.widget.direct.descriptorutil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface WidgetDescriptorText extends ClientBundle {

  public WidgetDescriptorText INSTANCE = GWT.create(WidgetDescriptorText.class);
  
  @Source("widgetDescriptorScriptPreamble.txt")
  TextResource widgetDescriptorScriptPreamble();
  
  @Source("widgetDescriptorSedCommand.txt")
  TextResource widgetDescriptorSedCommand();
  
  @Source("widgetDescriptorPreamble.txt")
  TextResource widgetDescriptorPreamble();
  
  @Source("widgetDescriptorPostamble.txt")
  TextResource widgetDescriptorPostamble();
  
  @Source("widgetDescriptorScriptPostamble.txt")
  TextResource widgetDescriptorScriptPostamble();
}
