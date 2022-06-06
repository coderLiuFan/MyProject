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