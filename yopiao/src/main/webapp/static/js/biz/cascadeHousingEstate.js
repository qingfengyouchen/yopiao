var cascadeHousingEstate = {
  init: function(opts){
      opts = $.extend({
          ctx: undefined,
          id: undefined,
          kid: undefined,
          kname: undefined
      }, opts);

      jQuery.i18n.properties({
          name:'combogrid',
          path: opts.ctx + '/static/js/jquery.combogrid-1.6.3/i18n/',
          mode:'both',
          language: 'zh'
      });

      var isMatchResult;
      var $input = $("#" + opts.id );
      $input.combogrid({
          debug:true,
          i18n: true,
          showOn: true,
          searchIcon:true,
          originValue: "",
          otherParamsId: opts.otherParamsId,
          colModel: [{'columnName':'name','width':'50','label':'name'}],
          url: opts.ctx + '/common/searchHousingEstate',
          select: function( event, ui) {
              $( "#" + opts.kid ).val( ui.item.id );
              $input.val( ui.item.name );
              return false;
          },
          matchResultCallback: function(isMatch){
              isMatchResult = isMatch;
              setTimeout(function(){
                    if(!isMatchResult){
                        clearValue();
                    }
              }, 6000);
          }
      });

      $input.before('<input type="hidden" id="'+
          opts.kid+'" name="'+opts.kname+'" value="'+opts.kvalue+'">');

      if(opts.otherParamsId){
          $input.before('<input type="hidden" id="'+ opts.otherParamsId+'">');
      }

      $input.blur(function(){
          clearValue();
      });

      function clearValue(){
          if(!isMatchResult || Public.isNull($input.val())){
              $input.val("");
              $('#' + opts.kid).val("");
          }
      }
  }
};


