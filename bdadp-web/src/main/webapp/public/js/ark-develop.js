/**
 * Created by labo on 2016/8/26.
 */

define(['js/scene-develop/scene-develop', 'js/scene-develop/add-scene-new',
        'js/scene-develop/add-package', 'js/modeldialog', 'js/scene-develop/scene-drag'],
    function (ScenePictrue, Scene, ScenePackage, Dialog) {
        'use strict';
        return function () {
        function _start() {
            var addScene = new Scene.Scene();
            var scenePackage = new ScenePackage.ScenePackages();
            addScene.initAllScene();                           //init all the scene
            scenePackage.initAllScenePackage();              //init all the scene package
            scenePackage.searchScenePackage();               //search the scenepackage
            ScenePictrue.sceneInit();               //init the toolbar and addPackage
            SceneDarg.dragInit(Dialog);            // drag and drop init
        }
        return {
            start: _start
        }
        }
    })


