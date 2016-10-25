/**
 *  Created by 沈培 on 2016/7/20
 */

(function(){
	'use strict';
	var arrangeapp = angular.module('unail.arrange',[]);
	arrangeapp.factory("arrangefactory",function(){
		var arr=[];
		for(var i=8;i<=21;i++){
			if(i<10){
				arr.push("0"+i+":00");
			}else{
				arr.push(i+":00");
			}
		}
		return {
			timearry:arr,
			dayarry:["星期日","星期一","星期二","星期三","星期四","星期五","星期六"]
		}
	});
	arrangeapp.controller("arrangeController",["$scope","arrangeservice","$uibModal","$filter","arrangefactory","shopservice","$rootScope",
		function($scope,arrangeservice,$uibModal,$filter,arrangefactory,shopservice,$rootScope){
		$scope.title = "预约查询";
		$scope.worktimes=arrangefactory.timearry;
		$scope.selecttime=new Date();
		$scope.startTime = "";
		$scope.endTime = "";
		$scope.currentshop = "";
		$scope.purview = "";
		console.log("$rootScope",$rootScope); 

		if($rootScope.user.purview == "ALL") {
			$scope.purview = true;
			$scope.clickShopItem = function clickShopItem(shopitem) {
				console.log(shopitem);     
				$scope.currentshop = shopitem.shopNo;
			}
		} else {
			$scope.purview = false;
			$scope.currentshop = $rootScope.user.purview;
		}

		shopservice.getshops($rootScope.user.purview,function(data){    
    		console.log(data);
    		if(data == "" || data == null) {  
    			
    		} else {
    			$scope.shops = data;
    		}
    	});

		$scope.$watch("selecttime",function(newvalue,oldvalue,scope){
			dealSearchTime();
		},true);

		function dealSearchTime() {
			console.log("searchtime",$scope.selecttime);
			$scope.selecttime.setHours(0,0,0,0);
			var nowWeekDay = $scope.selecttime.getDay() == 0 ?7:$scope.selecttime.getDay();	//获取当前星期X(1-7,7代表星期天)
			var nowTime = $scope.selecttime.getTime(); //当前时间戳
			//获得本周开始与截至时间段
			var nowWeekStar = new Date(nowTime - (nowWeekDay-1)*24*60*60*1000);
			var nowWeekEnd = new Date(nowTime + (7-nowWeekDay)*24*60*60*1000);
			$scope.startTime = nowWeekStar;
			$scope.endTime = nowWeekEnd;

			showRangeTimeCus($scope.startTime,$scope.endTime);
		}
		
		function showRangeTimeCus(startTime,endTime) {
			$scope.days=[];
			var daytime=$scope.startTime.getTime();
			while($scope.endTime.getTime()>=daytime){
				var currenttime=new Date(daytime);
				var d={
					date:currenttime,
					datestr:$filter("date")(daytime,"yyyy-MM-dd"),
					week:currenttime.getDay(),
					weekname:arrangefactory.dayarry[currenttime.getDay()]
				}
				$scope.days.push(d);
				daytime=daytime+24*60*60*1000;
			}
			var stime=$filter("date")(startTime,"yyyy-MM-dd HH:mm");
			var etime=$filter("date")(endTime,"yyyy-MM-dd HH:mm");
			arrangeservice.getRangeTimeCus(stime,etime,function(result){
				var data=result.data;
				for(var i=0;i<data.length;i++){
					var date=$filter("date")(data[i].arrangetime,"yyyy-MM-dd");
					var time=$filter("date")(data[i].arrangetime,"HH:mm");
					data[i].datestr=date;
					data[i].timestr=time;
				}
				$scope.arrangearry=data;
				console.log("$scope.arrangearry",$scope.arrangearry);  
			});
		}
		
		//上一周
		$scope.lastWeek = getLastWeekTime;
		function getLastWeekTime() {
			$scope.startTime = new Date($scope.startTime.getTime() - 24*60*60*1000*7);
			$scope.endTime = new Date($scope.endTime.getTime() - 24*60*60*1000*7);
			showRangeTimeCus($scope.startTime,$scope.endTime);
		}
		
		//下一周
		$scope.nextWeek = getNextWeekTime;
		function getNextWeekTime() {
			$scope.startTime =new Date($scope.startTime.getTime() + 24*60*60*1000*7);
			$scope.endTime = new Date($scope.endTime.getTime() + 24*60*60*1000*7);
			showRangeTimeCus($scope.startTime,$scope.endTime);
		}
		
		$scope.deletearrange = deleteArrange;
		
		function deleteArrange(arrangeno) {
			event.stopPropagation(); 
			console.log("arrangeno",arrangeno);
			
			var promitInstance = $uibModal.open({
                animation: true,
                templateUrl: 'system/common/promit.html',
                controller:function($scope,$uibModalInstance){
                    $scope.title="操作确认";
                    $scope.text="确认对此预约数据执行删除操作吗？";
                    $scope.cancel=function(){
                        $uibModalInstance.dismiss('cancel');
                    }
                    $scope.save=function(){
                        $uibModalInstance.close("ok");
                    }
                },
                backdrop:true
            });
			
			promitInstance.result.then(function(){
				arrangeservice.deletearrange(arrangeno,function(data){
					if(data.result > 0){ 
						for(var i=0; i<$scope.arrangearry.length; i++) {
							if($scope.arrangearry[i].arrangeno == arrangeno) {
								//删除后，刷新客户预约数据
								$scope.arrangearry.splice(i,1);
								break;
							}
						}   
						console.log(data);
					}
				});
            });
			
		}
		
		
		$scope.addArrange = function(date,time){
			if(date == null || time == null) {
				showAddArrange();
				
			} else {
				var t = [];
				t = time.split(":");
				if(date.getTime() < new Date().setHours(0,0,0,0)) {
					return;
					
				} else if(date.getTime() > new Date().setHours(0,0,0,0)) {
					showAddArrange(date,time)
					
				} else if(date.getTime() == new Date().setHours(0,0,0,0)) {
					if(parseInt(t[0]) < new Date().getHours()) {
						return;
					} else if(parseInt(t[0]) > new Date().getHours()) {
						showAddArrange(date,time) 
					}
				}
				
			}
			
		};

		//添加预约弹出框
		function showAddArrange(arrangedate,arrangetime) {
			if(arrangedate!=null){
				arrangefactory.arrangeconfig={
					arrangedate:arrangedate,
					arrangetime:arrangetime
				}
			}
			var modalInstance = $uibModal.open({
        		animation: true,
        		templateUrl: 'unail/arrange/addArrange.html',
        		controller: 'addArrangeController',
        		bindToController: true,                               
        		size: "lg",
        		backdrop: false    
        	});
			
			modalInstance.result.then(function(data) {
				//添加保存成功
				if(data.result > 0) {
					var arrange = data.data;
					var newarrange = {
							arrangeno:arrange.arrangeno,
							arrangeproduct:arrange.arrangeproduct,
							arrangeshop:arrange.arrangeshop,
							arrangetime:arrange.arrangetime,
							customname:arrange.customname,
							customphone:arrange.customphone,
							customtype:arrange.customtype,
							datestr:$filter("date")(arrange.arrangetime,"yyyy-MM-dd"),
							iffinish:arrange.iffinish,
							timestr:$filter("date")(arrange.arrangetime,"HH:mm")
					}
					$scope.arrangearry.push(newarrange);        
				} else {
					$scope.alert.type = "danger";
        			$scope.alert.msg = "预约信息添加失败";
        			$scope.alertflag = "show";
				}
			}, function(flag) {
				if(flag.indexOf("back") >= 0) {
        			return false;
        		}
        	});
		}

		
		$scope.showarrange = showSeeArrange;
		
		function showSeeArrange(arrange) {
			event.stopPropagation(); 
			arrangefactory.arrangeInfor = arrange;
			console.log("arrangeData",arrange);
			
			var modalInstance = $uibModal.open({
        		animation: true,
        		templateUrl: 'unail/arrange/addArrange.html',
        		controller: 'showArrangeController',
        		bindToController: true,                               
        		size: "lg",
        		backdrop: false    
        	});
			
			modalInstance.result.then(function(data) {
				if(data.result >0) {
	    			
	    		} else {

	    		}
			}, function(flag) {
        		if(flag.indexOf("back") >= 0) {
        			return false;
        		}
        	});
		}
		
	}]);
	
	/**
	 * 查看预约信息
	 */
	arrangeapp.controller("showArrangeController",["$scope","$uibModalInstance","$filter","arrangefactory","shopservice","productService",function($scope,$uibModalInstance,$filter,arrangefactory,shopservice,productService){
		$scope.ModTitle = "查看预约"; 		
		$scope.form = arrangefactory.arrangeInfor;
		$scope.products = [];
		$scope.productItems = [];
		var productnos = [];
		productnos = $scope.form.arrangeproduct.split(",");
		
		shopservice.getshops("all",function(data) {    
    		console.log(data);
    		if(data == "" || data == null) {
    			
    		} else {
    			for(var i=0; i<data.length; i++) {  
    				if($scope.form.arrangeshop == data[i].shopNo) {
    					$scope.form.arrangeshop = data[i].shopName;
    				}
    			}
    		}
    	});
		productService.getAllProducts("all",function(data){
            if(data.result>0){
                $scope.products=data.data;
                for(var j=0; j <productnos.length; j++) {
                	for(var i=0; i<$scope.products.length; i++) {
	                	if(productnos[j] == $scope.products[i].productno) {
	                		var productItem = {
	                				producttype1: $scope.products[i].producttype1,
	                				productname: $scope.products[i].productname
	                		}
	                		$scope.productItems.push(productItem);
	                		break;
	                	}
	                }
                }
                console.log($scope.productItems); 

            } else {
                
            }
        });
		
		$scope.cancel=function cancel(flag){
            $uibModalInstance.dismiss('cancel');
        }
	}]);
	
	/**
	 * 添加预约信息
	 */
	arrangeapp.controller("addArrangeController",["$scope","arrangeservice","$uibModalInstance","$mdDialog","$mdMedia","$filter","arrangefactory","$rootScope",
		function($scope,arrangeservice,$uibModalInstance,$mdDialog,$mdMedia,$filter,arrangefactory,$rootScope){
		var config=arrangefactory.arrangeconfig;

		$scope.ModTitle = "添加预约";
		$scope.purview = $rootScope.user.purview;
		$scope.timearry=arrangefactory.timearry;
		//预约添加的默认值
        $scope.form = {     
            advoption:false    
        }
		if(config!=null){
			$scope.form.arrangedate=config.arrangedate;
			$scope.form.arrangetime=config.arrangetime;
		}
        $scope.showproduct = function() {
        	$scope.form.$showproducts=true;
        }
        $scope.hideproduct = function() {
            $scope.form.$showproducts=false;
            for(var i=0; $scope.form.addproducts && i<$scope.form.addproducts.length;i++) {
            	var ishas=false;
                for(var j=0;j<$scope.form.products.length;j++){
                    if($scope.form.products[j].productno == $scope.form.addproducts[i].productno){
                        ishas=true;
                        break;
                    }
                }
                if(!ishas){
                    $scope.form.products.push($scope.form.addproducts[i]);
                }
            }
            
        }
        
        $scope.form.products=[];
        
        $scope.delproduct = function(product){
            for(var i=0; i<$scope.form.products.length; i++){
                if($scope.form.products[i].productno == product.productno){
                    $scope.form.products.splice(i,1);
                    break;
                }
            }
        }

        function getSelectedShop(){
        	var shops= $filter('filter')($scope.form.shops, { $isselected:true });
			if(shops.length>0){
				return shops[0];
			}else{
				return null;
			}
		}

        $scope.save = function save(arrangeform) {
        	console.log("save",$scope.form);
			$scope.form.arrangeshop=getSelectedShop();
			$scope.form.datestr=$filter("date")($scope.form.arrangedate,"yyyy-MM-dd");
			$scope.form.datestr+=" "+$scope.form.arrangetime;
        	arrangeservice.addArrange($scope.form,function(data){
        		console.log("addArrangeData",data);
        		if(data.result > 0) {
        			$uibModalInstance.close(data);  
        			
        		} else {
        			$scope.error = {
        					haserror: true,
        					errormsg: "添加失败，您可以再试一次！"
        			}
        		}
        	});
        }
        
		$scope.cancel = function cancel(flag) {
			$uibModalInstance.dismiss('cancel');
        }
	}]);
	
	arrangeapp.service("arrangeservice",function arrangeservice($http){
		this.getRangeTimeCus = function(startTime,endTime,callback) {
			console.log(startTime);
			console.log(endTime);
			if(startTime == "" || endTime == "") {
				return;
			} else {
				$http({
					url:'arrange/arranges',
					method:'POST',
					data:{
						startT:startTime,
						endT:endTime
					}
				}).then(function(response) {
					callback(response.data);
				});
			}
		}
		
		this.addArrange = function(arrange,callback) {
			$http({
				url:'arrange/add',
				method:'POST',
				data:arrange
			}).then(function(response) {
				callback(response.data);
			},function(response) {
				
			});
		}
		
		this.deletearrange = function(arrangeno,callback) {
			$http({
				url:'arrange/delete/'+arrangeno,
				method:'DELETE'
            }).then(function(response) {
                console.log("arrange/delete",response);
                callback(response.data,arrangeno);
            });
		}
		
		
	});   
})();