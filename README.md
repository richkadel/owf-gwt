**Note that this library is now maintained in the _Harmonia Holdings Group_ [GitHub site for OWF-GWT](https://github.com/Harmonia-Holdings-Group/owf-gwt)**

OWF-GWT
=======

OWF-GWT implements Java-based Google Web Toolkit (GWT) wrappers around 
the [OZONE Widget Framework (OWF)](http://ozoneplatform.org) library.
A developer building a GWT application can use OWF-GWT to much more
easily add their GWT widgets into an OWF environment, using Java APIs
and Java IDE's to extend their app with OWF pub/sub channels,
proxy methods, and intents, as well as automatic widget launching
and other features that help expose the app as a firt class OWF widget.

Tools have also been incorporated to simplify widget deployment to
multiple OWF instances. And OWF-GWT includes a very useful
and fully-implemented option to expose a collection of widgets outside
of an OWF environment (as a standalone application with a collection
of widgets), without losing any of the implemented IWC capabilities.

Dependencies
------------

Besides GWT itself, OWF-GWT depends on the 
[JsFunction-GWT](http://github.com/richkadel/jsfunction-gwt) 
project, also on GitHub, and was partially modeled after
the GWT/JS integration done for [Cesium-GWT](http://github.com/richkadel/cesium-gwt).

History
-------

My first significant GWT application ("SAPPHIRE") was a stand-alone,
single GWT app based on Sencha GXT.
It had separate "views" and "forms" in various tabs and layouts, and each
tabbed component could call any other component via direct Java (GWT)
method calls.  This was very convenient, but did not support loading
these components as separable widgets into the DoD-preferred 
OZONE Widget Framework (OWF).

Samples
-------

OWF-GWT's `owf-gwt-test` module demonstrates how to create GWT
widgets that work in a standalone GWT app (the `EntryPoint` widget
in `Dock.java`, with matching GWT module descriptor `owfgwttest.gwt.xml`)
as well as work as individual, independent widgets in an OWF
framework.

Browse [Dock.java](https://github.com/richkadel/owf-gwt/blob/master/owf-gwt-test/src/main/java/ozone/gwt/owfgwttest/directcontainers/Dock.java)
along with
[FormTabs.java](https://github.com/richkadel/owf-gwt/blob/master/owf-gwt-test/src/main/java/ozone/gwt/owfgwttest/directcontainers/FormTabs.java)
and
[ViewTabs.java](https://github.com/richkadel/owf-gwt/blob/master/owf-gwt-test/src/main/java/ozone/gwt/owfgwttest/directcontainers/ViewTabs.java)
to see how the individual widgets are loaded directly into a standalone
GWT app.

The rest of this README file explains how these widgets are also
prepared for and deployed to an OWF environment.


Steps to Convert GWT Widgets to OWF Widgets
-------------------------------------------

To solve the problem, I had to make each component a separable GWT
module. This entails the following:

1. Implement the GWT EntryPoint interface in the component. 

    * In a typical GWT application, each component is either a Widget 
      or an "IsWidget" (IsWidget
      is an interface with the method "asWidget()" that allows the
      widget initialization to be deferred until the actual Widget is
      needed--e.g., to load it into a container.)

    * EntryPoint requires one additional method: "onModuleLoad()". 
      This is a good place to perform any standard configuration,
      including loading any configuration information, for example.
      The one thing normally required in a GWT app is to load the
      primary UI (typically "this" widget) into the `RootPanel`
      (the main window or--in the case of OWF--the widget frame):
      
            public void onModuleLoad() {
              RootPanel.get().add(this);
            }
  
2. Create the GWT module descriptor for each component
  that will now be an individual OWF widget.
  In order to take advantage of OWF-GWT's deployment
  tools, the GWT module descriptor *must* be named
  the widget class name with ".gwt.xml" extension, e.g.,
  TableView.java would have the descriptor: 

        TableView.gwt.xml

    Copy one of the existing module descriptors
    and change "<module rename-to=" to match the module name in the
    of the descriptor file; add or remove inherits (note it's possible
    that you may not realize you need some of these--comment out ones
    you THINK you don't need, so you can easily add them back); and
    change the "<entry-point" to the EntryPoint class in step 1.

3. Update the Run Configurations for the SuperDevMode and the
  DevMode servers.  Both of these Run Configurations contain arguments
  that refer to every possible GWT module.  

    DevMode's final arguments
    are a space-separated list of dot-delimited module names to load, 
    of the form "com.harmonia.sapphire.mywidget" (referring to the 
    location and name of the GWT module file, without the ".gwt.xml" suffix).
    DevMode's list SHOULD get updated automatically, when you open
    the RunConfiguration, because the standard DevMode run configuration
    editor automatically searches for available modules in the source
    path, like the one you just created in step 2.
    
    SuperDevMode (aka CodeServer) requires a -src parameter for each
    module, with two following arguments: the path to the Java source
    and the dot-delimited module, just like what was provided in the DevMode
    arguments. It's likely there is a valid relative path to use, but
    I have used the practice of providing a fully-qualified path, e.g.:
    
        -src /Users/richkadel/work/harmonia/sapphire/harmonia-sapphire-apps/src/main/java com.harmonia.sapphire.mywidget
  
    You may want to figure out the starting location and use a relative
    path; but certainly remove and/or replace the specific user account
    "/Users/richkadel" and any leading path components (like "work"),
    and set the module location/name appropriately

4. Go back into the EntryPoint class you are modifying, and enable it
  for OWF. One requirement of ALL EntryPoint classes is that it have
  a no-arg constructor. Most SAPPHIRE apps require two constructors:
  the no-arg constructor used when called from GWT as an EntryPoint,
  and a constructor that takes a WidgetHandle, passed in directly from
  a class that implements WidgetContainer. The original SAPPHIRE GUI
  has two TabbedWidgetContainers (one for views and one for forms).

    Standard SAPPHIRE best practice for OWF and direct widget constructors
    is as follows:

        public SPARQLForm() {// entry point constructor
          this(null);
        }
        
        public SPARQLForm(WidgetContainer widgetContainer) {
          widgetHandle = WidgetFramework.createWidgetHandle(widgetContainer, this);
          // do not use until Widget.onLoad() or IsWidget.asWidget() are called
          ...
        }
  
    The TreeView class is the exception. It is not held in a WidgetContainer
    so there is only the no-arg constructor, and the WidgetContainer 
    parameter in the createWidgetHandle method is passed in as "null".
    
5. If your Widget is a subclass of a GWT Widget, you should override
  the Widget.onLoad() method to complete your initialization. 
  But if your widget class implements "IsWidget",
  you will likely initialize the widget in the "asWidget()" method (with
  a check to ensure your initialization only happens one time, since
  asWidget() can be called multiple times and subsequent calls should
  simply return the main widget created during the first invocation).
  Wherever you do the initialization, you will need to add
  OWF-style widget initializations including a declaration of received
  "Intent"s, subscribed "EventingChannel"s, and registered
  "WidgetFunctionProxy"s.
  
    Here's a typical
    call sequence at the end of the widget initialization, which starts "listening"
    for widget intents for FolderItem's having been selected or double-clicked.
    At the end, call widgetHandle.notifyWidgetReady() to let other widget's know
    the widget is ready to receive events:
    
        new FolderItemSelected(widgetHandle, MapView.CONFIG_TYPE_SUFFIX) {
          @Override
          public void intentReceived(WidgetProxy sender, FolderItemMetaData data) {
            folderItemSelected(data);
          }
        };
        
        new FolderItemDoubleClicked(widgetHandle, MapView.CONFIG_TYPE_SUFFIX) {
          @Override
          public void intentReceived(WidgetProxy sender, FolderItemMetaData data) {
            folderItemDoubleClicked(data);
          }
        };
        
        new PointsToMap(widgetHandle) {
          public void intentReceived(WidgetProxy sender, JsMetaTable jsMetaTable) {
            widgetHandle.activate();
            MetaTable metaTable = new JsMetaTableHolder(jsMetaTable);
            addPoints(metaTable);
          }
        };
      
        WidgetProxyFunctions wpf = new WidgetProxyFunctions();
      
        wpf.add(MapViewProxy.MORPH_TO_2D, new NoArgsFunction() {
          public void callback() {
            getCesiumWidget().getScene().morphTo2D();
          }
        });
      
        wpf.add(MapViewProxy.MORPH_TO_3D, new NoArgsFunction() {
          public void callback() {
            getCesiumWidget().getScene().morphTo3D();
          }
        });
      
        wpf.add(MapViewProxy.GET_SETTINGS,
            new NoArgsFunctionJsReturn<MapViewSettings>() {
              public MapViewSettings callback() {
                return MapViewSettings.create(getBaseLayer().getSaturation(),
                    getBaseLayer().getBrightness(), getCesiumWidget().getScene()
                        .getMode());
              }
            });
      
        widgetHandle.registerWidgetProxyFunctions(wpf);
        
        new TreeViewChannel(widgetHandle) {
          @Override
          public void onRequestFolderDetails(WidgetProxy tree) {
            AddSaveableView.startActivity(widgetHandle, saveableView);
          }
        };
    
6. If your Widget sends any intents, or uses another form of inter-widget communication
  (WidgetProxy methods or Channel pub/sub), 
  you will want to add those calls. If converting a GWT app from direct calls, remove
  all of the direct references to objects that are no longer part of your OWF
  widget (e.g., other widgets, or containers that will be removed in lieu of
  the OWF environment) and replace those calls with Intents or Channel
  messages, or WidgetProxy calls.  (WidgetProxy calls require a WidgetProxy,
  which is typically obtained by coordinating with another widget through
  Intents or a Channel pub/sub message first.)
  
7. Create the initial widget descriptor file. You can actually initialize a
  widget within OWF using the Widget Editor, but it is much easier to
  deploy a widget into OWF through a pre-existing widget descriptor file.
  The `owf-gwt-test` module includes some samples you can start from
  (copy and modify to suit):

        src/main/webapp/testwidgets

    Copy one, and ensure you update:

    * universalName
    * widgetUrl (note the use of the URL fragment so a single HTML file can support multiple SAPPHIRE widgets)
    * imageUrlLarge - Create a 128x128 pixel image unique to your widget
    * imageUrlSmall - The large 128x128 images actually scale well in OWF, so you can typically use the same image for both large and small
    * displayName
    * intents, receive array - See the examples, and match the Intents subclasses you are receiving in your Widget initialization
    
    At this time, it appears there is no strong reason to declare the Intents a widget "sends".
    
    OWF-GWT provides a mechanism to easily create all of the widget descriptors automatically,
    and to re-generate widget descriptors for different environments and for different
    modes (production versus debug, with and without support for GWT Super Dev Mode).
    As long as you have implemented
    a standalone (non-OWF, aka "direct" mode) implementation of your 
    complete app (e.g., you are instantiating all
    widgets at once, in a pre-existing GWT widget layout with or without
    WidgetContainer implementations like `Dock`, `ViewTabs`, and `FormTabs`),
    you can have OWF-GWT generate a script that you can invoke to generate descriptors.
    Run your app to launch all of the widgets outside OWF, and then open the browser's
    JavaScriptConsole to manually invoke the global method:
    
        > __OwfGwtSaveDescriptors()

    (Google Chrome and other modern browsers allow this kind of dynamic global function
    invocation.) 
    
    After successful execution, the console will show the following message:
    
        "Your browser should be downloading a script that will create OWF widget 
        descriptor files for all widgets active in this DirectWidgetFramework.
        Execute this script from a Unix terminal, to save the widgets to /tmp/widgetdescriptors,
        then move them to an accessible URL (e.g., into your WAR file).
        Make the file executable or "bash mkdesc.sh" 
        (With no arguments, the script will print usage details and examples.)
        
        You may also want to save this script in your software code repository to
        create new widget descriptors for new environments you may need to deploy
        to in the future."
        
    And your browser will either automatically start or prompt for you to start
    downloading a generated bash script file you can execute to generate a complete
    set of widget descriptors along with all declared received intents.
    
    Note, `__OwfGwtSaveDescriptors()` can take two parameters: 
    
    * "displayGroupName" -
      (typically your overall system/application name or company, such as "Harmonia")
      used to set the OWF widget's display name (used in a 
      widget title bar) to include the group, distinguishing a set of widgets
    
    * "prefix" -
      used to add a prefix to the widget's name and universalName
      so you can create two versions of a widget in the same OWF (typically
      with different widget descriptor configurations, e.g., different launch
      parameters--e.g., one for production mode testing and one for debugging).
    
    After downloading the script to a suitable location (you may want to
    check it into your sourcecode baseline for easy access and reuse for
    new deployment environments), you can run the
    script through `bash` or change it's permissions to make the script
    executable (e.g., `chmod 744 mkdesc.sh`).
    
    Execute this script without parameters to see the parameter options and
    examples. When created (by passing in the required parameters), the
    widget descriptors and a couple of useful files (paths.txt and 
    intentsMap.txt--see below for more information on each of these files)
    are written to
    /tmp/widgetdescriptors (replacing any previous versions in that directory).
    All of these files can be copied to a
    path inside your webapp, e.g., src/main/webapp/testwidgets/

8. In OWF, using an OWF admin account (like testAdmin1, if using the default
  development setup), open the "Widgets" widget from the widget 
  tools button. Select the "Create" button and enter the URL of the
  descriptor file previously created above. If successfully loaded, press
  the "Apply" button. If the "wait" icon spins, either the URL is wrong
  or you might have forgotten to restart the "DevMode" server to recognize
  the new descriptor file, or to "Refresh" the Package Explorer to make
  sure Eclipse sees the files added to the webapps directory, before restarting
  DevMode. Hit "Cancel" and try to correct the issue.
  
    Note the __OwfGwtSaveDescriptors() script creates a "paths.txt" file
    listing all of the widget descriptor paths for your convenience. You can
    quickly copy and paste these paths into successive instances of the
    Widget Editor as you load each one.

9. Before closing the "Widget Editor", select "Users" (and/or "Groups") and
  add any users that need access to this widget. Close the 
  "Widget Editor" and "Widget" widget.  Reload OWF (often, the new widget
  will not show up automatically, and may also suppress other widgets).
  Or it can be faster to open the "Users" editor and add all new widgets
  to a given user at one time.
  Then try to add your new widget to your OWF desktop.

    If you are using Intents, you will be prompted for the widget to receive an intent, at the time
    it is used. Click the checkbox to remember each selection, and you won't be prompted for that
    intent again.  If you have a problem with an intent (either not being sent or not received),
    close the affected widget(s) and reopen them. This will start the intent selection process again.
    
    Note the __OwfGwtSaveDescriptors() script creates an "intentsMap.txt" file
    that lists all intents with only one recipient, and the unique widget 
    universal name that receives that intent. When you run the "mkdesc.sh"
    script, you can specify a parameter to use this intents map, which
    avoids prompting the user to specify the destination widget when there
    is only one. (The side effect is, only one instance of the destination
    can be used.)
