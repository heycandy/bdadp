/**labo    依赖bootstrap.min.css、bootstrap-table.css、jquery.min.js、bootstrap.min.js、bootstrap-table.js、tableExport.js、bootstrap-table-export.js
 * [表格的创建]
 * @param {[String]} Id        [创建表格的id]
 * @param {[Array]} arrayHead  [表头]
 * @param {[Array]} data       [表的数据]
 */
define([], function () {
    function BdadpTable(Id, tablePropObj, arrayHead, data, boolcheck) {
        this._id = Id;
        this._tablePropObj = tablePropObj;
        this._arrayHead = arrayHead;
        this._data = data || [];
        this._boolcheck = boolcheck || false;
    }

    BdadpTable.prototype.creatTable = function () {
        _this = this;
        this._th = "";
        this._html = "";
        //table property
        this._head = '<table id="' + this._id + '"';
        for (var tableProp in _this._tablePropObj) {
            this._head += tableProp + "='" + _this._tablePropObj[tableProp] + "' ";
        }
        this._head += ">";
        //table head property
        var _arrayHead = this._arrayHead;
        for (var i = 0; i < _arrayHead.dataField.length; i++) {
            var _specialOperate = "";
            for (var attr in _arrayHead.events[i]) {
                _specialOperate += attr + "='" + _arrayHead.events[i][attr] + "' ";
            }
            _this._th +=
                "<th data-align='left' " + _specialOperate + "data-field='"
                + _arrayHead.dataField[i] + "'>" + _arrayHead.headText[i] + "</th>";
        }
        if (_this._boolcheck) {
            this._html +=
                this._head + "<thead>"
                + "<tr style='display:none;'><th data-field='state' data-checkbox='true' data-formatter='checkFormatter' data-align='center'></th>"
                + _this._th + "</tr></thead></table>";
        } else {
            this._html +=
                this._head + "<thead>" + "<tr style='display:none;'>" + _this._th
                + "</tr></thead></table>";
        }
        return this._html;
    }

    BdadpTable.prototype.delete = function (valueTarget) {
        var $table = $("#" + this._id);
        $table.bootstrapTable('remove', {
            field: valueTarget[0],
            values: [valueTarget[1]]
        });
    };

    BdadpTable.prototype.refresh = function () {
        $("#" + this._id).bootstrapTable('refresh');
    };
    BdadpTable.prototype.addRow = function (rowIndex, rowVal) {
        $("#" + this._id).bootstrapTable('insertRow', {
            index: rowIndex,
            row: rowVal
        });
    };

    BdadpTable.prototype.hiddenColumns = function (ArrayColumns) {

        for (var column in ArrayColumns) {
            $("#" + this._id).bootstrapTable('hideColumn', column);
        }
    }

    return {
        BdadpTable: BdadpTable
    }
})
