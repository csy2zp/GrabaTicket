    var t1 = 0;
    var autoBuyTicket = function(){
        var ticketType = [];
        var fromCity = $("input[name='fromCity']").val();
        var toCity = $("input[name='toCity']").val();
        var trainDate = $("input[name='trainDate']").val();
        var passengerTicketStr = ""
        var oldPassengerStr = ""
        var periodTime = [];
        $("input[name='periodTime']").val().split(",").forEach(function(v,i){
            periodTime.push({
                "startTime":v.split('-')[0],
                "endTime":v.split('-')[1]
            })
        });
                
        $("input[name='ticketType']:checked").each(function(i,v){
            ticketType.push(v.value);
        })
        $("input[name='users']:checked").each(function(i,v){
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
                    "toCity" : toCity ,
                    "trainDate" : trainDate ,
                    "passengerTicketStr" : passengerTicketStr,
                    "oldPassengerStr" : oldPassengerStr ,
                    "periodTime" : periodTime}
        var wait = false;
        t1 = window.setInterval(function(){
            if(wait)
                return;
            wait = true;
            $.ajax({
                    contentType:"application/json",
                    url: "/auto_buy_ticket",
                    dataType: "json",
                    type: "POST",
                    data: JSON.stringify(data),
                    success : function(r){
                        wait = false;
                        if(r.messageStatus == 'success'){
                            $("button").attr("style","width:100px;height: 40px;");
                            $(".loding").attr("style","display:none");
                            $(".stop").attr("style","display:none");
                            window.clearTimeout(t1);
                            alert("购票成功")           
                        }
                    },
                    error: function(e){
                        wait = false;
                        if(e.message == null){
                            console.log(e)
                        } else{
                            console.log(e);
                        }
                    }
            })
        },2000)
        
    };

    var stop = function(){
        $(".loding").attr("style","display:none");
        $("button").attr("style","width:100px;height: 40px;");
        $(".stop").attr("style","display:none");
        window.clearTimeout(t1);
    }