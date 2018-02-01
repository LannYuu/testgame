function MyWebSocket(url,callbacks){
    var that = this;
    if(url)
        this.target = url;
    if(callbacks){
        this.onerrorCallback = callbacks.onerror;
        this.onopenCallback = callbacks.onopen;
        this.onmessageCallback = callbacks.onmessage;
        this.oncloseCallback = callbacks.onclose;
    }
    if('WebSocket' in window){
        var initWebsocket = function(){
            if(that.websocket){
                that.websocket.close();
            }
            var websocket = new WebSocket(that.target);
            that.send = function (message) {
                websocket.send(message);
            };
            //连接发生错误的回调方法
            websocket.onerror = function(event){
                // setErrorInfo("");
                if (that.onerrorCallback && that.onerrorCallback instanceof Function)
                    that.onerrorCallback(event);
            };
            //连接成功建立的回调方法
            websocket.onopen = function(event){
                if (that.onopenCallback && that.onopenCallback instanceof Function)
                    that.onopenCallback(event);
            };
            //接收到消息的回调方法
            websocket.onmessage = function(event){
                // setMessage(event.data);
                if (that.onmessageCallback && that.onmessageCallback instanceof Function)
                    that.onmessageCallback(event);
            };
            //连接关闭的回调方法
            websocket.onclose = function(){
                // setSystemInfo("与服务器断开连接");
                if (that.oncloseCallback && that.oncloseCallback instanceof Function)
                    that.oncloseCallback(event);
            };
            //窗口关闭时 关闭socket;
            window.onbeforeunload = function(){
                if(websocket){
                    websocket.close();
                }
            };
            return websocket;
        };
        this.websocket = initWebsocket();
        this.reconnect = function(url){
            if(url)
                this.target = url;
            this.websocket = initWebsocket();
        };
    }
    else{
        alert('浏览器不支持Websocket,请升级！');
    }
}
