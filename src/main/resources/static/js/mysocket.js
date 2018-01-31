function initWebSocket(url){
    var websocket;
    var target;
    if(url)
        target = url;
    if('WebSocket' in window){
        if(websocket){
            websocket.close();
        }
        websocket = new WebSocket(target);
        websocket.target = target;
        //连接发生错误的回调方法
        websocket.onerror = function(){
            setErrorInfo("");
        };
        //连接成功建立的回调方法
        websocket.onopen = function(event){

        };
        //接收到消息的回调方法
        websocket.onmessage = function(event){
            setMessage(event.data);
        };
        //连接关闭的回调方法
        websocket.onclose = function(){
            setSystemInfo("与服务器断开连接");
            // setTimeout(function(){
            //     setSystemInfo("正在尝试重新连接..");
            //     initWebSocket(websocket.target);
            // },1000);
        };
        //窗口关闭时 关闭socket;
        window.onbeforeunload = function(){
            if(websocket){
                websocket.close();
            }
        };
    }
    else{
        alert('浏览器不支持Websocket,请升级！');
    }
    return websocket;
}
