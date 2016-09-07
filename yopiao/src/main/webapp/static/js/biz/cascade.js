var cascade = {
  init: function(opts){
      opts = $.extend({
          ctx: undefined,
          id: undefined,
          kid: undefined,
          kname: undefined,
          displayName: undefined,
          url: undefined,
          colModel: undefined,
          selectCallback: undefined
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
          colModel: opts.colModel,
          url: opts.url,
          select: function( event, ui) {
              var kid = ui.item.id;
              var value = ui.item[opts.displayName];
              $( "#" + opts.kid ).val( kid );
              $input.val( value );

              if(opts.selectCallback){
                  opts.selectCallback(kid, value);
              }
              return false;
          },
          matchResultCallback: function(isMatch){
              isMatchResult = isMatch;
              setTimeout(function(){
                    if(!isMatchResult){
                        clearValue();
                        isMatch = true;
                    }
              }, 20000);
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


