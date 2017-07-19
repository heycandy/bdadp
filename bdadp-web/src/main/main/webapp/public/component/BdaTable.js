/**labo    依赖bootstrap.min.css、bootstrap-table.css、jquery.min.js、bootstrap.min.js、bootstrap-table.js、tableExport.js、bootstrap-table-export.js
 * [表格的创建]
 * @param {[String]} Id        [创建表格的id]
 * @param {[Array]} arrayHead  [表头]
 * @param {[Array]} data       [表的数据]
 */
define([], function () {
  function BdadpTable(Id, arrayHead, data, boolcheck) {
    this._id = Id;
    this._arrayHead = arrayHead;
    this._data = data || [];
    this._boolcheck = boolcheck || false;
  }

  /* window.progress = function(value, row, index){
   //var progressVal = typeof row.progress == "string"?Number(row.progress).toFixed(4):row.progress.toFixed(4);
   //var progressVal = Math.round(row.progress*10000)/10000;
   var progressVal = row.progress;
   var backgroundColor = "#32CD32";
   if(progressVal == -1){
   backgroundColor = "#DC143C";
   progressVal = 1;
   }
   return '<div class="progress" style=" margin-bottom: 0px;">'+
   '<div class="progress-bar" role="progressbar" aria-valuenow="'+ progressVal*100 +'" aria-valuemin="0" aria-valuemax="1" style="color:black; width:' + progressVal*100 +'%; background-color: '+ backgroundColor +'">'+
   + Math.round(progressVal*10000)/100 +'%' +
   '</div>' +
   '</div>';
   }*/

  BdadpTable.prototype.creatTable = function () {
    _this = this;
    this._th = "";
    this._html = "";
    this._head =
        '<table id="' + this._id + '"'
        + ' data-classes="table table-no-bordered table-hover"  data-toolbar="#toolbar"'
        + ' data-search="true"' + ' data-show-columns="true"' + ' data-show-export="true"'
        + ' data-detail-formatter="detailFormatter"' + ' data-minimum-count-columns="1"'
        + ' data-pagination="true"' + ' data-id-field="id"' + ' data-page-size="5"'
        + ' data-page-list="[5,8,12]"' + ' data-show-footer="false"'
        + ' data-response-handler="responseHandler">';
    //var table = new BdadpTable("ID",{"dataField":["id","name","price"],"headText":["ID","Item
    // Name","Item Price"]},data);
    var _arrayHead = this._arrayHead;
    for (var i = 0; i < _arrayHead.dataField.length; i++) {
      var _specialOperate = "";
      for (var attr in _arrayHead.events[i]) {
        _specialOperate += attr + "='" + _arrayHead.events[i][attr] + "' ";
      }
      _this._th +=
          "<th data-align='center' data-sortable='true'" + _specialOperate + "data-field='"
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
  return {
    BdadpTable: BdadpTable
  }
})
