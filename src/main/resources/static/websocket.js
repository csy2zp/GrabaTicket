var ws = null;
var t1 = 0;
var startWebSocket = function(){
   if ("WebSocket" in window){      
      // 打开一个 web socket
      ws = new WebSocket("ws://localhost:8200/websocket");
      ws.onopen = function()
      {
         var t1 = window.setInterval(function(){
         ws.send("{\"messageType\":3}")
      },50000); 
      };
       
      ws.onmessage = function (evt) 
      { 
         var received_msg = evt.data;
         alert(received_msg.message);
      };
       
      ws.onclose = function()
      { 
         // 关闭 websocket
         window.clearTimeout(t1);
         alert("连接已关闭..."); 
      };
   } else{
      // 浏览器不支持 WebSocket
      alert("您的浏览器不支持 WebSocket!");
   }
}