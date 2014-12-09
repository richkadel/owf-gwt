package ozone.gwt.owfgwttest.widgets;

import jsfunction.gwt.functions.NoArgsFunctionJsReturn;
import jsfunction.gwt.functions.VarArgsFunction;
import ozone.gwt.owfgwttest.channels.DocumentTreeInitializationChannel;
import ozone.gwt.owfgwttest.channels.data.DocumentTreeInitializationData;
import ozone.gwt.owfgwttest.intents.ShowTable;
import ozone.gwt.owfgwttest.intents.data.TableData;
import ozone.gwt.owfgwttest.proxies.DocumentTreeProxy;
import ozone.gwt.owfgwttest.proxies.data.DocumentTreeSettings;
import ozone.gwt.widget.OnReceipt;
import ozone.gwt.widget.WidgetFramework;
import ozone.gwt.widget.WidgetHandle;
import ozone.gwt.widget.WidgetProxy;
import ozone.gwt.widget.WidgetProxyFunctions;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JsArrayMixed;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
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
    
    tree.addSelectionHandler(new SelectionHandler<TreeItem>() {
      public void onSelection(SelectionEvent<TreeItem> selected) {
        ShowTable.startActivity(
          widgetHandle, 
//TODO
          TableData.create("arg1", selected.getSelectedItem().getText()),
          new OnReceipt() {
            public void intentReceived(WidgetProxy dest) {
//TODO
            }
            public void intentNotReceived() {
              GWT.log("ShowTable intent was not received");
            }
          }
        );
      }
    });
    
    root.setState(true);//open
    
    setWidget(tree);
    setSize("100%", "100%");
    
    WidgetProxyFunctions wpf = new WidgetProxyFunctions();

    wpf.add(DocumentTreeProxy.GET_SETTINGS,
        new NoArgsFunctionJsReturn<DocumentTreeSettings>() {
          public DocumentTreeSettings callback() {
//TODO
            return DocumentTreeSettings.create(1.0, 1.0);
          }
        });

    wpf.add(DocumentTreeProxy.SET_SATURATION, new VarArgsFunction() {
      public void callback(JsArrayMixed args) {
        double saturation = args.getNumber(0);
//TODO
      }
    });

    wpf.add(DocumentTreeProxy.SET_BRIGHTNESS, new VarArgsFunction() {
      public void callback(JsArrayMixed args) {
        double brightness = args.getNumber(0);
//TODO
      }
    });
    
//TODO
    new DocumentTreeInitializationChannel(widgetHandle) {
      protected void messageReceived(WidgetProxy sender,
          DocumentTreeInitializationData message) {
        GWT.log("Got DocumentTreeInitializationChannel message: "+message.getPublisherMessage());
      }
    };

    widgetHandle.registerWidgetProxyFunctions(wpf);
  }

  @Override
  public void onModuleLoad() {
    RootPanel.get().add(this);
  }
}
