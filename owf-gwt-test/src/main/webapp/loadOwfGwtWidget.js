/*jslint indent: 2 */
/*jslint regexp: true */
/*jslint plusplus: true */
/*jslint white: true */
/*jslint nomen: true */
/*global document, window, OWF, Ozone */

//////////////////////////////////////////////////////////
// THIS FILE IS COPIED FROM the OWF-GWT SOURCE CODE.
// IF ANY CHANGES ARE MADE TO THIS FILE, PLEASE CONTRIBUTE
// THE CHANGES BACK TO OWF-GWT:
//
// https://github.com/richkadel/owf-gwt
//
// License: Apache License, Version 2.0
//////////////////////////////////////////////////////////

(function() { // self-executing anonymous function
  "use strict"; // JavaScript pragma

// This automatically loads required OWF resources
//  
// If the "superdevmode" parameter is provided, this sets up the widget
// to run in GWT Super Dev Mode, AND adds a reload button to the OWF widget's
// title bar to recompile the GWT source via the standard GWT source server at
// port 9876.
//
// If not superdevmode, the reload button simply refreshes the widget.
//
// First, use a trick to get parameters from the script tag "src" attribute
// URL (from http://feather.elektrum.org/book/src.html)

  var
    scripts = document.getElementsByTagName('script'),
    thisScript = scripts[scripts.length - 1],
    params,
    server_url,
    docroot,
    appname,
    srctype,
    question,
    // functions //
    parseQuery,
    addStartdevmodescript,
    appendJavascript,
    showError,
    appendCss,
    checkStartdevmodescript,
    widgetState;

  parseQuery = function(url) {

    var
      query = url.replace(/^[^\?]+\??/, ''),
      retval = {},
      Pairs,
      KeyVal,
      key,
      val,
      i;

    if (!query) {
      return retval; // return empty object
    }
    Pairs = query.split(/[;&]/);
    for (i = 0; i < Pairs.length; i++) {
      KeyVal = Pairs[i].split('=');
      if (KeyVal && KeyVal.length === 2) {
        key = decodeURI(KeyVal[0]);
        val = decodeURI(KeyVal[1]);
        val = val.replace(/\+/g, ' ');
        retval[key] = val;
      }
    }
    return retval;
  };

  addStartdevmodescript = function() {
    
    var
      s;
    
    window.__gwt_bookmarklet_params = {
      'server_url' : server_url
    };
    s = document.createElement('script');
    s.id = 'startdevmodescript';
    s.src = 'http://localhost:9876/dev_mode_on.js';
    document.getElementsByTagName('head')[0].appendChild(s);
  };

  checkStartdevmodescript = function() {
    
    OWF.ready(function() {

      var widgetEventingController = Ozone.eventing.Widget.getInstance();
      widgetState = Ozone.state.WidgetState.getInstance({
        widgetEventingController:  widgetEventingController,
        autoInit: false
      }); 

      OWF.Chrome.removeHeaderButtons({
        items:[{
          itemId:'refresh'
        }]
      });
      
      OWF.Chrome.insertHeaderButtons({
        items:[{
          type: 'refresh', // standard EXT/JS tool
          itemId:'refresh',
          handler: function() {
            if (widgetState !== undefined) {
              widgetState.activateWidget();
            }
            if (params.superdevmode) {
              addStartdevmodescript();
            } else {
              window.location.reload();
            }
          }
        }]
      });
    });
  };

  appendCss = function(href) {
    
    var
      csslink = document.createElement('link');
    
    csslink.rel = "stylesheet";
    csslink.type = "text/css";
    csslink.href = href;
    document.documentElement.firstChild.appendChild(csslink);
  };

  appendJavascript = function(src,callback) {
    
    var
      script = document.createElement('script');
    
    script.type="text/javascript";
    script.src=src;
    if (callback !== undefined) {
      script.onload=callback;
    }
    document.documentElement.firstChild.appendChild(script);
  };

  showError = function(message) {
    
    var
      body = document.body,
      h1 = document.createElement("h1");
    
    if (!body) {
      body = document.createElement("body");
      document.body = body;
    }
    h1.innerHTML = message;
    body.appendChild(h1);
  };
  
  /////////////////////////////////////
  // END OF FUNCTION DECLARATIONS
  /////////////////////////////////////
  // START OF EXECUTION BODY
  /////////////////////////////////////

  params = parseQuery(thisScript.src);

  if (Object.keys(params).length === 0) {
    params = parseQuery(document.URL);
  }

  server_url = 'http://localhost:9876/';

  docroot = params.docroot;

  appname = params.appname;

  srctype = params.srctype;

  if (!srctype) {
    srctype = "min";
  }

  if (!appname) {
    if (window.location.hash) {
      appname = window.location.hash.substring(1);
      question = appname.indexOf("?");
      if (question >= 0) {
        appname = appname.substring(0,question);
      }
      if (appname.length === 0) {
        appname = null;
      }
    }
    if (!appname) {
      showError("App name not set!");
      return;
    }
  }

  if (docroot === undefined) {
    docroot = ""; // assume paths are relative to the enclosing HTML file
                  // (the file that embedded this script)
  } else if (docroot.length > 0 && docroot.charAt(docroot.length-1) !== '/') {
    docroot += "/";
  }

  // Need reset.css for GXT support if this is a GXT app
  appendCss(docroot+appname+"/reset.css");
  
  if (document.title && document.title !== "") {
    document.title = appname+" - "+document.title;
  } else {
    document.title = appname;
  }

  //////////////////////////////////////////////////
  // This script loads your compiled module.
  // If you add any GWT meta tags, they must
  // be added before this line.
  //////////////////////////////////////////////////
  appendJavascript(docroot+appname+"/"+appname+".nocache.js");

  if (window.name !== undefined && window.name !== "") { 
    // assume we are running in OWF
    appendCss(docroot+"owf/css/dragAndDrop.css");
    appendJavascript(docroot+"owf/js-min/owf-widget-"+srctype+".js", checkStartdevmodescript);
  }
}());
