$.namespace("menu.index");
$(function () {
    menu.index.init();
});

menu.index = {
    init: function () {
        var currentPath = window.document.location.pathname;
        currentPath = currentPath.split(';')[0];
        var tag = $('ul').find('[href="' + currentPath + '"]');
        tag.css('background', '#1890ff');
        tag.parent().parent().removeClass('collapse');
        $('#'+tag.parent().parent().attr('id')+'_i').removeClass('icon-chevron-down').addClass('icon-chevron-up');
        menu.index.restHeight();
        window.addEventListener("resize", menu.index.restHeight);
    },

    restHeight:function(){
        var height = $(window).height() - 60;
        height = Math.max(height, $('.right_content').height());
        $('.leftMenu').css({"height": height + 'px', "backgroundColor": '#001529'});
    }
};