/**
 * Created by Hu on 2016/7/6.
 */
require([
  './config',
], function () {
  "use strict";
  require([
    './js/ark-sEdit',
    './js/ark-userManager',
     // './js/ark-scene'
  ], function (sceneEdit, userManager) {
    new sceneEdit().start();
    new userManager().start();
    // new arkScene().start();
    settings.interFun();
  });
});
