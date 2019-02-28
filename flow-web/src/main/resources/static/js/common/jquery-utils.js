(function ($) {
    $.fn.extend({

        /**
         * 清空表单
         */
        clearForm: function () {
            let _this = this;
            $(_this).find('[name]').each(function () {
                $(this).val('');
            });
        },
        /**
         * 获取表单数据
         */
        getForm: function () {
            let param = {};
            let _this = this;
            $(_this).find('[name]').each(function () {
                let value = $(this).val();
                if (value) {
                    param[this.name] = $(this).val();
                }
            });
            return param;
        },
        /***
         * 设置表单数据
         * @param params
         */
        setForm: function (params) {
            let _this = this;
            $(_this).find('[name]').each(function () {
                $(this).val(params[this.name])
            });
        },

        /**
         * 表单校验
         * 校验成功返回true 失败 false;
         * @returns {boolean}
         */
        validateForm: function () {

            let _this = this;
            let checkEmpty = true;
            let defaultMsg = '表单校验失败';
            $(_this).find('[name]').each(function () {
                $(this).siblings("i").remove();
                if (this.required && !$(this).validateRule()) {
                    checkEmpty = false;
                    let msg = $(this).attr('message') ? $(this).attr('message') : defaultMsg;
                    $(this).after("<i style='color:red;'>&nbsp;" + msg + "</i>");
                }
            });
            return checkEmpty;
        },

        validateRule: function () {

            let _this = $(this);
            let result = false;
            let type = _this.type;
            switch (type) {
                case 'text':
                    result = _this._text();
                    break;
                case  'email':
                    result = _this._email();
                    break;
                case 'number':
                    result = _this._checkNum();
                    break;
                default:
                    result = _this._text();
            }
            return result;
        },

        /**
         * 文本校验
         * @returns {boolean}
         */
        _text: function () {

            let _this = $(this);
            let _result = true;
            let _val = $.trim($(_this).val());
            if (_val === '') {
                _result = false;
            }
            let _min = _this.attr('minLength');
            let _max = _this.attr('max_Length');
            if (_min && _max) {
                _result = (_val.length >= _min && _val.length <= _max);
            }
            if (_min && !_max) {
                _result = (_val.length >= _min);
            }
            if (!_min && _max) {
                _result = (_val.length <= _max);
            }
            let _pattern = _this.attr('pattern');
            if (_pattern) {
                let _reg = new RegExp(_pattern);
                _result = _reg.test(_val) && _result;
            }
            return _result;
        },
        /**
         * 邮件
         * @returns {*|boolean}
         */
        _email: function () {

            let _this = $(this);
            let _result = true;
            let _val = $.trim(_this.val());
            let _pattern = _this.attr('pattern');
            if (!_pattern) {
                let _reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
                _result = _reg.test(_val);
            } else {
                _result = _this._text();
            }
            return _result;
        },

        /**
         * 数字
         * @returns {boolean}
         */
        _checkNum: function () {
            return !/^((([1-9]+\d*)?|\d?)(\.\d*)?)?$/.test($(this).value);
        },

        /**
         *
         * @param url
         * @param param
         *      查询参数
         */
        exportFile: function (url, param) {
            let exportForm = document.createElement("form");
            exportForm.action = url;
            exportForm.method = "post";
            exportForm.style.display = "none";
            for (let key in param) {
                let option = document.createElement("input");
                option.name = key;
                option.value = param[key];
                exportForm.appendChild(option);
            }
            document.body.appendChild(exportForm);
            exportForm.submit();
        },

        /**
         *
         * @param ele
         * @param param
         *
         *  textField
         *  valueField
         *  defaultEmpty 是否默认为空
         *  dataList 下拉数据
         *  dataType 数据类型 list listmap
         *
         */
        loadCombobox: function (param) {

            let _this = $(this);
            let textField = param.textField;
            let valueField = param.valueField;
            let defaultEmpty = param['defaultEmpty'];
            let dataList = param['dataList'];
            let dataType = param.dataType;
            if (dataList) {
                _this.empty();
                if (defaultEmpty) {
                    let option = $('<option></option>');
                    option.text('');
                    option.val('');
                    _this.append(option);
                }
                if (!dataType || dataType === 'list') {
                    dataList.forEach(function (entity) {
                        let option = $('<option></option>');
                        option.text(entity);
                        option.val(entity);
                        _this.append(option);
                    });
                } else {
                    dataList.forEach(function (entity) {
                        let option = $('<option></option>');
                        option.text(entity[textField]);
                        option.val(entity[valueField]);
                        _this.append(option);
                    });
                }
            }
        }

    })
})(jQuery);