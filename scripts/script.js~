
//The REPL object.
var $;

//The main method providing an entry point for the Appium JAVA REPL scripting capabilities.
function main(repl){
    $ = repl;
    iteration();
    locators();
    appInfo();
    moreinfo();
    deviceActions();
}

//Access to REPL commands.
function iteration(){
  var elements = $.ids("android:id/text1");
  for each (element in elements) {
    element.click();
    $.back();
  }
}

//Some device actions through JADB.
function deviceActions(){
  var Device = $.device();
  Device.brightness(100);
  Device.call();
  Device.volumenUp();
  Device.volumenDown();

  print(Device.version());
  print(Device.vm());
  print(Device.dhcp());
  print(Device.gsm());
  print(Device.net());
}

function locators(){
  print($.className("android.widget.TextView").getText());
  print($.id("android:id/text1").getText());
  print($.xpath('//*[@text="NFC"]').getText());

  //List
  print($.ids("android:id/text1").size());
  print($.classNames("android.widget.TextView").size());
  print($.xpaths('//*[@text="NFC"]').size());

  //UISelector
  print($.text("App"));
  print($.texts("App").size());
  print($.textContain("Te"));
  print($.textContains("Te").size());
}

//Application under test info through APKInspector.
function appInfo(){
  print($.appInfo());
}

//Some Appium commands.
function moreinfo(){
  print($.activity());
  print($.orientation());
  print($.capabilities());
  print($.session());
  print($.strings());
  print($.source());
  print($.time());
  print($.context());
  print($.contextHandles());
  print($.sessionDetails());
}
