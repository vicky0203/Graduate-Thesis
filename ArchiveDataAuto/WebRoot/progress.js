var bar_color = "#FF3E96"; //进度条背景颜色
var no_color="";  //解决浏览器兼容设置的无颜色变量
var clear = "&nbsp;"; //清空背景颜色用的变量，如果该变量为空字符,非IE内核浏览器显示有问题

var tableNo = 0; //正在归档的数据表的编号 从0开始 与progressBar编号相对应

function process(){
	var archiveType = $("#archiveType").val();//归档类型 Condition 或者General
	var temp = $("#tableNum").val();//要归档的表的个数  取出来的是 string
	var totalTableNumber = 0;
	if(!(temp==""||temp==null||temp==undefined)){
		totalTableNumber = parseInt(temp);
	}
	//先判断是否所有表均未开始归档 待归档数为0
	var start = true;
	var test = 0;
	for(var count=0;count<totalTableNumber;count++){
		var recordNumber = parseInt($("#table"+count+"recordNum").val());
		if(recordNumber==test){
			if(count==totalTableNumber-1){
				start = false;
			}
		}
	}
	if(!start){
		window.location.href = "show_tables.jsp";
	}else{
		checkDiv(totalTableNumber);
		$.ajax({
			type: "POST",  
			url: "archiveProgressBarServlet?task=first",
			dataType:"text",
			async: false,
			success:function(){
				pollServer(tableNo,totalTableNumber,archiveType);
			}
		});
	}
}

//检查processBar所在div，设置其是否显示
function checkDiv(totalTableNumber) { 
	for (var i = 0; i <= totalTableNumber; i++) {
	   var process_bar = $("#progressBar"+i);
	   if(process_bar.css("visibility") == "visible"){
		   //清空processBar所在div
		   for (var j = 1; j <= 50; j++) {
				var elem = $("block"+j);
				elem.html(clear);
		    } 
		  $("#complete").html("");
	   }
    }
}

function pollServer(tableNo,totalTableNumber,archiveType) {
	console.log("archiveType:"+archiveType);
	var tN = tableNo;
	var tTN = totalTableNumber;
	var aType = archiveType;
	$.ajax({
		type: "POST",
		url: "archiveProgressBarServlet?task=poll",
		dataType:"text",
		data:{ tableName:$("#table"+tN).val(), tableNo:tN, totalTableNumber:tTN, //归档类型 Condition 或者General
			   recordNumber:$("#table"+tN+"recordNum").val(), archiveType:$("#archiveType").val()}, 
		async: false,
		success:function(percent_complete){
			pollCallback(tN,tTN,aType,percent_complete);
		}
	});
}

function pollCallback(table_no,total_table_number,ar_type,percent_complete) {
	  //var ar_t = ar_type;
	  console.log("aType:"+ar_type);
      console.log("tableNo:"+table_no);
	  var nowTable = $("#progressBar"+table_no);//当前正在归档的数据表对应的进度条	  
	  nowTable.css("visibility","visible");
	  
	  console.log("percent_complete:"+percent_complete); 
	  var recn = $("#table"+table_no+"recordNum").val();//当前正在归档的数据表总记录数
	  var recordNum = 0;
	  if(!(recn==""||recn==null||recn==undefined)){
		  recordNum = parseInt(recn);
	  }
	  console.log("recordNum:"+recordNum); 
	  
	  var per = recordNum/50;  // 显示在50个block上  计算每个block对应多少条记录
	  console.log("table"+table_no+"记录总数/50="+per);
	  var index = percent_complete / per; 		//计算显示block的个数 
	  console.log("table"+table_no+"index="+index);//100个百分点，显示在50个block上
		
	  for (var i = 1; i <= index; i++) {
 	       //console.log("#table"+table_no+"block"+i);
		   var elem = $("#table"+table_no+"block"+i);
		   elem.html(clear);
		   elem.css("backgroundColor",bar_color);
		   var htmlVal = "<font style='font-weight:bold;color:blue'>";
			   htmlVal +=percent_complete;
			   htmlVal +="</font>"; 
		   $("#table"+table_no+"finish").html(htmlVal);
      }
	
	  
	  //只要进度小于100%继续每隔1000毫秒调用一次pollServer()
	  if (table_no<total_table_number && percent_complete < recordNum) {  
		  console.log("F1 call back"+table_no+":"+total_table_number+":"+percent_complete+":"+recordNum);
	      setTimeout("pollServer("+table_no+","+total_table_number+",'"+ar_type+"')",155);  //传字符串参数要带单引号！！！
	  }else if(table_no<total_table_number && percent_complete == recordNum){
		  console.log(table_no+": Finish!!!");
		  console.log("F2 callback"+table_no+":"+total_table_number+":"+percent_complete+":"+recordNum);
		  table_no++;
		  if (table_no < total_table_number){
			  console.log("F3 callback"+table_no+":"+total_table_number+":"+percent_complete+":"+recordNum);
		      setTimeout("pollServer("+table_no+","+total_table_number+",'"+ar_type+"')",155);   
		  }		  
	  }
      
	  if(table_no==total_table_number) {
		  alert("Data Archive Finish!");
		  window.location.href = "show_tables.jsp";
	  }
	  
	  if(table_no > total_table_number || percent_complete > recordNum){
		   console.log("Calculation Error!");
		   if (percent_complete > recordNum){
			   console.log("F4 break!!!");
		   }
	  }	 
}