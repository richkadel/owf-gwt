package ozone.gwt.widget;

import jsfunction.NoArgsFunction;
import jsfunction.NoArgsFunctionReturn;
import jsfunction.VarArgsFunction;
import jsfunction.VarArgsFunctionReturn;

import com.google.gwt.core.client.JsArray;

public class WidgetProxyFunctions {
  
  private JsArray<WidgetProxyFunction> widgetProxyFunctions = JsArray.createArray().cast();
  
  public void add(String name, NoArgsFunction function) {
    add(WidgetProxyFunction.create(name, function));
  }
  
  public <T> void add(String name, NoArgsFunctionReturn<T> function) {
    add(WidgetProxyFunction.create(name, function));
  }
  
  public void add(String name, VarArgsFunction function) {
    add(WidgetProxyFunction.create(name, function));
  }
  
  public <T> void add(String name, VarArgsFunctionReturn<T> function) {
    add(WidgetProxyFunction.create(name, function));
  }
  
  public void add(WidgetProxyFunction widgetProxyFunction) {
    widgetProxyFunctions.push(widgetProxyFunction);
  }
  
  public JsArray<WidgetProxyFunction> toJsArray() {
    return widgetProxyFunctions;
  }
}
