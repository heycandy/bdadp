/**
 * Created by Hu on 2016/7/23.
 */
define([], function () {
  'use strict';

  var plugin_name = 'dialog',
      tpl_button = '<button class="xdsoft_btn" type="button"></button>',
      tpl_dialog = '<div class="xdsoft_dialog xdsoft_dialog_shadow_effect" style="width:400px">' +
		   '<div class="xdsoft_dialog_popup_title">' +
		   '<span></span>' +
		   '<a class="xdsoft_close" style="cursor: pointer;">&times;</a>' +
		   '</div>' +
		   '<a class="xdsoft_close">&times;</a>' +
		   '<div class="xdsoft_dialog_content">' + '</div>' +
		   '<div class="xdsoft_dialog_buttons">' + '</div>' +
		   '</div>',

      default_options = {
	title: false,
	buttons: {},
	closeFunction: 'fadeOut',
	showFunction: 'fadeIn',
	closeBtn: true,
	closeOnClickOutside: false,
	closeOnEsc: false,
	clickDefaultButtonOnEnter: false,
	zIndex: 1034,
	modal: true,
	shown: true,
	removeOnHide: true,
	hideGlobalScrollbar: true,
	onBeforeShow: function () {
	},
	onBeforeHide: function () {
	},
	onAfterShow: function () {
	},
	onAfterHide: function () {
	}
      };

  function createCallbackHandler(callback, dialog_box) {
    return function (event) {
      if (!callback || callback.call(dialog_box, event, this) !== false) {
	dialog_box[plugin_name]('hide');
      }
    };
  }

  function makeButtons(buttons, tpl_button, buttons_box, dialog_box) {
    var title, btn, k = 0;

    if (buttons !== undefined) {
      buttons_box
	  .empty();
    }
    if (buttons && $.isPlainObject(buttons)) {
      for (title in buttons) {
	if (buttons.hasOwnProperty(title)) {
	  if (buttons[title] instanceof $) {
	    btn = buttons[title];
	  } else {
	    btn = $(tpl_button).html(title);

	    btn.click(createCallbackHandler($.isFunction(buttons[title]) ? buttons[title]
						: ((buttons[title].click
						    && $.isFunction(buttons[title].click))
		? buttons[title].click : false), dialog_box));

	    if ($.isPlainObject(buttons[title])) {
	      if (buttons[title].className) {
		btn.addClass(buttons[title].className);
	      }

	      if (buttons[title].primary) {
		buttons_box.find('button')
		    .removeClass('xdsoft_primary');
		btn
		    .addClass('xdsoft_primary');
	      }

	      if (buttons[title].title) {
		btn = btn.html(title);
	      }
	    }
	  }

	  buttons_box.append(btn);
	  k += 1;
	}
      }
    }
  }

  function makeTitle(text, title, dialog_box, options) {
    if (!text && text !== '') {
      title.hide();
      dialog_box.find('div.xdsoft_dialog>.xdsoft_close').show();
    } else {
      dialog_box.find('div.xdsoft_dialog>.xdsoft_close').hide();
      title
	  .show()
	  .find('span')
	  .html(text);
    }
    if (!options.closeBtn) {
      dialog_box.find('.xdsoft_close').hide();
    }
  }

  $.fn[plugin_name] = function (_options, second, third) {
    var that = this,
	dialog_box = that,
	options = $.extend(true, {}, default_options, $.isPlainObject(_options) ? _options : {}),
	dialog = dialog_box.find('.xdsoft_dialog'),
	buttons_box = dialog_box.find('.xdsoft_dialog_buttons'),
	event;

    if (dialog_box.hasClass('xdsoft_dialog_overlay') && dialog_box.data('options')) {

      options = dialog_box.data('options');

      if ($.type(_options) === 'string' && _options.length) {
	_options = _options.toLowerCase();
	_options = _options.charAt(0).toUpperCase() + _options.slice(1);
	event = $.Event('before' + _options + '.xdsoft');
	dialog_box.trigger(event);
	if (options['onBefore' + _options] && $.isFunction(options['onBefore' + _options])) {
	  options['onBefore' + _options].call(that, options, _options);
	}
	if (!event.isDefaultPrevented()) {
	  switch (_options.toLowerCase()) {
	    case 'ok':
	      if (dialog_box.is(':visible')) {
		if (second !== 'enter' || options.clickDefaultButtonOnEnter) {
		  buttons_box.find('.xdsoft_primary').trigger('click');
		}
		if (second === 'enter' && options.clickDefaultButtonOnEnter && third) {
		  third.stopPropagation();
		  third.preventDefault();
		}
	      }
	      break;
	    case 'hide':
	      if (dialog_box.is(':visible')) {
		if (second !== 'esc' || options.closeOnEsc) {
		  dialog_box.stop()[options.closeFunction](function () {
		    if (options.removeOnHide) {
		      dialog_box.remove();
		    }
		    if (options.modal && options.hideGlobalScrollbar
			&& !$('.xdsoft_dialog_overlay:visible').length) {
		      $('html').removeClass('xdsoft_overlay_lock').removeClass('xdsoft_overlay_lock_offset');
		      $('html')
			  .css('margin-right', 0);
		    }
		    dialog_box.trigger('after' + _options + '.xdsoft');
		    if (options['onAfter' + _options] && $.isFunction(options['onAfter'
									      + _options])) {
		      options['onAfter' + _options].call(that, options, _options);
		    }
		    if (dialog_box.data('resize_interval')) {
		      clearInterval(dialog_box.data('resize_interval'));
		    }
		  });
		  if (second === 'esc' && options.closeOnEsc && third) {
		    third.stopPropagation();
		    third.preventDefault();
		  }
		}
	      }
	      break;
	    case 'show':
	      if (!dialog_box.is(':visible')) {
		if (options.modal && options.hideGlobalScrollbar) {

		  $('html').addClass('xdsoft_overlay_lock');
		}
		dialog_box.stop()[options.showFunction](function () {
		  dialog_box.trigger('after' + _options + '.xdsoft');
		  if (options['onAfter' + _options] && $.isFunction(options['onAfter'
									    + _options])) {
		    options['onAfter' + _options].call(that, options, _options);
		  }
		});
		if (!options.modal) {
		  dialog_box
		      .data('old_width', dialog.width())
		      .data('resize_interval', setInterval(function () {
			      if (dialog.width() !== dialog_box.data('old_width')) {
				dialog_box.dialog('resize');
			      }
			    }, 300));
		}
	      }
	      break;
	    case 'title':
	      makeTitle(second, dialog_box.find('.xdsoft_dialog_popup_title'), dialog_box, options);
	      break;
	    case 'content':
	      dialog_box
		  .find('.xdsoft_dialog_content')
		  .html(second);
	      break;
	  }
	  if (_options.toLowerCase() !== 'hide' && _options.toLowerCase() !== 'show') {
	    dialog_box.trigger('after' + _options + '.xdsoft');
	    if (options['onAfter' + _options] && $.isFunction(options['onAfter' + _options])) {
	      options['onAfter' + _options].call(that, options, _options);
	    }
	  }
	}
      } else {
	makeTitle(options.title, dialog_box.find('.xdsoft_dialog_popup_title'), dialog_box,
		  options);
	makeButtons(options.buttons, tpl_button, dialog_box.find('.xdsoft_dialog_buttons'),
		    dialog_box);
      }

      return dialog_box;
    }

    if ($.type(_options) === 'string') {
      return this;
    }

    dialog_box = $('<div class="xdsoft_dialog_overlay xdsoft_modal"></div>');

    dialog_box.css('zIndex', options.zIndex);

    dialog_box.data('options', options);

    dialog = $(tpl_dialog);

    dialog_box.on('mousedown.xdsoft', function (event) {
      if (options.closeOnClickOutside) {
	dialog_box[plugin_name]('hide');
      }
    });

    dialog.on('mousedown.xdsoft', function (event) {
      event.stopPropagation();
    });

    buttons_box = dialog.find('.xdsoft_dialog_buttons');

    dialog_box.append(dialog);

    dialog_box.dialog('content', that.length ? that[0] : '<div>' + that.selector + '</div>');

    dialog
	.find('.xdsoft_close')
	.click(function () {
		 dialog_box[plugin_name]('hide');
	       });

    makeTitle(options.title, dialog.find('.xdsoft_dialog_popup_title'), dialog_box, options);
    makeButtons(options.buttons, tpl_button, buttons_box, dialog_box);

    $('body').append(dialog_box);

    if (options.shown) {
      dialog_box.dialog('show');
    }
    if (!options.modal) {
      dialog_box.dialog('resize');
    }

    return dialog_box;
  };

  $.fn[plugin_name].default_options = default_options;

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

    function init(modelId) {
      this._html = new Html();
      this._html.append("<div class='modal fade' id='" + modelId
			+ "' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>");
      this._html.append("<div class='modal-dialog'>");
      this._html.append("<div class='modal-content'>");
      this._html.append("<div class='modal-body'></div>");
      this._html.append("<div class='modal-footer'>");
      this._html.append("<button type='button' class='btn btn-default' data-dismiss='modal'>"
			+ $.i18n.prop('d_cancel') + "</button>");
      this._html.append("<button type='button' id='confirmBtn' class='btn btn-danger'>"
			+ $.i18n.prop('d_confirm') + "</button>");
      this._html.append("</div>");
      this._html.append("</div><!-- /.modal-content -->");
      this._html.append("</div><!-- /.modal-dialog -->");
      this._html.append("</div><!-- /.modal -->");

      return this._html;
    }

    function _create(modelId) {
      return new init(modelId);
    }

    function _destroy(modelId) {
      if ($("#" + modelId).length > 0) {
	$("#" + modelId).remove();
      }
    }

    function _initDialog(_modelId, _contextSelector) {
      var modelId = typeof (_modelId) == "undefined" ? 'defaultModel' : _modelId;
      var contextSelector = typeof (_contextSelector) == "undefined" ? 'body' : _contextSelector;
      var dialog = _create(modelId);
      $(contextSelector).append(dialog.html);
      $('#' + modelId).on('hidden.bs.modal', function () {
	if ($("#" + modelId).length > 0) {
	  $("#" + modelId).remove();
	}
      })
      $("#" + modelId + " .modal-footer").each(function (index, dom) {
	$(dom).css("border-top", "0px");
      })

      $('#' + modelId).modal({backdrop: 'static', keyboard: false});

    }

    /**
     *
     * @param callback
     * @param param  object{message,..}
     * @param _modelId
     * @param _contextSelector
     * @private
     */
    function _initConfirmDialog(callback, param, DialogShow, _modelId, _contextSelector) {
      var _param = typeof(param) == "undefined" ? {} : param;
      _param.defaultMessage = $.i18n.prop('d_warming_reinput');                                    //"您好！输入信息有误，请重输！"
                                                                                                   // Information
                                                                                                   // entered
                                                                                                   // is
                                                                                                   // incorrect,
                                                                                                   // please
                                                                                                   // re-enter!
      var message = _param && (_param.message || _param.defaultMessage);
      var modelId = typeof (_modelId) == "undefined" ? 'defaultModel' : _modelId;
      var contextSelector = typeof (_contextSelector) == "undefined" ? 'body' : _contextSelector;   //default context is body
      var confirmDialog = _create(modelId);
      $(contextSelector).append(confirmDialog.html);
      $('#' + modelId).on('hidden.bs.modal', function () {
	if ($("#" + modelId).length > 0) {
	  $("#" + modelId).remove();
	}
      })
      $("#" + modelId + " .modal-footer").each(function (index, dom) {
	$(dom).css("border-top", "0px");
      })

      //confirm dialog css setting
      $('#' + modelId).on('show.bs.modal', function () {
	$(".modal-dialog").each(function (index, dom) {
	  $(dom).css("margin-top", "10%");
	})

	$(".modal-body").each(function (index, dom) {
	  if (index == 0) {
	    $(dom).append("<div style=''><span class='glyphicon glyphicon-info-sign' style='font-size:30px; color:red;x'></span><label style='margin-left: 1%;color: red;font-size: 22px;'>"
			  + $.i18n.prop('d_warming') + "</label></div>")
	    $(".modal-dialog .modal-content .modal-body span.glyphicon").each(function (index,
											dom) {
	      $(dom).css("top", "7px");
	    })
	    $(dom).append("<p>" + message + "</p>")
	  }
	})
	//callback
	$("#confirmBtn").on('click', function () {
	  $('#' + modelId).trigger('hidden.bs.modal');
	  $(".modal-backdrop.fade.in").remove();
	  if (typeof(callback) == "function") {
	    callback();
	  }
	})

	if (typeof(DialogShow) == "function") {
	  DialogShow();
	}
      })

      $('#' + modelId).modal({backdrop: 'static', keyboard: false});
    }

    function _initShowDialog(content, $modelDialog, _modelId, _contextSelector) {
      var modelId = typeof (_modelId) == "undefined" ? 'defaultModel' : _modelId;
      var contextSelector = typeof (_contextSelector) == "undefined" ? 'body' : _contextSelector;   //default context is body
      var showDialog = _create(modelId);
      $(contextSelector).append(showDialog.html);
      $('#' + modelId).on('hidden.bs.modal', function () {
	if ($("#" + modelId).length > 0) {
	  $("#" + modelId).remove();
	}
      })
      $("#" + modelId + " .modal-footer").each(function (index, dom) {
	$(dom).css("border-top", "0px");
      })

      //show dialog body  setting
      $('#' + modelId).on('show.bs.modal', function () {
	$(".modal-dialog").each(function (index, dom) {
	  $(dom).css("margin-top", "10%");
	})

	$(".modal-body").each(function (index, dom) {
	  if (index == 0) {
	    if (typeof(content) != "undefined") {
	      $(dom).load(content.url,
			  function (response, status) {
			    if (status == "success") {
			      if (typeof($modelDialog)
				  == "function") {
				$modelDialog(response);
			      }
			    }
			  });
	    }
	  }
	})
	/*//callback
	 $("#confirmBtn").on('click',function(){
	 $('#' + modelId).trigger('hidden.bs.modal');
	 $(".modal-backdrop.fade.in").remove();
	 if(typeof(callback) =="function")callback();
	 })*/
      })
      $('#' + modelId).modal({backdrop: 'static', keyboard: false});
    }

    //alert
    function _initAlert(msg, title, time) {
      if (typeof (time) == "undefined" || time == "") {
	time = 1000;
      }
      setTimeout(function () {
	$(".xdsoft_modal").dialog("hide")
      }, time);
      return $('<div>' + msg + '</div>')[plugin_name]({
	title: title
      });
    };

    //alert

    //function _initAlert(msg, callback, title, time) {
    //	if (typeof (time) == "undefined" || time == "") {
    //		time = 1000;
    //	}
    //	setTimeout(function () {
    //		$(".xdsoft_modal").dialog("hide")
    //	}, time);
    //	return $('<div>' + msg + '</div>')[plugin_name]({
    //		title: title
    //	});
    //};

    /*	function _initAlert(msg, callback, title, time) {
     if (typeof (time) == "undefined" || time == "") {
     time = 1000;
     }
     setTimeout(function () {
     $(".xdsoft_modal").dialog("hide")
     }, time);
     return $('<div>' + msg + '</div>')[plugin_name]({
     title: title
     });
     };*/

    //confirm
    function _initConfirm(msg, callback, title) {
      return $('<div>' + msg + '</div>')[plugin_name]({
	title: title,
	onBeforeShow: function () {
	  $('.xdsoft_dialog_buttons button.xdsoft_btn:first-child').text($.i18n.prop('d_confirm'));
	  $('.xdsoft_dialog_buttons button.xdsoft_btn:last-child').text($.i18n.prop('d_cancel'));
	},
	buttons: {
	  ' ': callback || function () {
	  },
	  '': true
	}
      });
    };

    //wait
    function _initWait(title, with_close, not_close_on_click) {
      return $('<div class="xdsoft_waiter"></div>')[plugin_name]({
	title: title,
	closeBtn: with_close ? true : false,
	closeOnClickOutside: not_close_on_click ? false : true
      });
    };

    return {
      create: _create,		//return dialog html
      initDialog: _initDialog,		//return dialog object
      initConfirm: _initConfirm,
      initAlert: _initAlert,
      initWait: _initWait,
      initShowDialog: _initShowDialog
    }
  }

})
