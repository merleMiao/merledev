
var httpAgent = function(url, requestType, param, callback, async) {
    if (typeof callback != 'function') {
        callback = function(str) {
            console.log(str);
        }
    };
    $('.iloading').show();
    setTimeout(function(){
        $('.iloading').hide();
    },5000);
    if (debug) {
        $.ajax({
            type: requestType,
            url: url,
            data: param,
            async:async,
            success: function(msg) {
                $('.iloading').hide();
                callback(msg);
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                $('.iloading').hide();
            }
        });
    } else {
        iydf.bridge.prepare("pc");
        var event = {
            //apiName: 'client_http_agent',
            apiName: 'httpAgent',
            params: {
                "url": url,
                "type": requestType,
                "param": param
                //"action": action,
            },
            cb:callback,
            fakeret: "fail"
        };
        iydf.bridge.call(event);
    }
};

var setCookie = function setCookie(c_name, value, expiredays){
　　　　var exdate=new Date();
　　　　exdate.setDate(exdate.getDate() + expiredays);
　　　　document.cookie=c_name+ "=" + escape(value) + ((expiredays==null) ? "" : ";expires="+exdate.toGMTString());
}

var getRequest = function getRequest(key){
    var url = location.search; //获取url中"?"符后的字串
    var theRequest = new Object();
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for(var i = 0; i < strs.length; i ++) {
            theRequest[strs[i].split("=")[0]]=decodeURI(strs[i].split("=")[1]);
        }
    }
    if(key){
        return theRequest[key];
    }else{
        return theRequest;
    }
}