#!/bin/bash ############################ beginning of script ############################

# This script was created from OWF-GWT's built-in widget descriptor generator script
# generator (yes, I said "generator" twice). If you run your OWF-GWT widgets in a
# DirectWidgetFramework implementation, and configure them the way you would like
# to see them in an OWF environment as well, you can open a Web Browser console
# session (e.g., Google Chrome "Developer Tools") and execute (from the JavaScript
# console's interactive prompt) the global function:
#
#     > __OwfGwtSaveDescriptors()
# 
# If successful, it should respond with:
# 
#     < "Your browser should be downloading a script that will create OWF widget 
#     descriptor files for all widgets active in this DirectWidgetFramework.
#     Execute this script from a Unix terminal, to save the widgets to /tmp/widgetdescriptors,
#     then move them to an accessible URL (e.g., into your WAR file).
#     Make the file executable or "bash mkdesc.sh" 
#     (With no arguments, the script will print usage details and examples.)
# 
#     You may also want to save this script in your software code repository to
#     create new widget descriptors for new environments you may need to deploy
#     to in the future."
#
# And the browser should prompt for you to download the "potentially harmful" script
# "mkdesc.sh". (Potentially harmful because it is a linux script, of course.)
# Save it to an accessible location, or check it into your baseline so you
# can execute it whenever you need to regenerate widget descriptors for a new
# environment.
# 
# See the usage block below or execute mkdesc.sh with no arguments.

if [ $# == 0 ]; then
  echo "usage $0 <appUrlPrefix> [<descriptorDir>/] [<appParams>]"
  echo 
  echo "appParams are in URL parameter format: ?<name>=<value[\\&<name>=<value>...]"
  echo "  srctype=debug (include non-obfuscated debug versions of scripts when possible)"
  echo "  superdevmode=true (load widgets in superdevmode)"
  echo "    set widget title bar refresh button to recompile source from local codeServer)"
  echo "  useintentsmap=true (don't use OWF intents, rather use the intentsMap.js in the"
  echo "    widget descriptor directory to send intents to specific widgets, to avoid"
  echo "    laborious widget intent configuration when only one widget is responding to"
  echo "    a given event)"
  echo "  logenabled=true (enable OWF log4javascript, which may pop up a window as needed)"
  echo
  echo "Examples:"
  echo "  $0 https://raconteur.combat.mil:8443/sapphire-jboss/" combatwidgets/ '?useintentsmap=true'
  echo "  $0 http://localhost:8888/ debugwidgets/ '?srctype=debug\\&superdevmode=true\\&logenabled=true'"
  echo
  echo "  ** Important: Note the backslash in front of the ampersand (\\&) in the second"
  echo "                example. These parameters are sed regex replacement strings,"
  echo "                and \"&\" is a special character that must be escaped."
  exit 1
fi

appUrlPrefix=$1
descriptorDir=$2
appParams=$3

if [ -z "$appParams" ]; then
  appParams="?intentsMap=${descriptorDir}intentsMap.js"
else
  appParams=${appParams}"\\&intentsMap=${descriptorDir}intentsMap.js"
fi
   
rm -rf /tmp/widgetdescriptors
mkdir /tmp/widgetdescriptors
cd /tmp/widgetdescriptors
sed -e "s*%appUrlPrefix%*$appUrlPrefix*g;s*%appParams%*$appParams*g" << EOF > DocumentTree.html
<html>
<script type="text/javascript">
var JSON;JSON||(JSON={});
(function(){function k(a){return a<10?"0"+a:a}function o(a){p.lastIndex=0;return p.test(a)?'"'+a.replace(p,function(a){var c=r[a];return typeof c==="string"?c:"\\u"+("0000"+a.charCodeAt(0).toString(16)).slice(-4)})+'"':'"'+a+'"'}function l(a,j){var c,d,h,m,g=e,f,b=j[a];b&&typeof b==="object"&&typeof b.toJSON==="function"&&(b=b.toJSON(a));typeof i==="function"&&(b=i.call(j,a,b));switch(typeof b){case "string":return o(b);case "number":return isFinite(b)?String(b):"null";case "boolean":case "null":return String(b);case "object":if(!b)return"null";
e+=n;f=[];if(Object.prototype.toString.apply(b)==="[object Array]"){m=b.length;for(c=0;c<m;c+=1)f[c]=l(c,b)||"null";h=f.length===0?"[]":e?"[\n"+e+f.join(",\n"+e)+"\n"+g+"]":"["+f.join(",")+"]";e=g;return h}if(i&&typeof i==="object"){m=i.length;for(c=0;c<m;c+=1)typeof i[c]==="string"&&(d=i[c],(h=l(d,b))&&f.push(o(d)+(e?": ":":")+h))}else for(d in b)Object.prototype.hasOwnProperty.call(b,d)&&(h=l(d,b))&&f.push(o(d)+(e?": ":":")+h);h=f.length===0?"{}":e?"{\n"+e+f.join(",\n"+e)+"\n"+g+"}":"{"+f.join(",")+
"}";e=g;return h}}if(typeof Date.prototype.toJSON!=="function")Date.prototype.toJSON=function(){return isFinite(this.valueOf())?this.getUTCFullYear()+"-"+k(this.getUTCMonth()+1)+"-"+k(this.getUTCDate())+"T"+k(this.getUTCHours())+":"+k(this.getUTCMinutes())+":"+k(this.getUTCSeconds())+"Z":null},String.prototype.toJSON=Number.prototype.toJSON=Boolean.prototype.toJSON=function(){return this.valueOf()};var q=/[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
p=/[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,e,n,r={"\u0008":"\\b","\t":"\\t","\n":"\\n","\u000c":"\\f","\r":"\\r",'"':'\\"',"\\":"\\\\"},i;if(typeof JSON.stringify!=="function")JSON.stringify=function(a,j,c){var d;n=e="";if(typeof c==="number")for(d=0;d<c;d+=1)n+=" ";else typeof c==="string"&&(n=c);if((i=j)&&typeof j!=="function"&&(typeof j!=="object"||typeof j.length!=="number"))throw Error("JSON.stringify");return l("",
{"":a})};if(typeof JSON.parse!=="function")JSON.parse=function(a,e){function c(a,d){var g,f,b=a[d];if(b&&typeof b==="object")for(g in b)Object.prototype.hasOwnProperty.call(b,g)&&(f=c(b,g),f!==void 0?b[g]=f:delete b[g]);return e.call(a,d,b)}var d,a=String(a);q.lastIndex=0;q.test(a)&&(a=a.replace(q,function(a){return"\\u"+("0000"+a.charCodeAt(0).toString(16)).slice(-4)}));if(/^[\],:{}\s]*$/.test(a.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g,"@").replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,
"]").replace(/(?:^|:|,)(?:\s*\[)+/g,"")))return d=eval("("+a+")"),typeof e==="function"?c({"":d},""):d;throw new SyntaxError("JSON.parse");}})();
</script>
<script id="owfTransport" type="text/javascript">
var data = {
	"_widgetName": "DocumentTree",
	"widgetUrl": "%appUrlPrefix%owfGwtWidget.html#DocumentTree%appParams%",
	"imageUrlLarge": "%appUrlPrefix%owfWidgets/icons/DocumentTree128.png",
	"imageUrlSmall": "%appUrlPrefix%owfWidgets/icons/DocumentTree128.png",
	"universalName": "DocumentTree.widgets.owfgwttest.gwt.ozone",
	"displayName": "Document Tree",
	"height": 468,
	"width": 200,
	"visible": true,
	"singleton": false,
	"background": false,
	"widgetTypes": [
		"standard"
	],
	"intents": {
		"receive": []
	}
};
    
    var jsonData = JSON.stringify({
        status:200,
        data:data
    });
    window.name = jsonData;
</script>
    <body>
        <h3>window.name Transport</h3>
        Value in window.name is <span id="windowNameData" style="font-weight:bold"></span><br/>
        HTTP Status Code is <span style="font-weight:bold">200</span>
    </body>
</html>
EOF
# "EOF" line cannot have leading or trailing spaces. This ends the "sed" command.
sed -e "s*%appUrlPrefix%*$appUrlPrefix*g;s*%appParams%*$appParams*g" << EOF > TableForm.html
<html>
<script type="text/javascript">
var JSON;JSON||(JSON={});
(function(){function k(a){return a<10?"0"+a:a}function o(a){p.lastIndex=0;return p.test(a)?'"'+a.replace(p,function(a){var c=r[a];return typeof c==="string"?c:"\\u"+("0000"+a.charCodeAt(0).toString(16)).slice(-4)})+'"':'"'+a+'"'}function l(a,j){var c,d,h,m,g=e,f,b=j[a];b&&typeof b==="object"&&typeof b.toJSON==="function"&&(b=b.toJSON(a));typeof i==="function"&&(b=i.call(j,a,b));switch(typeof b){case "string":return o(b);case "number":return isFinite(b)?String(b):"null";case "boolean":case "null":return String(b);case "object":if(!b)return"null";
e+=n;f=[];if(Object.prototype.toString.apply(b)==="[object Array]"){m=b.length;for(c=0;c<m;c+=1)f[c]=l(c,b)||"null";h=f.length===0?"[]":e?"[\n"+e+f.join(",\n"+e)+"\n"+g+"]":"["+f.join(",")+"]";e=g;return h}if(i&&typeof i==="object"){m=i.length;for(c=0;c<m;c+=1)typeof i[c]==="string"&&(d=i[c],(h=l(d,b))&&f.push(o(d)+(e?": ":":")+h))}else for(d in b)Object.prototype.hasOwnProperty.call(b,d)&&(h=l(d,b))&&f.push(o(d)+(e?": ":":")+h);h=f.length===0?"{}":e?"{\n"+e+f.join(",\n"+e)+"\n"+g+"}":"{"+f.join(",")+
"}";e=g;return h}}if(typeof Date.prototype.toJSON!=="function")Date.prototype.toJSON=function(){return isFinite(this.valueOf())?this.getUTCFullYear()+"-"+k(this.getUTCMonth()+1)+"-"+k(this.getUTCDate())+"T"+k(this.getUTCHours())+":"+k(this.getUTCMinutes())+":"+k(this.getUTCSeconds())+"Z":null},String.prototype.toJSON=Number.prototype.toJSON=Boolean.prototype.toJSON=function(){return this.valueOf()};var q=/[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
p=/[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,e,n,r={"\u0008":"\\b","\t":"\\t","\n":"\\n","\u000c":"\\f","\r":"\\r",'"':'\\"',"\\":"\\\\"},i;if(typeof JSON.stringify!=="function")JSON.stringify=function(a,j,c){var d;n=e="";if(typeof c==="number")for(d=0;d<c;d+=1)n+=" ";else typeof c==="string"&&(n=c);if((i=j)&&typeof j!=="function"&&(typeof j!=="object"||typeof j.length!=="number"))throw Error("JSON.stringify");return l("",
{"":a})};if(typeof JSON.parse!=="function")JSON.parse=function(a,e){function c(a,d){var g,f,b=a[d];if(b&&typeof b==="object")for(g in b)Object.prototype.hasOwnProperty.call(b,g)&&(f=c(b,g),f!==void 0?b[g]=f:delete b[g]);return e.call(a,d,b)}var d,a=String(a);q.lastIndex=0;q.test(a)&&(a=a.replace(q,function(a){return"\\u"+("0000"+a.charCodeAt(0).toString(16)).slice(-4)}));if(/^[\],:{}\s]*$/.test(a.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g,"@").replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,
"]").replace(/(?:^|:|,)(?:\s*\[)+/g,"")))return d=eval("("+a+")"),typeof e==="function"?c({"":d},""):d;throw new SyntaxError("JSON.parse");}})();
</script>
<script id="owfTransport" type="text/javascript">
var data = {
	"_widgetName": "TableForm",
	"widgetUrl": "%appUrlPrefix%owfGwtWidget.html#TableForm%appParams%",
	"imageUrlLarge": "%appUrlPrefix%owfWidgets/icons/TableForm128.png",
	"imageUrlSmall": "%appUrlPrefix%owfWidgets/icons/TableForm128.png",
	"universalName": "TableForm.widgets.owfgwttest.gwt.ozone",
	"displayName": "Table Form",
	"height": 200,
	"width": 595,
	"visible": true,
	"singleton": false,
	"background": false,
	"widgetTypes": [
		"standard"
	],
	"intents": {
		"receive": []
	}
};
    
    var jsonData = JSON.stringify({
        status:200,
        data:data
    });
    window.name = jsonData;
</script>
    <body>
        <h3>window.name Transport</h3>
        Value in window.name is <span id="windowNameData" style="font-weight:bold"></span><br/>
        HTTP Status Code is <span style="font-weight:bold">200</span>
    </body>
</html>
EOF
# "EOF" line cannot have leading or trailing spaces. This ends the "sed" command.
sed -e "s*%appUrlPrefix%*$appUrlPrefix*g;s*%appParams%*$appParams*g" << EOF > WebForm.html
<html>
<script type="text/javascript">
var JSON;JSON||(JSON={});
(function(){function k(a){return a<10?"0"+a:a}function o(a){p.lastIndex=0;return p.test(a)?'"'+a.replace(p,function(a){var c=r[a];return typeof c==="string"?c:"\\u"+("0000"+a.charCodeAt(0).toString(16)).slice(-4)})+'"':'"'+a+'"'}function l(a,j){var c,d,h,m,g=e,f,b=j[a];b&&typeof b==="object"&&typeof b.toJSON==="function"&&(b=b.toJSON(a));typeof i==="function"&&(b=i.call(j,a,b));switch(typeof b){case "string":return o(b);case "number":return isFinite(b)?String(b):"null";case "boolean":case "null":return String(b);case "object":if(!b)return"null";
e+=n;f=[];if(Object.prototype.toString.apply(b)==="[object Array]"){m=b.length;for(c=0;c<m;c+=1)f[c]=l(c,b)||"null";h=f.length===0?"[]":e?"[\n"+e+f.join(",\n"+e)+"\n"+g+"]":"["+f.join(",")+"]";e=g;return h}if(i&&typeof i==="object"){m=i.length;for(c=0;c<m;c+=1)typeof i[c]==="string"&&(d=i[c],(h=l(d,b))&&f.push(o(d)+(e?": ":":")+h))}else for(d in b)Object.prototype.hasOwnProperty.call(b,d)&&(h=l(d,b))&&f.push(o(d)+(e?": ":":")+h);h=f.length===0?"{}":e?"{\n"+e+f.join(",\n"+e)+"\n"+g+"}":"{"+f.join(",")+
"}";e=g;return h}}if(typeof Date.prototype.toJSON!=="function")Date.prototype.toJSON=function(){return isFinite(this.valueOf())?this.getUTCFullYear()+"-"+k(this.getUTCMonth()+1)+"-"+k(this.getUTCDate())+"T"+k(this.getUTCHours())+":"+k(this.getUTCMinutes())+":"+k(this.getUTCSeconds())+"Z":null},String.prototype.toJSON=Number.prototype.toJSON=Boolean.prototype.toJSON=function(){return this.valueOf()};var q=/[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
p=/[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,e,n,r={"\u0008":"\\b","\t":"\\t","\n":"\\n","\u000c":"\\f","\r":"\\r",'"':'\\"',"\\":"\\\\"},i;if(typeof JSON.stringify!=="function")JSON.stringify=function(a,j,c){var d;n=e="";if(typeof c==="number")for(d=0;d<c;d+=1)n+=" ";else typeof c==="string"&&(n=c);if((i=j)&&typeof j!=="function"&&(typeof j!=="object"||typeof j.length!=="number"))throw Error("JSON.stringify");return l("",
{"":a})};if(typeof JSON.parse!=="function")JSON.parse=function(a,e){function c(a,d){var g,f,b=a[d];if(b&&typeof b==="object")for(g in b)Object.prototype.hasOwnProperty.call(b,g)&&(f=c(b,g),f!==void 0?b[g]=f:delete b[g]);return e.call(a,d,b)}var d,a=String(a);q.lastIndex=0;q.test(a)&&(a=a.replace(q,function(a){return"\\u"+("0000"+a.charCodeAt(0).toString(16)).slice(-4)}));if(/^[\],:{}\s]*$/.test(a.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g,"@").replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,
"]").replace(/(?:^|:|,)(?:\s*\[)+/g,"")))return d=eval("("+a+")"),typeof e==="function"?c({"":d},""):d;throw new SyntaxError("JSON.parse");}})();
</script>
<script id="owfTransport" type="text/javascript">
var data = {
	"_widgetName": "WebForm",
	"widgetUrl": "%appUrlPrefix%owfGwtWidget.html#WebForm%appParams%",
	"imageUrlLarge": "%appUrlPrefix%owfWidgets/icons/WebForm128.png",
	"imageUrlSmall": "%appUrlPrefix%owfWidgets/icons/WebForm128.png",
	"universalName": "WebForm.widgets.owfgwttest.gwt.ozone",
	"displayName": "Web Form",
	"height": 200,
	"width": 585,
	"visible": true,
	"singleton": false,
	"background": false,
	"widgetTypes": [
		"standard"
	],
	"intents": {
		"receive": []
	}
};
    
    var jsonData = JSON.stringify({
        status:200,
        data:data
    });
    window.name = jsonData;
</script>
    <body>
        <h3>window.name Transport</h3>
        Value in window.name is <span id="windowNameData" style="font-weight:bold"></span><br/>
        HTTP Status Code is <span style="font-weight:bold">200</span>
    </body>
</html>
EOF
# "EOF" line cannot have leading or trailing spaces. This ends the "sed" command.
sed -e "s*%appUrlPrefix%*$appUrlPrefix*g;s*%appParams%*$appParams*g" << EOF > TableView.html
<html>
<script type="text/javascript">
var JSON;JSON||(JSON={});
(function(){function k(a){return a<10?"0"+a:a}function o(a){p.lastIndex=0;return p.test(a)?'"'+a.replace(p,function(a){var c=r[a];return typeof c==="string"?c:"\\u"+("0000"+a.charCodeAt(0).toString(16)).slice(-4)})+'"':'"'+a+'"'}function l(a,j){var c,d,h,m,g=e,f,b=j[a];b&&typeof b==="object"&&typeof b.toJSON==="function"&&(b=b.toJSON(a));typeof i==="function"&&(b=i.call(j,a,b));switch(typeof b){case "string":return o(b);case "number":return isFinite(b)?String(b):"null";case "boolean":case "null":return String(b);case "object":if(!b)return"null";
e+=n;f=[];if(Object.prototype.toString.apply(b)==="[object Array]"){m=b.length;for(c=0;c<m;c+=1)f[c]=l(c,b)||"null";h=f.length===0?"[]":e?"[\n"+e+f.join(",\n"+e)+"\n"+g+"]":"["+f.join(",")+"]";e=g;return h}if(i&&typeof i==="object"){m=i.length;for(c=0;c<m;c+=1)typeof i[c]==="string"&&(d=i[c],(h=l(d,b))&&f.push(o(d)+(e?": ":":")+h))}else for(d in b)Object.prototype.hasOwnProperty.call(b,d)&&(h=l(d,b))&&f.push(o(d)+(e?": ":":")+h);h=f.length===0?"{}":e?"{\n"+e+f.join(",\n"+e)+"\n"+g+"}":"{"+f.join(",")+
"}";e=g;return h}}if(typeof Date.prototype.toJSON!=="function")Date.prototype.toJSON=function(){return isFinite(this.valueOf())?this.getUTCFullYear()+"-"+k(this.getUTCMonth()+1)+"-"+k(this.getUTCDate())+"T"+k(this.getUTCHours())+":"+k(this.getUTCMinutes())+":"+k(this.getUTCSeconds())+"Z":null},String.prototype.toJSON=Number.prototype.toJSON=Boolean.prototype.toJSON=function(){return this.valueOf()};var q=/[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
p=/[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,e,n,r={"\u0008":"\\b","\t":"\\t","\n":"\\n","\u000c":"\\f","\r":"\\r",'"':'\\"',"\\":"\\\\"},i;if(typeof JSON.stringify!=="function")JSON.stringify=function(a,j,c){var d;n=e="";if(typeof c==="number")for(d=0;d<c;d+=1)n+=" ";else typeof c==="string"&&(n=c);if((i=j)&&typeof j!=="function"&&(typeof j!=="object"||typeof j.length!=="number"))throw Error("JSON.stringify");return l("",
{"":a})};if(typeof JSON.parse!=="function")JSON.parse=function(a,e){function c(a,d){var g,f,b=a[d];if(b&&typeof b==="object")for(g in b)Object.prototype.hasOwnProperty.call(b,g)&&(f=c(b,g),f!==void 0?b[g]=f:delete b[g]);return e.call(a,d,b)}var d,a=String(a);q.lastIndex=0;q.test(a)&&(a=a.replace(q,function(a){return"\\u"+("0000"+a.charCodeAt(0).toString(16)).slice(-4)}));if(/^[\],:{}\s]*$/.test(a.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g,"@").replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,
"]").replace(/(?:^|:|,)(?:\s*\[)+/g,"")))return d=eval("("+a+")"),typeof e==="function"?c({"":d},""):d;throw new SyntaxError("JSON.parse");}})();
</script>
<script id="owfTransport" type="text/javascript">
var data = {
	"_widgetName": "TableView",
	"widgetUrl": "%appUrlPrefix%owfGwtWidget.html#TableView%appParams%",
	"imageUrlLarge": "%appUrlPrefix%owfWidgets/icons/TableView128.png",
	"imageUrlSmall": "%appUrlPrefix%owfWidgets/icons/TableView128.png",
	"universalName": "TableView.widgets.owfgwttest.gwt.ozone",
	"displayName": "Table View",
	"height": 460,
	"width": 478,
	"visible": true,
	"singleton": false,
	"background": false,
	"widgetTypes": [
		"standard"
	],
	"intents": {
		"receive": []
	}
};
    
    var jsonData = JSON.stringify({
        status:200,
        data:data
    });
    window.name = jsonData;
</script>
    <body>
        <h3>window.name Transport</h3>
        Value in window.name is <span id="windowNameData" style="font-weight:bold"></span><br/>
        HTTP Status Code is <span style="font-weight:bold">200</span>
    </body>
</html>
EOF
# "EOF" line cannot have leading or trailing spaces. This ends the "sed" command.
sed -e "s*%appUrlPrefix%*$appUrlPrefix*g;s*%appParams%*$appParams*g" << EOF > WebView.html
<html>
<script type="text/javascript">
var JSON;JSON||(JSON={});
(function(){function k(a){return a<10?"0"+a:a}function o(a){p.lastIndex=0;return p.test(a)?'"'+a.replace(p,function(a){var c=r[a];return typeof c==="string"?c:"\\u"+("0000"+a.charCodeAt(0).toString(16)).slice(-4)})+'"':'"'+a+'"'}function l(a,j){var c,d,h,m,g=e,f,b=j[a];b&&typeof b==="object"&&typeof b.toJSON==="function"&&(b=b.toJSON(a));typeof i==="function"&&(b=i.call(j,a,b));switch(typeof b){case "string":return o(b);case "number":return isFinite(b)?String(b):"null";case "boolean":case "null":return String(b);case "object":if(!b)return"null";
e+=n;f=[];if(Object.prototype.toString.apply(b)==="[object Array]"){m=b.length;for(c=0;c<m;c+=1)f[c]=l(c,b)||"null";h=f.length===0?"[]":e?"[\n"+e+f.join(",\n"+e)+"\n"+g+"]":"["+f.join(",")+"]";e=g;return h}if(i&&typeof i==="object"){m=i.length;for(c=0;c<m;c+=1)typeof i[c]==="string"&&(d=i[c],(h=l(d,b))&&f.push(o(d)+(e?": ":":")+h))}else for(d in b)Object.prototype.hasOwnProperty.call(b,d)&&(h=l(d,b))&&f.push(o(d)+(e?": ":":")+h);h=f.length===0?"{}":e?"{\n"+e+f.join(",\n"+e)+"\n"+g+"}":"{"+f.join(",")+
"}";e=g;return h}}if(typeof Date.prototype.toJSON!=="function")Date.prototype.toJSON=function(){return isFinite(this.valueOf())?this.getUTCFullYear()+"-"+k(this.getUTCMonth()+1)+"-"+k(this.getUTCDate())+"T"+k(this.getUTCHours())+":"+k(this.getUTCMinutes())+":"+k(this.getUTCSeconds())+"Z":null},String.prototype.toJSON=Number.prototype.toJSON=Boolean.prototype.toJSON=function(){return this.valueOf()};var q=/[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
p=/[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,e,n,r={"\u0008":"\\b","\t":"\\t","\n":"\\n","\u000c":"\\f","\r":"\\r",'"':'\\"',"\\":"\\\\"},i;if(typeof JSON.stringify!=="function")JSON.stringify=function(a,j,c){var d;n=e="";if(typeof c==="number")for(d=0;d<c;d+=1)n+=" ";else typeof c==="string"&&(n=c);if((i=j)&&typeof j!=="function"&&(typeof j!=="object"||typeof j.length!=="number"))throw Error("JSON.stringify");return l("",
{"":a})};if(typeof JSON.parse!=="function")JSON.parse=function(a,e){function c(a,d){var g,f,b=a[d];if(b&&typeof b==="object")for(g in b)Object.prototype.hasOwnProperty.call(b,g)&&(f=c(b,g),f!==void 0?b[g]=f:delete b[g]);return e.call(a,d,b)}var d,a=String(a);q.lastIndex=0;q.test(a)&&(a=a.replace(q,function(a){return"\\u"+("0000"+a.charCodeAt(0).toString(16)).slice(-4)}));if(/^[\],:{}\s]*$/.test(a.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g,"@").replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,
"]").replace(/(?:^|:|,)(?:\s*\[)+/g,"")))return d=eval("("+a+")"),typeof e==="function"?c({"":d},""):d;throw new SyntaxError("JSON.parse");}})();
</script>
<script id="owfTransport" type="text/javascript">
var data = {
	"_widgetName": "WebView",
	"widgetUrl": "%appUrlPrefix%owfGwtWidget.html#WebView%appParams%",
	"imageUrlLarge": "%appUrlPrefix%owfWidgets/icons/WebView128.png",
	"imageUrlSmall": "%appUrlPrefix%owfWidgets/icons/WebView128.png",
	"universalName": "WebView.widgets.owfgwttest.gwt.ozone",
	"displayName": "Web View",
	"height": 450,
	"width": 468,
	"visible": true,
	"singleton": false,
	"background": false,
	"widgetTypes": [
		"standard"
	],
	"intents": {
		"receive": []
	}
};
    
    var jsonData = JSON.stringify({
        status:200,
        data:data
    });
    window.name = jsonData;
</script>
    <body>
        <h3>window.name Transport</h3>
        Value in window.name is <span id="windowNameData" style="font-weight:bold"></span><br/>
        HTTP Status Code is <span style="font-weight:bold">200</span>
    </body>
</html>
EOF
# "EOF" line cannot have leading or trailing spaces. This ends the "sed" command.
cat << EOF > intentsMap.js
var __OwfGwtIntentsMap = {};
EOF


ls -1 /tmp/widgetdescriptors/ | sed "/paths.txt/d;s*^*${appUrlPrefix}${descriptorDir}*" > /tmp/widgetdescriptors/paths.txt

echo
echo "# cat /tmp/widgetdescriptors/paths.txt"
cat /tmp/widgetdescriptors/paths.txt
echo

echo "# ls /tmp/widgetdescriptors"
ls /tmp/widgetdescriptors
echo
echo "Now move these files into your application's webapp folders or some"
echo "other location that your destination OWF container will be able to find."

############################ end of script ############################
