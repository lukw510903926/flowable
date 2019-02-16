$.namespace("office.index");
$(function () {
    office.index.init();
});

office.index = {
    init: function () {
        var currentPath = window.document.location.pathname;
        var tag = $('ul').find('[href="' + currentPath + '"]');
        tag.css('background', '#1890ff');
        tag.parent().parent().removeClass('collapse');
        var minHeight = ($(window).height() - 60) + 'px';
        $('.left').css({"minHeight": minHeight,"backgroundColor": '#001529'});
    }
};