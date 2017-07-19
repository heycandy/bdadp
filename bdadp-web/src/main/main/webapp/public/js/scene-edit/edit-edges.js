/**
 * Created by Hu on 2016/9/9.
 */
define([], function () {
    'use strict';
    return function () {

        //test data
        var vertices = [-1, -2, -3, -4];
        var edges = [{'from': -4, 'to': -1}, {'from': -1, 'to': -2}, {'from': -2, 'to': -3}];

        /**
         * DAG(directed acycline graph) algorithm
         * @param vertices
         * @param edges
         * @constructor
         */
        var TopologicalSort = function (vertices, edges) {
            this.vertices = vertices;
            this.edges = edges;
        }

        /**
         * [{'start':_start, 'to':_toVertex}] _toVertext is array
         * bulid the relation of each vertex
         * @returns {Array}
         */
        TopologicalSort.prototype.initStack = function (vertices, edges) {
            var aov = [];           // Activity on vertice
            for (var i = 0; i < vertices.length; i++) {
                var _start = vertices[i], _toVertex = [];
                for (var j = 0; j < edges.length; j++) {
                    if (_start == edges[j].from) {
                        _toVertex.push(edges[j].to);
                    }
                }
                aov.push({'start': _start, 'to': _toVertex});
            }
            return aov;
        }

        /**
         * @param vertices
         * @param edges
         * @param key
         * @returns {{key: *, is_top: boolean, is_terminal: boolean}}
         */
        TopologicalSort.prototype.isTopTermimal = function (key, _vertices, _edges) {
            var vertices, edges;
            if (typeof(vertices) == 'undefined') {
                vertices = this.vertices;
            } else {
                vertices = _vertices;
            }
            if (vertices.indexOf(key) == -1) {
                throw 'key is illgal';
            }
            edges = _edges || this.edges;
            if (edges && vertices) {
                var aov = this.initStack(vertices, edges);
                var is_top = true, is_terminal = false;
                for (var i = 0; i < aov.length; i++) {
                    if (aov[i].to.indexOf(key) != -1) {
                        is_top = false;
                    }  // the vertex in children
                                                                      // array ,not the top
                    if (aov[i].start == key) {
                        if (aov[i].to.length == 0) {  // the 'to' represent the children vertex array
                            is_terminal = true;
                        }
                    }
                }
                return {'key': key, 'is_top': is_top, 'is_terminal': is_terminal};
            } else {
                throw 'edges or vertices cannot be null.'
            }
        }

        /**
         *  the array of indegree [{'key':-1,'num',number}..]
         * @returns {Array}
         */
        TopologicalSort.prototype.indegree = function () {
            var indegree = [];
            for (var i = 0; i < this.vertices.length; i++) {
                var _key = this.vertices[i], num = 0;
                for (var j = 0; j < this.edges.length; j++) {
                    if (_key == this.edges[j].to) {
                        num++;
                    }
                }
                indegree.push({'key': _key, 'num': num})
            }
            return indegree;
        }

        TopologicalSort.prototype.findIndegree = function (indegree, key) {
            var num;
            for (var i = 0; i < indegree.length; i++) {
                if (indegree[i].key == key) {
                    num = indegree[i].num;
                    break;
                }
            }
            return num;
        }

        /**
         * verdict DAG
         * @returns {boolean}
         */
        TopologicalSort.prototype.isDAG = function () {
            //first: tranverse each vertex and find indegree is '0' of vertex
            var popNum = [], verticesMaxLength = this.vertices.length, that = this;
            var _indegree = this.indegree();    // init indegree  ~~~ [{'key':-1,'num',number}..]

            function isZero() {
                var flag = 0;
                for (var n = 0; n < _indegree.length; n++) {
                    if (_indegree[n].num == 0) {
                        flag = 1;
                        break;
                    }
                }
                return flag;
            }

            if (isZero() == 1) {  //exist indegree num = 0
                var ind = startGraph(_indegree, this.vertices, this.edges);
                if (ind.popKey.length == verticesMaxLength) {
                    return true;
                }
                return false;
            } else {
                return false;
            }

            function startGraph(_indegree, vertices, edges) {
                if (_indegree.length == 0) {
                    return {'indegree': _indegree, 'popKey': popNum};
                }  // the
                                                                                             // return
                                                                                             // of
                                                                                             // this
                                                                                             // inner
                                                                                             // function
                var thatIndegree = $.map(_indegree, function (obj) {
                    return $.extend(true, {}, obj);
                });
                var popKey = [];
                for (var i = 0; i < vertices.length; i++) {   //tranverse each vertex
                    var _indegreeNum = that.findIndegree(thatIndegree, vertices[i]);
                    if (0 == _indegreeNum) {
                        popNum.push({'key': vertices[i]});
                        popKey.push({'key': vertices[i]});
                        var _stack = that.initStack(vertices, edges);  // [{'start':_start,
                                                                       // 'to':_toVertex}] toVertex
                                                                       // is array
                        var _toVertex = (function (start) {
                            for (var s = 0; s < _stack.length; s++) {
                                if (start == _stack[s].start) {
                                    return _stack[s].to;
                                }
                            }
                        })(vertices[i]);

                        for (var t = 0; t < _toVertex.length; t++) {
                            //toVertex subtract 1 of indegree
                            for (var j = 0; j < _indegree.length; j++) {
                                if (_toVertex[t] == _indegree[j].key) {
                                    _indegree[j].num--;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (popKey.length == 0) {
                    return {'indegree': _indegree, 'popKey': popNum};
                }  // the
                                                                                          // return
                                                                                          // of
                                                                                          // this
                                                                                          // inner
                                                                                          // function
                for (var p = 0; p < popKey.length; p++) {
                    for (var i = 0; i < _indegree.length;) {     // get new indegree after delete vertex
                        if (popKey[p].key == _indegree[i].key) {
                            _indegree.splice(i, 1);  // remove indegree is '0'
                        } else {
                            i++;
                        }
                    }
                    for (var j = 0; j < vertices.length;) {  // get new vertices after delete vertex
                        if (popKey[p].key == vertices[j]) {
                            vertices.splice(j, 1);
                        } else {
                            j++;
                        }
                    }
                    for (var k = 0; k < edges.length;) {
                        if (popKey[p].key == edges[k].from) {
                            edges.splice(k, 1);
                        } else {
                            k++;
                        }
                    }
                }
                /*   console.log(_indegree);
                 console.log(vertices);
                 console.log(edges);*/
                if (_indegree.length >= 0) {
                    return startGraph(_indegree, vertices, edges);
                }
            }

        }

        function filterDAGJson(jsonString) {
            if (typeof(jsonString) == 'undefined') {
                return null;
            }
            var jsonObj = $.parseJSON(jsonString), vertices = [], edges = [];
            var nodeDataArray = jsonObj.nodeDataArray;
            var linkDataArray = jsonObj.linkDataArray;
            if (nodeDataArray instanceof Array) {
                for (var i = 0; i < nodeDataArray.length; i++) {
                    vertices.push(nodeDataArray[i].key)
                }
            }
            if (linkDataArray instanceof Array) {
                for (var j = 0; j < linkDataArray.length; j++) {
                    edges.push({'from': linkDataArray[j].from, 'to': linkDataArray[j].to});
                }
            }

            return {'vertices': vertices, 'edges': edges};
        }

        function _isDAG(jsonString) {
            var obj = filterDAGJson(jsonString), vertices, edges;
            if (obj) {
                vertices = obj.vertices;
                edges = obj.edges;
            }
            var _sort = new TopologicalSort(vertices, edges);
            var isDAG = _sort.isDAG();
            return isDAG;
        }

        function _isTT(vertices, edges, key) {
            var _sort = new TopologicalSort(vertices, edges);
            return _sort.isTopTermimal(key);
        }

        //test
        function _init() {
            var _sort = new TopologicalSort(vertices, edges);
            var isTT = _sort.isTopTermimal(-4);
            var isDAG = _sort.isDAG();
            console.log(isDAG);
            debugger;
        }

        return {
            //  init: _init,
            isDAG: _isDAG,
            isTT: _isTT,
        }
    }

})
