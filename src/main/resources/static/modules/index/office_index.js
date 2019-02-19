$.namespace("office.index");
$(function () {
    office.index.init();
});

office.index = {
    init: function () {
        var currentPath = window.document.location.pathname;
        currentPath = currentPath.split(';')[0];
        var tag = $('ul').find('[href="' + currentPath + '"]');
        tag.css('background', '#1890ff');
        tag.parent().parent().removeClass('collapse');
        var height = $(window).height() - 60;
        height = Math.max(height, $('.content').height());
        $('.left').css({"height": height + 'px', "backgroundColor": '#001529'});

    }
};