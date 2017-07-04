/**
 * Created by ezgoing on 14/9/2014.
 */

"use strict";
(function (factory) {
  if (typeof define === 'function' && define.amd) {
    define(['jquery'], factory);
  } else {
    factory(jQuery);
  }
}(function ($) {
  //圆角矩形
  CanvasRenderingContext2D.prototype.roundRect = function (x, y, w, h, r) {
    var min_size = Math.min(w, h);
    if (r > min_size / 2) {
      r = min_size / 2;
    }
    // 开始绘制
    this.beginPath();
    this.moveTo(x + r, y);
    this.arcTo(x + w, y, x + w, y + h, r);
    this.arcTo(x + w, y + h, x, y + h, r);
    this.arcTo(x, y + h, x, y, r);
    this.arcTo(x, y, x + w, y, r);
    this.closePath();
    return this;
  }
  var cropbox = function (options, el) {
    var el = el || $(options.imageBox),
        obj =
            {
              state: {},
              ratio: 1,
              options: options,
              imageBox: el,
              thumbBox: el.find(options.thumbBox),
              spinner: el.find(options.spinner),
              image: new Image(),
              getDataURL: function () {
                var width = this.thumbBox.width(),
                    height = this.thumbBox.height(),
                    canvas1 = document.createElement("canvas"),
                    canvas2 = document.createElement("canvas"),
                    canvas3 = document.createElement("canvas"),
                    canvas11 = document.createElement("canvas"),
                    dim = el.css('background-position').split(' '),
                    size = el.css('background-size').split(' '),
                    dx = parseInt(dim[0]) - el.width() / 2 + width / 2,
                    dy = parseInt(dim[1]) - el.height() / 2 + height / 2,
                    dw = parseInt(size[0]),
                    dh = parseInt(size[1]),
                    sh = parseInt(this.image.height),
                    sw = parseInt(this.image.width);

                canvas11.width = width;
                canvas11.height = height;
                var context11 = canvas11.getContext("2d");
                context11.drawImage(this.image, 0, 0, sw, sh, dx, dy, dw, dh);
                var imageDD = canvas11.toDataURL('image/png', 0.5);

                //canvas1.width = width;
                //canvas1.height = height;
                //var context1 = canvas1.getContext("2d");
                //context1.drawImage(this.image, 0, 0, sw, sh, dx, dy, dw, dh);

                // add round rect
                //var image2 = new Image();
                //image2.src = canvas11.toDataURL('image/png');
                //canvas2.width = width;
                //canvas2.height = height;
                //var context2 = canvas2.getContext("2d");
                //var pattern = context2.createPattern(image2, 'no-repeat');
                // 这里使用圆角矩形
                //context2.roundRect(0, 0, image2.width, image2.height, 25);
                // 填充绘制的圆
                //context2.fillStyle = pattern;
                //context2.fill();

                //add transparent bottom blank
                var image3 = new Image();
                //image3.src = canvas2.toDataURL('image/png');
                image3.src = imageDD;
                canvas3.width = width;
                canvas3.height = height + 40;
                var context3 = canvas3.getContext("2d");
                context3.drawImage(image3, 0, 0);
                var imageData2 = canvas3.toDataURL('image/png', 0.1);
                return [imageDD, imageData2];
              },
              getBlob: function () {
                var imageData = this.getDataURL();
                var b64 = imageData.replace('data:image/png;base64,', '');
                var binary = atob(b64);
                var array = [];
                for (var i = 0; i < binary.length; i++) {
                  array.push(binary.charCodeAt(i));
                }
                return new Blob([new Uint8Array(array)], {type: 'image/png'});
              },
              zoomIn: function () {
                this.ratio *= 1.1;
                setBackground();
              },
              zoomOut: function () {
                this.ratio *= 0.9;
                setBackground();
              }
            },
        setBackground = function () {
          /*var w = parseInt(obj.image.width) * obj.ratio;
           var h = parseInt(obj.image.height) * obj.ratio;*/
          var w = 164 * obj.ratio;
          var h = 160 * obj.ratio;

          var pw = (el.width() - w) / 2;
          var ph = (el.height() - h) / 2;

          el.css({
                   'background-image': 'url(' + obj.image.src + ')',
                   'background-size': w + 'px ' + h + 'px',
                   'background-position': pw + 'px ' + ph + 'px',
                   'background-repeat': 'no-repeat'
                 });
        },
        imgMouseDown = function (e) {
          e.stopImmediatePropagation();

          obj.state.dragable = true;
          obj.state.mouseX = e.clientX;
          obj.state.mouseY = e.clientY;
        },
        imgMouseMove = function (e) {
          e.stopImmediatePropagation();

          if (obj.state.dragable) {
            var x = e.clientX - obj.state.mouseX;
            var y = e.clientY - obj.state.mouseY;

            var bg = el.css('background-position').split(' ');

            var bgX = x + parseInt(bg[0]);
            var bgY = y + parseInt(bg[1]);

            el.css('background-position', bgX + 'px ' + bgY + 'px');

            obj.state.mouseX = e.clientX;
            obj.state.mouseY = e.clientY;
          }
        },
        imgMouseUp = function (e) {
          e.stopImmediatePropagation();
          obj.state.dragable = false;
        },
        zoomImage = function (e) {
          e.originalEvent.wheelDelta > 0 || e.originalEvent.detail < 0 ? obj.ratio *= 1.1
              : obj.ratio *= 0.9;
          setBackground();
        }

    obj.spinner.show();
    obj.image.onload = function () {
      obj.spinner.hide();
      setBackground();

      el.bind('mousedown', imgMouseDown);
      el.bind('mousemove', imgMouseMove);
      $(window).bind('mouseup', imgMouseUp);
      el.bind('mousewheel DOMMouseScroll', zoomImage);
    };
    obj.image.src = options.imgSrc;
    el.on('remove', function () {
      $(window).unbind('mouseup', imgMouseUp)
    });

    return obj;
  };

  jQuery.fn.cropbox = function (options) {
    return new cropbox(options, this);
  };
}));
