package ozone.gwt.owfgwttest.directcontainers;

import ozone.gwt.owfgwttest.widgets.DocumentTree;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class Dock extends DockLayoutPanel implements EntryPoint {
  
  public Dock() {
    super(Unit.EM);
    
    addSouth(new FormTabs(), 10);
    addWest(new DocumentTree(), 10);
    add(new ViewTabs());
    
    setPixelSize(650, 650);
  }

  @Override
  public void onModuleLoad() {
    RootPanel.get().add(this);
  }
}
