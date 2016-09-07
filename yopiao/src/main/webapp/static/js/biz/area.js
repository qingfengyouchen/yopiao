var area = {
    /**
     * @param $container
     * @param opts {
     *  ctx
     *  cityId
     *  cityName
     *  cityVal
     *  districtId
     *  districtName
     *  districtVal
     *  zoneId
     *  zoneName
     *  zoneVal
     * }
     */
    init: function($container, opts){
        opts = $.extend({
            cityId: 'cityId',
            cityName: 'cityId',
            districtId: 'districtId',
            districtName: 'districtId',
            zoneId: 'zoneId',
            zoneName: 'zoneId',
            createSelect: function(id, name, labelName){
                return $('<label>'+ labelName +'：</label><select id="' +
                    id + '" name="' + name + '" style="width: 80px;">' +
                    '<option value="">请选择</option></select>');
            },
            changeCallback: undefined
        }, opts);
        var _this = this;
        $container.append(opts.createSelect(opts.cityId, opts.cityName, '市'));
        if(opts.districtId){
            var $area2 = opts.createSelect(opts.districtId, opts.districtName, '区县');
            $area2.hide();
            $container.append($area2);
        }

        if(opts.zoneId) {
            var $area3 = opts.createSelect(opts.zoneId, opts.zoneName, '区域');
            $area3.hide();
            $container.append($area3);
        }

        var $city = $container.find('#' + opts.cityId);
        var $district = $container.find('#' + opts.districtId);
        var $zone = $container.find('#' + opts.zoneId);
        _this._initCity($city, opts, function(){
            if($district.length  < 1 || Public.isNull(opts.districtIdVal)){
                if(opts.changeCallback){
                    opts.changeCallback();
                }
                return;
            }
            _this._getAreaByFather(opts.ctx, $city.val(), 2, $district, opts.districtIdVal,
                function(){
                    if($zone.length  < 1 || Public.isNull(opts.zoneIdVal)){
                        if(opts.changeCallback){
                            opts.changeCallback();
                        }
                        return;
                    }
                    _this._getAreaByFather(opts.ctx, $district.val(), 3, $zone, opts.zoneIdVal,function(){
                        if(opts.changeCallback){
                            opts.changeCallback();
                        }
                    });
            });
        });
    },
    _initCity: function($city, opts, callback){
        var _this = this;
        $.ajax({
            url: opts.ctx + '/common/getCity',
            type: 'get',
            dataType: 'json',
            success: function(data){
                if(data){
                    $(data).each(function(i, item){
                        $city.append('<option value="' + item.id + '">' + item.name + '</option>');
                    });

                    //setTimeout(function(){
                        $city.val(opts.cityIdVal);
                    //}, 0);

                    if(callback){
                        callback();
                    }
                }
            }
        });

        $city.change(function(){
            _this._getAreaByFather(opts.ctx, $(this).val(), 2, $('#'+opts.districtId));
            if(opts.changeCallback){
                opts.changeCallback();
            }
        });

        $('#' + opts.districtId).change(function(){
            _this._getAreaByFather(opts.ctx, $(this).val(), 3, $('#'+opts.zoneId));
            if(opts.changeCallback){
                opts.changeCallback();
            }
        });
    },
    _getAreaByFather: function(ctx, fatherId, level, $select, defaultVal, callback) {
        var _this = this;
        $.ajax({
            url: ctx + '/common/getArea/'+ level +'/' + fatherId,
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

                    if(callback){
                        callback();
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