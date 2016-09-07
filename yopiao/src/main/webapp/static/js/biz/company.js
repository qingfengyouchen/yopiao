var company = {
    /**
     * @param $container
     * @param opts {
     *  ctx
     *  companyId
     *  companyName
     *  companyIdVal
     *  subCompanyId
     *  subCompanyName
     *  subCompanyIdVal
     * }
     */
    init: function($container, opts){
        opts = $.extend({
            companyId: 'companyId',
            companyName: 'companyId',
            subCompanyId: 'subCompanyId',
            subCompanyName: 'subCompanyId',
            createSelect: function(id, name, labelName){
                return $('<label>'+ labelName +'：</label><select id="' +
                    id + '" name="' + name + '" style="width: 160px;">' +
                    '<option value="">请选择</option></select>');
            }
        }, opts);
        var _this = this;
        $container.append(opts.createSelect(opts.companyId, opts.companyName, '地产公司'));
        if(opts.subCompanyId){
            var $container2 = opts.createSelect(opts.subCompanyId, opts.subCompanyName, '门店');
            $container2.hide();
            $container.append($container2);
        }

        var $company = $container.find('#' + opts.companyId);
        var $subCompany = $container.find('#' + opts.subCompanyId);
        _this._initCompany($company, opts, function(){
            if($subCompany.length  < 1 || Public.isNull(opts.subCompanyIdVal)){
                return;
            }
            _this.getByFather(opts.ctx, $company.val(), $subCompany, opts.subCompanyIdVal);
        });
    },
    _initCompany: function($company, opts, callback){
        var _this = this;
        $.ajax({
            url: opts.ctx + '/common/getCompany',
            type: 'get',
            dataType: 'json',
            success: function(data){
                if(data){
                    $(data).each(function(i, item){
                        $company.append('<option value="' + item.id + '">' + item.name + '</option>');
                    });

                    //setTimeout(function(){
                        $company.val(opts.companyIdVal);
                    //}, 0);

                    if(callback){
                        callback();
                    }
                }
            }
        });

        $company.change(function(){
            _this.getByFather(opts.ctx, $(this).val(), $('#'+opts.subCompanyId));
        });
    },
    getByFather: function(ctx, fatherId, $select, defaultVal) {
        var _this = this;
        $.ajax({
            url: ctx + '/common/getSubCompany/' + fatherId,
            type: 'get',
            dataType: 'json',
            success: function (data) {
                if (data) {
                    _this._showHideSelect($select, data.length > 0);
                    $select.find("option:gt(0)").remove();
                    $(data).each(function (i, item) {
                        $select.append('<option value="' + item.id + '">' + item.name + '</option>');
                    });

                    if(defaultVal){
                        setTimeout(function(){
                            $select.val(defaultVal);
                        }, 0);
                    }

                }else{
                    _this._showHideSelect($select, false);
                }
            }
        });
    },
    _showHideSelect: function($select, isShow){
        if(isShow){
            $select.css('display', '');
            $select.prev().css('display', '');
        }else{
            $select.css('display', 'none');
            $select.prev().css('display', 'none');
        }
    }
};