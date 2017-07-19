/**
 * Created by Administrator on 2016/10/31.
 */
(function () {
    $.fn.extend({
        checks_select: function (options) {
            $(this).data("jq_checks_select", null);
            if (settings.testLangType() == "zh" || settings.testLangType() == "zh_CN") {
                $(this).val("---请输入---");
            } else if (settings.testLangType() == "en" || settings.testLangType() == "en_US" || settings.testLangType() == "en_GB") {
                $(this).val("---please choose---")
            }

            $(this).next().empty(); //请输入
            $(this).unbind("click");
            $(this).click(function (e) {
                var self = this;
                jq_check = $(this);
//jq_check.attr("class", ""); 
                if (!$(this).data("jq_checks_select")) {
                    $(this).data("jq_checks_select", jq_check.next());
                    $(this).data("jq_checks_select").addClass("checks_div_select");
//jq_checks_select = $("<div class='checks_div_select'></div>").insertAfter(jq_check); 
                    $.each(options, function (i, n) {
                        check_div = $("<div><input type='checkbox' value='" + n + "'>" + n + "</div>").appendTo($(self).data("jq_checks_select"));
                        check_box = check_div.children();
                        check_box.click(function (e) {
//jq_check.attr("value",$(this).attr("value") ); 

                            temp = "";
                            $(this).parent().parent().find("input:checked").each(function (i) {
                                if (i == 0) {
                                    temp = $(this).val();
                                } else {
                                    temp += "," + $(this).val();
                                }
                            });
//alert(temp); 
                            jq_check.val(temp);
                            e.stopPropagation();
                        });
                    });
                    $(this).data("jq_checks_select").show();
                } else {
                    $(this).data("jq_checks_select").toggle();

                }
                e.stopPropagation();
            });
            $(document).click(function () {
                flag = $("#weeksWrap");
                if (flag.val() == "") {
                    if (settings.testLangType() == "zh" || settings.testLangType() == "zh_CN") {
                        flag.val("---请输入---");
                    } else if (settings.testLangType() == "en" || settings.testLangType() == "en_US" || settings.testLangType() == "en_GB") {
                        flag.val("---please choose---");
                    }

                }
                $("#weeksWrap").next().hide();

                flag = $("#monthWrap");
                if (flag.val() == "") {
                    if (settings.testLangType() == 'zh') {
                        flag.val("---请输入---");
                    } else {
                        flag.val("---please choose---");
                    }
                }
                $("#monthWrap").next().hide();
            });

        }
    })

})(jQuery); 