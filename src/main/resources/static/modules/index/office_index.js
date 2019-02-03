$.namespace("office.index");
$(function () {
office.index.init();
});

office.index = {
    init:function () {
        console.log(window.document.location.pathname);
        var currentPath = window.document.location.pathname;
        var tag = $('ul').find('[href="'+currentPath+'"]');
        tag.css('background', '#b9def0');
    }
};