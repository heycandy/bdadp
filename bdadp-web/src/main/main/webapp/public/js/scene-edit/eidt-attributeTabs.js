/**
 * Created by Hu on 2016/8/30.
 */
define([], function () {
  'use strict';
  return function () {

    function Html(html) {
      this.html = typeof (html) == "undefined" ? '' : html;
      this.append = function (html) {
	if (typeof (html) == "string") {
	  this.html += html;
	  return;
	}
	if (typeof (html) == "object" && html.constructor.name == "Html") {
	  this.html += html.toString();
	  return;
	}
      };
      this.toString = function () {
	return this.html;
      };
    }

    /**
     * attrId{sceneId+key+attri}
     * @param attributesTabId
     * @param descTabId
     * @returns {Html|*}
     */
    function initTabs(attributesTabId, descTabId) {
      this._html = new Html();
      this._html.append('<div class="tabPanel">');
      this._html.append('<ul class="nav nav-tabs">');
      this._html.append('<li class="active">');
      this._html.append('<a href="#' + attributesTabId
			+ '" data-toggle="tab"><span style="font-size: 13px;">'
			+ $.i18n.prop("d_edit_attribute") + '</span></a></li>');
      this._html.append('<li><a href="#' + descTabId
			+ '" data-toggle="tab"><span style="font-size: 13px;">'
			+ $.i18n.prop("d_edit_describe") + '</span></a></li>');
      this._html.append('</ul>');
      //********tab content*************
      this._html.append('<div class="tab-content">');
      this._html.append('<div class="tab-pane fade in active" id="' + attributesTabId + '"></div>');			//add attribute form
      this._html.append('<div class="tab-pane fade" id="' + descTabId + '"></div>');
      this._html.append('</div>');
      this._html.append('</div>');
      return this._html;
    }

    function _create(attributesTabId, descTabId) {
      return new initTabs(attributesTabId, descTabId);
    }

    return {
      create: _create		//return dialog html
    }
  }

})
