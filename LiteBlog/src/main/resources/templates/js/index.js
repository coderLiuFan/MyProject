/* 自定义trim */
function trim (str) {  //删除左右两端的空格,自定义的trim()方法
  return str == undefined ? "" : str.replace(/(^\s*)|(\s*$)/g, "")
}

//获取url地址上面的参数
// function requestUrlParam(argname){
//   var url = location.href //获取完整的请求url路径
//   var arrStr = url.substring(url.indexOf("?")+1).split("&")
//   for(var i =0;i<arrStr.length;i++)
//   {
//       var loc = arrStr[i].indexOf(argname+"=")
//       if(loc!=-1){
//           return arrStr[i].replace(argname+"=","").replace("?","")
//       }
//   }
//   return ""
// }

function requestUrlParam(argName) {
    if (!argName) {
        return false;
    }
    var args = {}, query = location.search.substring(1), pairs = query.split("&");
    for (var i = 0; i < pairs.length; i++) {
        var pos = pairs[i].indexOf('=');
        if (pos == -1) continue;
        var argname = pairs[i].substring(0, pos), value = pairs[i].substring(pos + 1);
        value = decodeURIComponent(value);
        if (argName == argname) {
            return value;
        }
    }
}

function updateUrl( key, value){
    var newurl = updateQueryStringParameter(key, value)
    //向当前url添加参数，没有历史记录
    window.history.replaceState({
        path: newurl
    }, '', newurl);
}

function updateQueryStringParameter(key, value) {
    var uri = window.location.href
    if(!value) {
        return uri;
    }
    var re = new RegExp("([?&])" + key + "=.*?(&|$)", "i");
    var separator = uri.indexOf('?') !== -1 ? "&" : "?";
    if (uri.match(re)) {
        return uri.replace(re, '$1' + key + "=" + value + '$2');
    }
    else {
        return uri + separator + key + "=" + value;
    }
}



function getMyDate(num) {
    var oDate = new Date(num),
        oYear = oDate.getFullYear(),
        oMonth = oDate.getMonth()+1,
        oDay = oDate.getDate(),
        // oHour = oDate.getHours(),
        // oMin = oDate.getMinutes(),
        // oSen = oDate.getSeconds(),
        oTime = oYear +'-'+ addZero(oMonth) +'-'+ addZero(oDay) /*+' '+ addZero(oHour) +':'+
            addZero(oMin) +':'+addZero(oSen)*/;
    return oTime;
}

//补零操作
function addZero(num){
    if(parseInt(num) < 10){
        num = '0'+num;
    }
    return num;
}

function changeTime(time){
    var commonTime = "";
    if(time){
        var unixTimestamp = new Date(time*1) ;
        commonTime = unixTimestamp.toLocaleString();
    }
    return commonTime;
}
