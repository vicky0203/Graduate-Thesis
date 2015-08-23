var bar_color = "#FF3E96"; //������������ɫ
var no_color="";  //���������������õ�����ɫ����
var clear = "&nbsp;"; //��ձ�����ɫ�õı���������ñ���Ϊ���ַ�,��IE�ں��������ʾ������

var tableNo = 0; //���ڹ鵵�����ݱ�ı�� ��0��ʼ ��progressBar������Ӧ

function process(){
	var archiveType = $("#archiveType").val();//�鵵���� Condition ����General
	var temp = $("#tableNum").val();//Ҫ�鵵�ı�ĸ���  ȡ�������� string
	var totalTableNumber = 0;
	if(!(temp==""||temp==null||temp==undefined)){
		totalTableNumber = parseInt(temp);
	}
	//���ж��Ƿ����б��δ��ʼ�鵵 ���鵵��Ϊ0
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

//���processBar����div���������Ƿ���ʾ
function checkDiv(totalTableNumber) { 
	for (var i = 0; i <= totalTableNumber; i++) {
	   var process_bar = $("#progressBar"+i);
	   if(process_bar.css("visibility") == "visible"){
		   //���processBar����div
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
		data:{ tableName:$("#table"+tN).val(), tableNo:tN, totalTableNumber:tTN, //�鵵���� Condition ����General
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
	  var nowTable = $("#progressBar"+table_no);//��ǰ���ڹ鵵�����ݱ��Ӧ�Ľ�����	  
	  nowTable.css("visibility","visible");
	  
	  console.log("percent_complete:"+percent_complete); 
	  var recn = $("#table"+table_no+"recordNum").val();//��ǰ���ڹ鵵�����ݱ��ܼ�¼��
	  var recordNum = 0;
	  if(!(recn==""||recn==null||recn==undefined)){
		  recordNum = parseInt(recn);
	  }
	  console.log("recordNum:"+recordNum); 
	  
	  var per = recordNum/50;  // ��ʾ��50��block��  ����ÿ��block��Ӧ��������¼
	  console.log("table"+table_no+"��¼����/50="+per);
	  var index = percent_complete / per; 		//������ʾblock�ĸ��� 
	  console.log("table"+table_no+"index="+index);//100���ٷֵ㣬��ʾ��50��block��
		
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
	
	  
	  //ֻҪ����С��100%����ÿ��1000�������һ��pollServer()
	  if (table_no<total_table_number && percent_complete < recordNum) {  
		  console.log("F1 call back"+table_no+":"+total_table_number+":"+percent_complete+":"+recordNum);
	      setTimeout("pollServer("+table_no+","+total_table_number+",'"+ar_type+"')",155);  //���ַ�������Ҫ�������ţ�����
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