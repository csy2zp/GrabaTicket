    var autoBuyTicket = function(){
        var ticketType = $("input[name='ticketType']").val();
        var fromCity = $("input[name='fromCity']").val();
        var toCity = $("input[name='toCity']").val();
        var trainDate = $("input[name='trainDate']").val();
        var passengerTicketStr = ""
        var oldPassengerStr = ""
        var periodTime = $("input[name='periodTime']").val().split(",");
                
        var repeatSubmitToken = ""
        $("input[name='Fruit']:checked").each(function(i,v){
            passengerTicketStr += "#,0,1," + v.value.substring(0,v.value.length-1) + ",N_" 
            oldPassengerStr += v.value + "_";          
        });
        passengerTicketStr = passengerTicketStr.substring(0,passengerTicketStr.lastIndexOf("_"));

        $(".loding").attr("style","width:40px;height: 40px;");
        $("button").attr("style","display:none");
        $(".stop").attr("style","width:100px;height: 40px;");
        //预下单，如果没有图片验证就直接下单
        var data = {"ticketType" : ticketType,
                    "fromCity" : fromCity,
                    "toCity" : toCity,
                    "trainDate" : trainDate,
                    "passengerTicketStr" : passengerTicketStr,
                    "oldPassengerStr" : oldPassengerStr,
                    "periodTime" : periodTime,
                    "messageType":1;
                }
        ws.send(JSON.stringify(data));
    };

    var stop = function(){
        $(".loding").attr("style","display:none");
        $("button").attr("style","width:100px;height: 40px;");
        $(".stop").attr("style","display:none");
        ws.send(JSON.stringify({"messageType":2}}));
    }