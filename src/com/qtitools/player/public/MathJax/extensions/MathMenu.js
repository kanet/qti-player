/*************************************************************
 *
 *  MathJax/extensions/MathMenu.js
 *  
 *  Implements a right-mouse (or CTRL-click) menu over mathematics
 *  elements that gives the user the ability to copy the source,
 *  change the math size, and zoom settings.
 *
 *  ---------------------------------------------------------------------
 *  
 *  Copyright (c) 2010 Design Science, Inc.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

(function (HUB,HTML,AJAX) {
  var CONFIG = HUB.Insert({
    delay: 400,                                    // the delay for submenus
    helpURL: "http://www.mathjax.org/Help-User/",  // the URL for the "MathJax Help" menu
    windowSettings: {                              // for source window
      status: "no", toolbar: "no", locationbar: "no", menubar: "no",
      directories: "no", personalbar: "no", resizable: "yes", scrollbars: "yes",
      width: 100, height: 50
    },
    
    styles: {
      ".MathJax_Menu": {
        position:"absolute", "background-color":"#F0F0F0", color:"black",
        width:"auto", padding:"2px", border:"1px solid", margin:0, cursor:"default",
        "font-family":"sans-serif", "font-size":"85%", "font-weight":"normal",
        "font-style":"normal", "text-align":"left", "text-indent":0, "text-transform":"none",
        "line-height":"normal", "letter-spacing":"normal", "word-spacing":"normal",
        "word-wrap":"none", "white-space":"nowrap", "float":"none",
        "box-shadow":"2px 2px 5px #AAAAAA",         // Opera 10.5
        "-webkit-box-shadow":"2px 2px 5px #AAAAAA", // Safari 3 and Chrome
        "-moz-box-shadow":"2px 2px 5px #AAAAAA",    // Forefox 3.5
        "-khtml-box-shadow":"2px 2px 5px #AAAAAA",  // Konqueror
        filter: "progid:DXImageTransform.Microsoft.dropshadow(OffX=2, OffY=2, Color='gray', Positive='true')" // IE
      },

      ".MathJax_MenuItem": {
        padding:"1px .5em",
        background:"transparent"
      },

      ".MathJax_MenuTitle": {
        "border-bottom":"1px solid black", "background-color":"#CCCCCC",
        margin:"-2px -2px 2px -2px",
        "text-align":"center", "font-style":"italic", "font-size":"80%", color:"#444444",
        padding:"1px 0", overflow:"hidden"
      },

      ".MathJax_MenuArrow": {
        position:"absolute", right:".4em",
        "font-family": (HUB.Browser.isMSIE ? "'Arial unicode MS'" : "")
      },

      ".MathJax_MenuCheck": {
        "font-family": (HUB.Browser.isMSIE ? "'Arial unicode MS'" : "")
      },

      ".MathJax_MenuRadioCheck": {
        "font-family": (HUB.Browser.isMSIE ? "'Arial unicode MS'" : "")
      },

      ".MathJax_MenuLabel": {
        color:"#787878",
        "font-style":"italic"
      },
    
      ".MathJax_MenuRule": {
        "border-top":"1px solid #AAAAAA",
        margin:"2px 3px"
      },
     
      ".MathJax_MenuDisabled": {
        color:"#AAAAAA"
      },
     
      ".MathJax_MenuActive": {
        "background-color": "#FCFCFC"
      }
    }
  },(HUB.config.MathMenu||{}));
  
  /*************************************************************/
  /*
   *  Cancel event's default action (try everything we can)
   */
  var FALSE = function (event) {
    if (!event) {event = window.event}
    if (event) {
      if (event.preventDefault) {event.preventDefault()}
      if (event.stopPropagation) {event.stopPropagation()}
      event.cancelBubble = true;
      event.returnValue = false;
    }
    return false;
  };

  /*************************************************************/
  /*
   *  The main menu class
   */
  var MENU = MathJax.Menu = MathJax.Object.Subclass({
    items: [],
    posted: false,
    title: null,
    margin: 0,

    Init: function (def) {this.items = [].slice.call(arguments,0)},
    With: function (def) {if (def) {HUB.Insert(this,def)}; return this},

    /*
     *  Display the menu
     */
    Post: function (event,parent) {
      if (!event) {event = window.event};
      var title = (!this.title ? null : [["div",{className: "MathJax_MenuTitle"},[this.title]]]);
      if (!MENU.div) {MENU.div = MENU.Background(this)}
      var menu = HTML.addElement(MENU.div,"div",{
        onmouseup: MENU.Remove,
        ondragstart: this.False, onselectstart: this.False, oncontextmenu: this.False,
        menuItem: this, className: "MathJax_Menu"
      },title);
      
      for (var i = 0, m = this.items.length; i < m; i++) {this.items[i].Create(menu)}
      this.posted = true;
      
      menu.style.width = (menu.offsetWidth+2) + "px";
      var x = document.body.scrollLeft, y = document.body.scrollTop, i = 0;
      if (!parent) {
        if (title) {y += -menu.childNodes[0].offsetHeight; i = 1}
        x += Math.floor(event.clientX - menu.offsetWidth/2 + 5);
        y += Math.floor(event.clientY - .67*menu.childNodes[i].offsetHeight);
        menu.childNodes[i].menuItem.Activate(menu.childNodes[i]);
        menu.childNodes[i].onmousemove = MENU.Mousemove;
        MENU.mouseMoved = 0;
      } else {
        x = parent.offsetWidth - 20; y =0;
        while (parent && parent !== MENU.div) {
          x += parent.offsetLeft; y += parent.offsetTop;
          parent = parent.parentNode;
        }
      }
      
      if (x + menu.offsetWidth > document.body.offsetWidth - this.margin)
        {x = document.body.offsetWidth - menu.offsetWidth - this.margin}
      menu.style.left = x+"px"; menu.style.top = y+"px";
      
      if (document.selection && document.selection.empty) {document.selection.empty()}
      return this.False(event);
    },

    /*
     *  Remove the menu from the screen
     */
    Remove: function (event,menu) {
      if (MENU.mouseMoved > 1) {
        if (MENU.div) {
          document.body.removeChild(MENU.div);
          delete MENU.div;
        }
      } else if (menu === MENU.div.firstChild) {
        MENU.mouseMoved = 2;
      }
    },

    False: FALSE
  },{
    div: null,     // the DOM elements for the menu and submenus
    mouseMoved: 0, // whether the mouse has moved after being posted

    Remove:    function (event) {MENU.Event(event,this,"Remove")},
    Mouseover: function (event) {MENU.Event(event,this,"Mouseover")},
    Mouseout:  function (event) {MENU.Event(event,this,"Mouseout")},
    Mousedown: function (event) {MENU.Event(event,this,"Mousedown")},
    Mouseup:   function (event) {MENU.Event(event,this,"Mouseup")},
    Mousemove: function (event) {MENU.Event(event,this,"Mousemove")},
    Event: function (event,menu,type) {
      if (!event) {event = window.event}
      var item = menu.menuItem;
      if (item && item[type]) {return item[type](event,menu)}
      return null;
    },

    /*
     *  Style for the background DIV
     */
    BGSTYLE: {
      position:"absolute", left:0, top:0,
      width:"100%", height:"100%",
      border:0, padding:0, margin:0
    },

    Background: function (menu) {
      var div = HTML.addElement(document.body,"div",{style:this.BGSTYLE},[["div",{
        style: this.BGSTYLE, menuItem: menu,
        onmousedown: this.Remove, onmouseup: this.Remove
      }]]);
      if (menu.msieBackgroundBug) {
        // MSIE doesn't allow transparent background to be hit boxes, so
        // fake it using opacity with solid background color
        div.firstChild.style.backgroundColor = "white";
        div.firstChild.style.filter = "alpha(opacity=0)";
      }
      return div;
    }

  });

  /*************************************************************/
  /*
   *  The menu item root subclass
   */
  var ITEM = MENU.ITEM = MathJax.Object.Subclass({
    name: "", // the menu item's label

    Create: function (menu) {
      var def = {
        onmouseover: MENU.Mouseover, onmouseout: MENU.Mouseout,
        onmouseup: MENU.Mouseup, onmousedown: this.False,
        className: "MathJax_MenuItem", menuItem: this
      };
      if (this.disabled) {def.className += " MathJax_MenuDisabled"}
      HTML.addElement(menu,"div",def,this.Label(def,menu));
    },

    Mouseover: function (event,menu) {
      if (!this.disabled) {this.Activate(menu)}
      if (!this.menu || !this.menu.posted) {
        var menus = MENU.div.childNodes, items = menu.parentNode.childNodes;
        for (var i = 0, m = items.length; i < m; i++) {
          var item = items[i].menuItem;
          if (item && item.menu && item.menu.posted) {item.Deactivate(items[i])}
        }
        m = menus.length-1;
        while (m >= 0 && menu.parentNode.menuItem !== menus[m].menuItem) {
          menus[m].menuItem.posted = false;
          menus[m].parentNode.removeChild(menus[m]);
          m--;
        }
        if (this.Timer) {this.Timer(event,menu)}
      }
    },
    Mouseout: function (event,menu) {
      if (!this.menu || !this.menu.posted) {this.Deactivate(menu)}
      if (this.timer) {clearTimeout(this.timer); delete this.timer}
      MENU.mouseMoved = 2;
    },
    Mousemove: function (event,menu) {
      MENU.mouseMoved++; if (MENU.mouseMoved > 1) {this.onmousemove = null}
    },
    Mouseup: function (event,menu) {return this.Remove(event,menu)},

    Remove: function (event,menu) {
      MENU.mouseMoved = 2;
      menu = menu.parentNode.menuItem;
      return menu.Remove(event,menu);
    },

    Activate: function (menu) {this.Deactivate(menu); menu.className += " MathJax_MenuActive"},
    Deactivate: function (menu) {menu.className = menu.className.replace(/ MathJax_MenuActive/,"")},

    With: function (def) {if (def) {HUB.Insert(this,def)}; return this},
    False: FALSE
  });

  /*************************************************************/
  /*
   *  A menu item that performs a command when selected
   */
  MENU.ITEM.COMMAND = MENU.ITEM.Subclass({
    action: function () {},

    Init: function (name,action,def) {
      this.name = name; this.action = action;
      this.With(def);
    },
    Label: function (def,menu) {return [this.name]},
    Mouseup: function (event,menu) {
      if (MENU.mouseMoved <= 1) {MENU.mouseMoved = 2}
        else {this.Remove(event,menu); this.action.call(this)}
      return this.False(event);
    }
  });

  /*************************************************************/
  /*
   *  A menu item that posts a submenu
   */
  MENU.ITEM.SUBMENU = MENU.ITEM.Subclass({
    menu: null,        // the submenu
    marker: "\u25B9",  // the menu arrow

    Init: function (name,def) {
      this.name = name; var i = 1;
      if (!(def instanceof MENU.ITEM)) {this.With(def), i++}
      this.menu = MENU.apply(MENU,[].slice.call(arguments,i));
    },
    Label: function (def,menu) {
      def.onmousemove = MENU.Mousemove; this.menu.posted = false;
      return [this.name+" ",["span",{className:"MathJax_MenuArrow"},[this.marker]]];
    },
    Timer: function (event,menu) {
      if (this.timer) {clearTimeout(this.timer)}
      event = {clientX: event.clientX, clientY: event.clientY}; // MSIE can't pass the event below
      this.timer = setTimeout(MathJax.CallBack(["Mouseup",this,event,menu]),500);
    },
    Mouseup: function (event,menu) {
      if (!this.menu.posted) {
        if (this.timer) {clearTimeout(this.timer); delete this.timer}
        this.menu.Post(event,menu);
      } else {
        var menus = MENU.div.childNodes, m = menus.length-1;
        while (m >= 0) {
          var child = menus[m];
          child.menuItem.posted = false;
          child.parentNode.removeChild(child);
          if (child.menuItem === this.menu) {break};
          m--;
        }
      }
      MENU.mouseMoved = 2;
      return this.False(event);
    },
    Mousemove: function (event,menu) {
      if (this.timer) {this.Timer(event,menu)} else {MENU.mouseMoved++}
    }
  });

  /*************************************************************/
  /*
   *  A menu item that is one of several radio buttons
   */
  MENU.ITEM.RADIO = MENU.ITEM.Subclass({
    variable: null,     // the variable location [object,attribute]
    marker: "\u2714",   // the checkmark

    Init: function (name,variable,def) {
      this.name = name; this.variable = variable; this.With(def);
      if (this.value == null) {this.value = this.name}
    },
    Label: function (def,menu) {
      var span = {className:"MathJax_MenuRadioCheck"};
      if (this.variable[0][this.variable[1]] !== this.value) {span = {style:{visibility:"hidden"}}}
      return [["span",span,[this.marker]]," "+this.name];
    },
    Mouseup: function (event,menu) {
      if (!this.disabled) {
        var child = menu.parentNode.childNodes;
        for (var i = 0, m = child.length; i < m; i++) {
          var item = child[i].menuItem;
          if (item && item.variable && item.variable[0] === this.variable[0] && 
              item.variable[1] === this.variable[1]) {
            child[i].firstChild.style.visibility = "hidden";
          }
        }
        menu.firstChild.visibility = ""; 
        this.variable[0][this.variable[1]] = this.value;
        if (this.action) {this.action.call(MENU)}
      }
      this.Remove(event,menu);
      return this.False(event);
    }
  });

  /*************************************************************/
  /*
   *  A menu item that is checkable
   */
  MENU.ITEM.CHECKBOX = MENU.ITEM.Subclass({
    variable: null,     // the variable location [object,attribute]
    marker: "\u2716",   // the checkmark

    Init: function (name,variable,def) {
      this.name = name; this.variable = variable; this.With(def);
    },
    Label: function (def,menu) {
      var span = {className:"MathJax_MenuCheck"};
      if (!this.variable[0][this.variable[1]]) {span = {style:{visibility:"hidden"}}}
      return [["span",span,[this.marker]]," "+this.name];
    },
    Mouseup: function (event,menu) {
      if (!this.disabled) {
        menu.firstChild.visibility = (this.variable[0][this.variable[1]] ? "hidden" : "");
        this.variable[0][this.variable[1]] = !this.variable[0][this.variable[1]];
        if (this.action) {this.action.call(MENU)}
      }
      this.Remove(event,menu);
      return this.False(event);
    }
  });

  /*************************************************************/
  /*
   *  A menu item that is a label
   */
  MENU.ITEM.LABEL = MENU.ITEM.Subclass({
    Init: function (name,def) {this.name = name; this.With(def)},
    Label: function (def,menu) {
      delete def.onmouseover, delete def.onmouseout; delete def.onmousedown;
      def.className += " MathJax_MenuLabel";
      return [this.name];
    }
  });

  /*************************************************************/
  /*
   *  A rule in a menu
   */
  MENU.ITEM.RULE = MENU.ITEM.Subclass({
    Label: function (def,menu) {
      delete def.onmouseover, delete def.onmouseout; delete def.onmousedown;
      def.className += " MathJax_MenuRule";
      return null;
    }
  });
  
  /*************************************************************/
  /*************************************************************/

  /*
   *  Handle the ABOUT box
   */
  MENU.About = function () {
    var HTMLCSS = MathJax.OutputJax["HTML-CSS"];
    var local = (HTMLCSS.webFonts ? "" : "local "), web = (HTMLCSS.webFonts ? " web" : "");
    var font = (HTMLCSS.imgFonts ? "Image" : local+HTMLCSS.fontInUse+web);
    var jax = [];
    MENU.About.GetJax(jax,MathJax.InputJax,"Input");
    MENU.About.GetJax(jax,MathJax.OutputJax,"Output");
    MENU.About.GetJax(jax,MathJax.ElementJax,"Element");
    MENU.About.div = MENU.Background(MENU.About);
    var about = MathJax.HTML.addElement(MENU.About.div,"div",{
      style:{
        position:"fixed", left:"50%", width:"auto", "text-align":"center",
        border:"2px outset", padding:"1em 2em", "background-color":"#DDDDDD",
        cursor: "default", "font-size":"90%",
        "box-shadow":"2px 2px 5px #AAAAAA",         // Opera 10.5
        "-webkit-box-shadow":"2px 2px 5px #AAAAAA", // Safari 3 and Chrome
        "-moz-box-shadow":"2px 2px 5px #AAAAAA",    // Forefox 3.5
        "-khtml-box-shadow":"2px 2px 5px #AAAAAA",  // Konqueror
        filter: "progid:DXImageTransform.Microsoft.dropshadow(OffX=2, OffY=2, Color='gray', Positive='true')" // IE
      },
      onclick: MENU.About.Remove
    },[
      ["b",{style:{fontSize:"120%"}},["MathJax"]]," v"+MathJax.version,["br"],
      "using "+font+" fonts",["br"],["br"],
      ["span",{style:{
        display:"inline-block", "text-align":"left", "font-size":"80%",
        "background-color":"#E4E4E4", padding:".4em .6em", border:"1px inset"
      }},jax],["br"],["br"],
      ["a",{href:"http://www.mathjax.org/"},["wwww.mathjax.org"]]
    ]);
    var doc = (document.documentElement||{});
    var H = window.innerHeight || doc.clientHeight || doc.scrollHeight || 0;
    if (MENU.prototype.msieAboutBug) {
      about.style.width = "15em"; about.style.position = "absolute";
      about.style.left = Math.floor((document.documentElement.scrollWidth - about.offsetWidth)/2)+"px";
      about.style.top = (Math.floor((H-about.offsetHeight)/3)+document.body.scrollTop)+"px";
    } else {
      about.style.marginLeft = Math.floor(-about.offsetWidth/2)+"px";
      about.style.top = Math.floor((H-about.offsetHeight)/3)+"px";
    }
  };
  MENU.About.Remove = function (event) {
    if (MENU.About.div) {document.body.removeChild(MENU.About.div); delete MENU.About.div}
  };
  MENU.About.GetJax = function (jax,JAX,type) {
    for (var id in JAX) {
      if (JAX[id].isa && JAX[id].isa(JAX))
        {jax.push(JAX[id].name+" "+type+" Jax v"+JAX[id].version,["br"])}
    }
    return jax;
  };
  
  /*
   *  Handle the MathJax HELP menu
   */
  MENU.Help = function () {
    window.open(CONFIG.helpURL,"MathJaxHelp");
  };
  
  /*
   *  Handle showing of element's source
   */
  MENU.ShowSource = function (event) {
    if (!event) {event = window.event}
    if (!MENU.jax) return;
    if (MENU.variables.format === "MathML") {
      var MML = MathJax.ElementJax.mml;
      if (MML && typeof(MML.mbase.prototype.toMathML) !== "undefined") {
        MENU.ShowSource.Text(MENU.jax.root.toMathML(),event);
      } else if (!MENU.loadingToMathML) {
        MENU.loadingToMathML = true;
        MENU.ShowSource.Window(event); // WeBKit needs to open window on click event
        MathJax.CallBack.Queue(
          AJAX.Require("[MathJax]/extensions/toMathML.js"),
          [this,arguments.callee,event]  // call this function again
        );
        return;
      }
    } else {
      if (MENU.jax.originalText == null) {alert("No TeX form available"); return}
      MENU.ShowSource.Text(MENU.jax.originalText,event);
    }
  };
  MENU.ShowSource.Window = function (event) {
    if (!MENU.ShowSource.w) {
      var def = [], DEF = CONFIG.windowSettings;
      for (var id in DEF) {if (DEF.hasOwnProperty(id)) {def.push(id+"="+DEF[id])}}
      MENU.ShowSource.w = window.open("","_blank",def.join(","));
    }
    return MENU.ShowSource.w;
  };
  MENU.ShowSource.Text = function (text,event) {
    var w = MENU.ShowSource.Window(event);
    text = text.replace(/^\s*/,"").replace(/\s*$/,"");
    text = text.replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;");
    w.document.open();
    w.document.write("<html><head><title>MathJax Equation Source</title></head><body style='font-size:85%'>");
    w.document.write("<table><tr><td><pre>"+text+"</pre></td></tr></table>");
    w.document.write("</body></html>"); w.document.close();
    var table = w.document.body.firstChild;
    var H = (w.outerHeight-w.innerHeight)||30, W = (w.outerWidth-w.innerWidth)||30;
    W = Math.min(Math.floor(.5*screen.width),table.offsetWidth+W+25);
    H = Math.min(Math.floor(.5*screen.height),table.offsetHeight+H+25);
    w.resizeTo(W,H);
    if (event && event.screenX != null) {
      var x = Math.max(0,Math.min(event.screenX-Math.floor(W/2), screen.width-W-20)),
          y = Math.max(0,Math.min(event.screenY-Math.floor(H/2), screen.height-H-20));
      w.moveTo(x,y);
    }
    delete MENU.ShowSource.w;
  };
  
  /*
   *  Handle rescaling all the math
   */
  MENU.Scale = function () {
    var HTMLCSS = MathJax.OutputJax["HTML-CSS"];
    var scale = prompt("Scale all mathematics (compared to surrounding text) by",
                        HTMLCSS.config.scale+"%");
    if (scale) {
      if (scale.match(/^\s*\d+\s*%?\s*$/)) {
        scale = parseInt(scale);
        if (scale) {
          if (scale !== HTMLCSS.config.scale) {
            HTMLCSS.config.scale = scale;
            HUB.Reprocess();
          }
        } else {alert("The scale should not be zero")}
      } else {alert("The scale should be a perentage (e.g., 120%)")}
    }
  };

  /*************************************************************/
  /*************************************************************/

  HUB.Browser.Select({
    MSIE: function (browser) {
      var quirks = (document.compatMode === "BackCompat");
      MENU.Augment({
        margin: 20,
        msieBackgroundBug: true,
        msieAboutBug: quirks
      });
    }
  });

  /*************************************************************/
  /*
   *  The values of the checkbox and radio menu items
   */
  MENU.variables = {
    format: (MathJax.InputJax.TeX ? "TeX" : "MathML"),
    zoom: "None",
    CTRL: false,
    ALT: true,
    CMD: false,
    Shift: false,
    zscale: "200%"
  };

  /*
   *  The main menu
   */
  MENU.menu = MENU(
    ITEM.COMMAND("Show Source",MENU.ShowSource),
    ITEM.SUBMENU("Format",
      ITEM.RADIO("MathML", [MENU.variables,"format"]),
      ITEM.RADIO("TeX",    [MENU.variables,"format"], {disabled: !MathJax.InputJax.TeX})
    ),
    ITEM.RULE(),
    /* 
     * ITEM.SUBMENU("Zoom",
     *   ITEM.RADIO("None",            [MENU.variables,"zoom"]),
     *   ITEM.RADIO("on Hover",        [MENU.variables,"zoom"]),
     *   ITEM.RADIO("on Click",        [MENU.variables,"zoom"]),
     *   ITEM.RADIO("on Double-Click", [MENU.variables,"zoom"]),
     *   ITEM.SUBMENU("When used with",
     *     ITEM.CHECKBOX("CTRL",       [MENU.variables,"CTRL"], {disabled: HUB.Browser.isMac}),
     *     ITEM.CHECKBOX("ALT/Option", [MENU.variables,"ALT"]),
     *     ITEM.CHECKBOX("CMD",        [MENU.variables,"CMD"], {disabled: !HUB.Browser.isMac}),
     *     ITEM.CHECKBOX("Shift",      [MENU.variables,"Shift"])
     *   ),
     *   ITEM.RULE(),
     *   ITEM.SUBMENU("Scaling Factor",
     *     ITEM.RADIO("125%",  [MENU.variables,"zscale"]),
     *     ITEM.RADIO("133%",  [MENU.variables,"zscale"]),
     *     ITEM.RADIO("150%",  [MENU.variables,"zscale"]),
     *     ITEM.RADIO("175%",  [MENU.variables,"zscale"]),
     *     ITEM.RADIO("200%",  [MENU.variables,"zscale"]),
     *     ITEM.RADIO("250%",  [MENU.variables,"zscale"]),
     *     ITEM.RADIO("300%",  [MENU.variables,"zscale"]),
     *     ITEM.RADIO("400%",  [MENU.variables,"zscale"])
     *   )
     * ),
     */
    ITEM.COMMAND("Scale...",MENU.Scale),
    ITEM.RULE(),
    ITEM.COMMAND("About MathJax \u00A0",MENU.About),
    ITEM.COMMAND("MathJax Help",MENU.Help)
  ).With({title:"MathJax Menu"});

  /*************************************************************/

  MathJax.CallBack.Queue(
    ["Styles",AJAX,CONFIG.styles],
    ["Post",HUB.Startup.signal,"MathMenu Ready"],
    ["loadComplete",AJAX,"[MathJax]/extensions/MathMenu.js"]
  );

})(MathJax.Hub,MathJax.HTML,MathJax.Ajax);
