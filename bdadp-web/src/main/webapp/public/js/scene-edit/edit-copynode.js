/**
 * Created by Hu on 2016/11/29.
 */
define([], function () {
    'use strict';
    return function () {

        function CommandHandler() {

        }

        CommandHandler.prototype.copy = function (sceneId, srcDiagram) {
            // var formId = thatId + "_component_" + componentId=pid + "_form_" + thatKey
            var _sceneId = sceneId, that = this;
            srcDiagram.addDiagramListener("ClipboardChanged", function (e) {
                var _copiedNodes = [];
                e.subject.each(function (part) {
                    part.data.formId =
                        _sceneId + "_component_" + part.data.pid + "_form_" + part.data.key;
                    _copiedNodes.push(part.data);
                })

                that.formsData = $.map(_copiedNodes, function (node) {
                    var _formJsonData = $('#' + node.formId).serializeArray(),
                        _tabDescFromData = $('#' + 'tabDesc_' + node.formId).serializeArray();
                    return {'node': node, 'data': _formJsonData, 'description': _tabDescFromData}
                })
                return that;
            })
            return that;
        }

        CommandHandler.prototype.paster = function (sceneId, diagram, callback) {
            var that = this;
            diagram.addDiagramListener("ClipboardPasted", function (e) {
                var _pasternode = [], total = [], before = [];
                e.diagram.nodes.each(function (node) {
                    console.log(node.data);
                    total.push(node.data);
                })

                e.subject.each(function (node) {
                    _pasternode.push(node.data);
                })

                before = $.grep(total, function (element) {
                    var arr = $.grep(_pasternode, function (pnode) {
                        return pnode.formId == element.formId && pnode.key == element.key
                    })
                    return arr.length > 0 ? false : true
                })

                e.diagram.startTransaction("Copy New Node");
                e.subject.each(function (node) {
                    /* var newnode = diagram.findNodeForData(node.data);
                     newnode.data.location = new go.Point(node.location.x + 100, node.location.y);
                     newnode.data.text = nodeTextReName(node.data.text, diagram);*/
                    console.log(node.data);
                    diagram.model.setDataProperty(node.data, "text",
                                                  nodeTextReName(node.data.text, diagram, before));
                    diagram.model.setDataProperty(node.data, "location",
                                                  new go.Point(node.location.x + 100,
                                                               node.location.y));
                    before.push(node.data);
                })

                e.diagram.commitTransaction("Copy New Node");

                that.formsData = $.map(_pasternode, function (node) {
                    var componentId = node.pid, formId = '';
                    if(componentId == undefined)return;
                    if (componentId.indexOf('_') != -1) {
                        var _type = componentId.substring(componentId.indexOf('_') + 1);
                        if (_type == "scenario") {
                            // componentId = componentId.substring(0, componentId.indexOf('_'));
                            formId = $.grep(node.formId.split('_'), function (element) {
                                return element != "scenario"
                            }).join('_');
                        }
                    } else {
                        formId = node.formId;
                    }
                    var _formJsonData = $('#' + formId).serializeArray(),
                        _tabDescFromData = $('#' + 'tabDesc_' + formId).serializeArray();
                    return {
                        'node': node,
                        'data': _formJsonData,
                        'description': _tabDescFromData,
                        'pasternode': node
                    }
                })

                /*   if(that.formsData == undefined){    //cross scene node copy
                 that.formsData = $.map(_pasternode, function(node){
                 var componentId = node.pid, formId='';
                 if (componentId.indexOf('_') != -1) {
                 var _type = componentId.substring(componentId.indexOf('_') + 1);
                 if (_type == "scenario") {
                 // componentId = componentId.substring(0, componentId.indexOf('_'));
                 formId = $.grep(node.formId.split('_'), function(element){ return element != "scenario"  }).join('_');
                 }
                 }else{
                 formId = node.formId;
                 }
                 var _formJsonData = $('#' + formId).serializeArray(),
                 _tabDescFromData = $('#' + 'tabDesc_' + formId).serializeArray();
                 return {'node':node, 'data':_formJsonData, 'description':_tabDescFromData, 'pasternode':node}
                 })
                 }else{                              // same scene node copy
                 that.formsData = $.map(that.formsData, function(_f){
                 var _pasterF = {};
                 for(var i=0; i<_pasternode.length; i++){
                 if(_f.node.formId == _pasternode[i].formId){
                 _pasterF = _pasternode[i];
                 break;
                 }
                 }
                 return {'node':_f.node, 'data':_f.data, 'description':_f.description, 'pasternode':_pasterF}
                 })
                 }*/

                callback(that.formsData);
            })
        }

        function nodeTextReName(copyNodeName, _diagram, before) {
            var s1 = copyNodeName;
            var _nodes = [];
            /*_diagram.nodes.each(function(node){
             _nodes.push({'pid':node.data.pid, 'name':node.data.text});
             })*/
            before.forEach(function (node) {
                _nodes.push({'pid': node.pid, 'name': node.text});
            })
          if (_nodes.length == 0) {
            return s1;
          }

            if (_nodes.length > 0) {

                // sort and ordination
                var sortGroup = (function () {
                    var _result = [];
                    for (var i = 0; i < _nodes.length; i++) {
                        var start, arr = new Array();
                      if (_nodes[i] == undefined) {
                        continue;
                      }
                        if (_nodes[i].name && _nodes[i].name.indexOf('(') == -1) {
                            start = _nodes[i].name;
                            arr.push(start);
                        } else {
                            start = _nodes[i].name.substring(0, _nodes[i].name.indexOf('('));
                            arr.push(_nodes[i].name);
                        }
                      if (start == undefined) {
                        return;
                      }
                        for (var j = i + 1; j < _nodes.length; j++) {
                            if (_nodes[j] && _nodes[j].name && _nodes[j].name.indexOf(start) == 0) {
                                arr.push(_nodes[j].name);
                                delete _nodes[j];
                            }
                        }
                        _result[i] = new Array();
                        _result[i].push(arr); // array contains arr
                    }
                    return _result;
                })();

                // whether s1 exist in sortGroup ?
                var _exist = false;
                for (var s = 0; s < sortGroup.length; s++) {
                    var group = sortGroup[s];
                  if (group == undefined) {
                    continue;
                  }
                    if (group[0] && group[0].indexOf(s1) != -1) {  //if exist
                        _exist = true;
                        //add '(' character
                        var _numberArr = [];  //eg. [1,2,3..]
                        if (group[0] instanceof Array) {
                            group[0].forEach(function (element) {
                                if (element.indexOf('(') != -1) {
                                    _numberArr.push(parseInt(element.substr(element.indexOf('(')
                                                                            + 1, 1)));
                                }
                            })
                          if (_numberArr.length == 0) {
                            return group[0][0] + '(1)';
                          }
                            if (_numberArr.length > 0) {
                                var _max = Math.max.apply(null, _numberArr) + 1;
                                return group[0][0] + '(' + _max + ')';
                            }
                        }
                        break;
                    }
                }
              if (!_exist) {
                return s1;
              }
            }
        }

        function _init(sceneId, srcDiagram, callback) {
            new CommandHandler().copy(sceneId, srcDiagram).paster(sceneId, srcDiagram, callback);
        }

        return {
            init: _init
        }
    }

})
