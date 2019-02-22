$.namespace('biz.info.detail');
$(function () {
    biz.info.detail.init();
});
biz.info.detail = {

    mark: 0,
    init : function(){
        var id = $('#bizId').val();
        $.ajax({
            url: path + "/bizInfo/detail/" + id,
            cache: false,
            async: false,
            success: function (result) {
                if (!result) {
                    bsAlert("错误", "异常数据，请验证数据正确性！", function () {
                        window.opener = null;
                        window.close();
                    });
                    return;
                }
                console.log(result);
            }
        });
    },
};