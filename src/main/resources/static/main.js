    var status = 1;
    var sub = function(){
        var stationTrainCode = $("input[name='stationTrainCode']").val();
        var fromCity = $("input[name='fromCity']").val();
        var toCity = $("input[name='toCity']").val();
        var trainDate = $("input[name='trainDate']").val();
        var ticketType = $("select").val();
        var imageLocation = $("input[name='imageLocation']").val();
        var passengerTicketStr = ""
        var oldPassengerStr = ""
        var keyCheckIsChange = "";
        var leftTicketStr = "";
        var trainLocation = "";
        
        var repeatSubmitToken = ""
        $("input[name='Fruit']:checked").each(function(i,v){
            passengerTicketStr += ticketType + ",0,1," + v.value.substring(0,v.value.length-1) + ",N_" 
            oldPassengerStr += v.value + "_";          
        });
        passengerTicketStr = passengerTicketStr.substring(0,passengerTicketStr.lastIndexOf("_"));

        $(".loding").attr("style","width:40px;height: 40px;");
        $("button").attr("style","display:none");
        if(status == 1){
            //预下单，如果没有图片验证就直接下单
            var data =  "stationTrainCode=" + stationTrainCode + "&" +
                        "fromCity=" + fromCity + "&" +
                        "toCity=" + toCity + "&" +
                        "trainDate=" + trainDate + "&" +
                        "passengerTicketStr=" + passengerTicketStr + "&" +
                        "oldPassengerStr=" + oldPassengerStr;
            $.ajax({
                contentType:"application/x-www-form-urlencoded",
                url: "/preOrder",
                dataType: "json",
                type: "POST",
                data: data,
                success : function(r){
                    $("button").attr("style","width:100px;height: 40px;");
                    $(".loding").attr("style","display:none");
                    if(r.status == 1){
                        keyCheckIsChange = r.keyCheckIsChange;
                        leftTicketStr = r.leftTicketStr;
                        trainLocation = r.train_location
                        repeatSubmitToken = r.REPEAT_SUBMIT_TOKEN;
                        status = 2;
                        $("button").text("验证图片");
                        $("#imageLocation").attr("style","display:block")
                        $("#image").attr("style","display:block")
                        $("#validationImg").attr("src",r.image);
                    } else{
                        if(r.messageStatus == 'success'){
                            alert("购票成功")
                        } else{
                            alert("没抢到，请重新抢")
                        }
                    }
                    
                },
                error: function(e){
                    $("button").attr("style","width:100px;height: 40px;");
                    $(".loding").attr("style","display:none");
                    if(e.message == null){
                        alert(e.responseJSON.message);
                    } else{
                        alert(e.message);
                    }
                }
            })
        } else{
            //确认下单
            var data =  "trainLocation=" + trainLocation + "&" +
                        "repeatSubmitToken=" + repeatSubmitToken + "&" +
                        "keyCheckIsChange=" + keyCheckIsChange + "&" +
                        "leftTicketStr=" + leftTicketStr + "&" +
                        "passengerTicketStr=" + passengerTicketStr + "&" +
                        "oldPassengerStr=" + oldPassengerStr + 
                        "imageLocation=" + imageLocation;
            $.ajax({
                contentType:"application/x-www-form-urlencoded",
                url: "/confirmOrder",
                dataType: "json",
                type: "POST",
                data: data,
                success : function(r){
                    $("button").attr("style","width:100px;height: 40px;");
                    $(".loding").attr("style","display:none");
                    if(r.messageStatus == 'success'){
                        alert("购票成功")
                    } else{
                        if(r.image != null){
                            $("#validationImg").attr("src",r.image);
                        }
                        alert(r.message)
                    }
                },
                error: function(e){
                    $("button").attr("style","width:100px;height: 40px;");
                    $(".loding").attr("style","display:none");

                    if(e.message == null){
                        alert(e.responseJSON.message);
                    } else{
                        alert(e.message);
                    }
                }
            })
        }
    }