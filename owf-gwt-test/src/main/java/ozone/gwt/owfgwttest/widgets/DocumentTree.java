package ozone.gwt.owfgwttest.widgets;

import ozone.gwt.widget.WidgetFramework;
import ozone.gwt.widget.WidgetHandle;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class DocumentTree extends TabLayoutPanel implements EntryPoint {
  
  private WidgetHandle widgetHandle;

  public DocumentTree() {
    super(1.5, Unit.EM);
    widgetHandle = WidgetFramework.createWidgetHandle(null, this);
  }
  
  @Override
  public void onLoad() {
    
    TreeItem root = new TreeItem();
    root.setText("root");
    root.addTextItem("item0");
    root.addTextItem("item1");
    root.addTextItem("item2");

    // Add a CheckBox to the tree
    //TreeItem item = new TreeItem(new CheckBox("item3"));
    //root.addItem(item);

    Tree tree = new Tree();
    tree.addItem(root);
    
    root.setState(true);//open
    
    ScrollPanel scroll = new ScrollPanel();
    scroll.setWidget(tree);
    scroll.setSize("100%", "100%");
    
    this.add(scroll, "Documents");
    setSize("100%", "100%");
  }

  @Override
  public void onModuleLoad() {
    RootPanel.get().add(this);
  }
}
