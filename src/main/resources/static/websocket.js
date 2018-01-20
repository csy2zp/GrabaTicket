if ("WebSocket" in window){      
      // 打开一个 web socket
      var ws = new WebSocket("ws://localhost:8200/websocket");
      ws.onopen = function()
      {
         // Web Socket 已连接上，使用 send() 方法发送数据
         alert("发送数据...");
         ws.send("{\"mqid\":1,\"messageType\":1,\"userName\":\"" + "csy2zp" +"\"}");
      };
       
      ws.onmessage = function (evt) 
      { 
         var received_msg = evt.data;
         console.log(received_msg);
      };
       
      ws.onclose = function()
      { 
         // 关闭 websocket
         alert("连接已关闭..."); 
      };
} else{
   // 浏览器不支持 WebSocket
   alert("您的浏览器不支持 WebSocket!");
}