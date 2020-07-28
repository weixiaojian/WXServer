/**
 * 判断是否为空
 * @param value
 * @param text
 * @returns {boolean}
 */
function checkEmpty(value,text){

    if(null==value || value.length==0){
        alert(text+ "不能为空");
        return false;
    }
    return true;
}

/**
 * 判断是否数字 (小数和整数)
 * @param value
 * @param text
 * @returns {boolean}
 */
function checkNumber(value, text){

    if(value.length==0){
        alert(text+ "不能为空");
        return false;
    }
    if(isNaN(value)){
        alert(text+ "必须是数字");
        return false;
    }
    return true;
}

/**
 * 判断是否整数
 * @param value
 * @param text
 * @returns {boolean}
 */
function checkInt(value, text){

    if(value.length==0){
        alert(text+ "不能为空");
        return false;
    }
    if(parseInt(value)!=value){
        alert(text+ "必须是整数");
        return false;
    }
    return true;
}

/**
 * 获取url后的参数值
 * @param key
 * @returns {string}
 */

function getUrlParam(key) {
    var href = window.location.href;
    var url = href.split("?");
    if(url.length <= 1){
        return "";
    }
    var params = url[1].split("&");

    for(var i=0; i<params.length; i++){
        var param = params[i].split("=");
        if(key == param[0]){
            return param[1];
        }
    }
}

