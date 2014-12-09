package ozone.gwt.owfgwttest.widgets;

import ozone.gwt.widget.WidgetFramework;
import ozone.gwt.widget.WidgetHandle;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class DocumentTree extends ScrollPanel implements EntryPoint {
  
  private WidgetHandle widgetHandle;

  public DocumentTree() {
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
    
    setWidget(tree);
    setSize("100%", "100%");
  }

  @Override
  public void onModuleLoad() {
    RootPanel.get().add(this);
  }
}
