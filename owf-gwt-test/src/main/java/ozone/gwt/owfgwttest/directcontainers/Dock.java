package ozone.gwt.owfgwttest.directcontainers;

import ozone.gwt.owfgwttest.widgets.DocumentTree;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;

public class Dock extends DockLayoutPanel implements EntryPoint {
  
  public Dock() {
    super(Unit.EM);
    
    TabLayoutPanel documentPanel = new TabLayoutPanel(1.5, Unit.EM);
    documentPanel.add(new DocumentTree(), "Documents");
    documentPanel.setSize("100%", "100%");
    
    addSouth(new FormTabs(), 10);
    addWest(documentPanel, 10);
    add(new ViewTabs());
    
    setPixelSize(650, 650);
  }

  @Override
  public void onModuleLoad() {
    RootPanel.get().add(this);
  }
}
