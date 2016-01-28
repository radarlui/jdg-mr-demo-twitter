/**
 * 
 */
var app = angular.module('dashboard',  ['angularFileUpload']);
function getGridCount($scope,$http){
	$http.get('rest/mr/MDRGetSize').success(function(resp){
		$scope.dataRecords = resp;
		});
}
function getMDRs($scope,$http,count){
	$http.get('rest/mr/MDRGet?count='+count).success(function(resp){
		$scope.mdrs = resp;
});
}
function getClusterStats($scope,$http){
	$http.get('rest/mr/clusterStats').success(function(resp){
    	$scope.clusterName= resp.clusterName;
    	$scope.nodeCount = resp.clusterMembers.length;
    	$("#nodes").text("");
    	for (i=0;i<resp.clusterMembers.length;i++){
        	var list = $('#nodes');
        	list.simpleGrid({
        		  margin      : 0,
        		  initialSize : 0,
        		  minSize     : 0,
        		});
    		list.append("<li><div class='node'><p align='center'><img src='images/server-alt1_button.png' style='height:40px;'/></p><p align='center'>" + resp.clusterMembers[i] + "</p></div></li>");
        	//$("#nodes").append("<div class='node'><p align='center'><img src='images/server-alt1_button.png' style='height:40px;'/></p><p align='center'>" + resp.clusterMembers[i] + "</p></div>");
            }   	
	});
}
function drawChart(div,data){
	var width = 300,
    height = 250,
    radius = Math.min(width, height) / 2;
	
	var color = d3.scale.ordinal().range(["#3366CC","#DC3912","#FF9900","#109618","#990099"]);
	var arc = d3.svg.arc()
    .outerRadius(radius - 10)
    .innerRadius(0);

var pie = d3.layout.pie()
    .sort(null)
    .value(function(d) { return d.value; });
	
d3.select(div).html('');
var svg = d3.select(div).append("svg")
    .attr("width", width)
    .attr("height", height)
    .append("g")
    .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

	  var g = svg.selectAll(".arc")
	      .data(pie(data))
	    .enter().append("g")
	      .attr("class", "arc");

		  g.append("path")
		      .attr("d", arc)
		      .style("fill", function(d) { return color(d.data.key); });
		
		  g.append("text")
		      .attr("transform", function(d) { return "translate(" + arc.centroid(d) + ")"; })
		      .attr("dy", ".35em")
		      .style("text-anchor", "middle")
		      .text(function(d) { return d.data.key; });
}

app.controller('formCtrl', function($scope,$http,FileUploader) {

	getGridCount($scope,$http);
	getMDRs($scope,$http,20);	
	getClusterStats($scope,$http);	        

	$scope.create= function(mdrFileP){
		$http({method:'POST',url:'rest/mr/MDRUpload',data:{mdrFile:mdrFileP}	,headers: {'Content-Type': 'multipart/form-data'},transformRequest: formDataObject }).success(function(resp){
		});
		};
    
    $scope.execMRCountry = function(){
    	$("#statsChart").html("");
        start = Date.now();
    	$http.get('rest/mr/processCountry').success(function(data){
			$scope.keyLabel = 'Country';
        	$scope.bills = data;
        	end = Date.now();
        	$scope.execMRTime = (end-start) + 'ms';
        	});  
    	getGridCount($scope,$http);
    	getMDRs($scope,$http,10);
    };
    $scope.execMRClientType = function(){
        start = Date.now();
    	$http.get('rest/mr/processClientType').success(function(data){
    		$scope.keyLabel='Client Type';
        	$scope.bills = data;
        	end = Date.now();
        	$scope.execMRTime = (end-start) + 'ms';
        	drawChart("#statsChart", data);
        	});
    	getGridCount($scope,$http);
    	getMDRs($scope,$http,10);    	
    };

    var billInterval;
    $("#billBtn").on('click',function(e){
		this.disabled=true;
		$("#busyDiv").addClass("spinner");
         $scope.execMRCountry();
         clearInterval(statsInterval);
         billInterval = setInterval(function(){$scope.execMRCountry()}, 1000);
         this.disabled=false;
         $("#busyDiv").removeClass("spinner");
        });
    var statsInterval;
    $("#statsBtn").on('click',function(e){
		this.disabled=true;
		$("#busyDiv").addClass("spinner");
         stats = $scope.execMRClientType();
         clearInterval(billInterval);
         statsInterval = setInterval(function(){$scope.execMRClientType()}, 1000); 

         this.disabled=false;
        $("#busyDiv").removeClass("spinner");
         
        });
    
    $("#refreshGrid").on('click',function(e){ 
        	getGridCount($scope,$http);
        	getMDRs($scope,$http,20);
        	
      });
    $("#refreshNodes").on('click',function(e){ 
    	getClusterStats($scope,$http);	
  		});
	$("#fileUploadModal").on('show.bs.modal',function(){
		document.getElementById('fup').value='';
		$scope.uploader.clearQueue();
		$scope.uploader.progress=0;
		});
	$('#fileUploadModal').on('hide.bs.modal',function(){
		getGridCount($scope,$http);
		getMDRs($scope,$http,20);	
	});

	$scope.uploader= new FileUploader({url:'rest/mr/MDRUpload',alias:'mdrFile'});
	
	$scope.uploader.onSuccessItem= function(fileItem, response, status, headers){$('#fileUploadModal').modal('hide')};
	
});
