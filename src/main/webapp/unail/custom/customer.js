/**
 * Created by 沈培 on 2016/6/24
 */

(function(){
	'user strict';
	var customapp = angular.module('unail.custom',[]);
	customapp.factory("custominstance",function(){return {}});
	
	customapp.controller("custommanageController",["$scope","customservice","$uibModal","custominstance",function($scope,customservice,$uibModal,custominstance){
		$scope.title = "客户管理";       
		$scope.customs = []; 
		$scope.searchkey = "";  
		customservice.getcustoms(function(data){
			console.log(data);   
			if(data == "" || data == null) {

			} else {      
				$scope.customs = data;
				dealCustomInfor($scope.customs);        
				
			}    
			
		});      
        
		function dealCustomInfor(customs) {
			for(var i=0; i<customs.length; i++) {
				if(customs[i].ifvip == "0") {               
					customs[i].ifvip = "否";          

				} else if(customs[i].ifvip == "1") {  
					customs[i].ifvip = "是";             
				}
				
				if(customs[i].wechathead == "" || customs[i].wechathead == null) {
					customs[i].wechathead = "image/user.png";      
				}                
				         
			    customs[i].firstconsumetime = new Date(parseInt(customs[i].firstconsumetime));
				customs[i].latelyconsumetime = new Date(parseInt(customs[i].latelyconsumetime));
   
			}          
		}
		
		$scope.updatecustom = showUpdCustom;
		
		//修改弹出框
		function showUpdCustom(updformcus) {
			custominstance.modify = updformcus;        
			var modalInstance = $uibModal.open({     
        		animation: true,
        		templateUrl: 'unail/custom/addCustom.html',
        		controller: 'modifyCustomController',      
        		bindToController: true,                               
        		size: "lg",
        		backdrop: false    
        	});
			
			modalInstance.result.then(function(data) {
				if(data.result >0) {
					
					$scope.alert.type = "success"; 
	    			$scope.alert.msg = "客户信息修改成功";
	    			$scope.alertflag = "show";   
	    			
	    		} else {
	    			$scope.alert.type = "danger";
	    			$scope.alert.msg = "客户信息修改失败";
	    			$scope.alertflag = "show";
	    		}
			}, function(flag) {
        		if(flag.indexOf("back") >= 0) {
        			return false;
        		}
        	});
			     
		}              
		
		$scope.addcustom = showAddCustom;
		
		//添加弹出框
        function showAddCustom(){
        	var modalInstance = $uibModal.open({
        		animation: true,
        		templateUrl: 'unail/custom/addCustom.html',
        		controller: 'addCustomController',
        		bindToController: true,                               
        		size: "lg",
        		backdrop: false    
        	});
        	
        	modalInstance.result.then(function(data) {
        		//添加保存成功
        		if(data.result > 0) {     
        			var cus = data.data;  
	   			    if(cus.ifvip == "0") {               
	 					cus.ifvip = "否";          
	 				} else if(cus.ifvip == "1") {    
	 					cus.ifvip = "是";             
	 				}
	   			    cus.firstconsumetime = new Date(parseInt(cus.firstconsumetime));    
					cus.latelyconsumetime = new Date(parseInt(cus.latelyconsumetime)); 
        			var custom={
        				  customno:cus.customno,   
				          customname:cus.customname,
				          customphone:cus.customphone,
				          custombirthday:cus.custombirthday,
				          customage:cus.customage,
				          ifvip:cus.ifvip,
				          vipcardno:cus.vipcardno,
				          firstconsumetime:cus.firstconsumetime,
				          latelyconsumetime:cus.latelyconsumetime,
				          customarea:cus.customarea
            		}
        			$scope.customs.push(custom);         
        			
        			$scope.alert.type = "success"; 
        			$scope.alert.msg = "客户信息添加成功";
        			$scope.alertflag = "show";   
        			
        		} else {
        			$scope.alert.type = "danger";
        			$scope.alert.msg = "客户信息添加失败";
        			$scope.alertflag = "show";
        		}
        	}, function(flag) {
        		if(flag.indexOf("back") >= 0) {
        			return false;
        		}
        	});
        }
		
        $scope.selectall = function selectall(){
    		for(var i=0; i<$scope.customs.length; i++) {
    			if($scope.$isselectall) {
    				$scope.customs[i].$isselected = true;
    			} else {
    				$scope.customs[i].$isselected = !$scope.customs[i].$isselected;
    			}  
    		}
    	}
        
        $scope.deletecustoms = deleteCustoms;
        
        $scope.getSelects = function() {
    		var selects = [];
    		for(var i=0; i<$scope.customs.length; i++) {
    			if($scope.customs[i].$isselected) {
    				selects.push($scope.customs[i]);
    			}
    		}
    		return selects;
    	}
        
        function deleteCustoms() { 
        	if($scope.getSelects().length == 0) {
    			return;
    		}
        	var promitInstance = $uibModal.open({
                animation: true,
                templateUrl: 'system/common/promit.html',
                controller:function($scope,$uibModalInstance){
                    $scope.title="操作确认";
                    $scope.text="确认对所选客户执行删除操作吗？";
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
    			var bjs = [];
    			for(var i=0; i<$scope.customs.length; i++) {
    				if($scope.customs[i].$isselected) {      
    					customservice.deletecustom($scope.customs[i].customno,function(data,customno){
    						if(data.result > 0) {
    							bjs.push(customno);         
    							for(var k=0; k<$scope.customs.length; k++) {     
    			    				for(var j=0; j<bjs.length; j++) {     
    			    					if($scope.customs[k].customno == bjs[j]) {           
    			    						$scope.customs.splice(k,1);                                
    			    					}
    			    				}     
    			    			}
    						}
    					});
    				}
    			}
    			
    		});
        }
        
        $scope.searchcustom = function searchcustom(searchkey) {
        	console.log("searchkey",searchkey);
			customservice.getSearchCus(searchkey,function(data) {
				if(data == "" || data == null) {

				} else {
					$scope.customs = data;  
					dealCustomInfor($scope.customs);       
				}    
			});
		}
        
		
		$scope.deletecustom = function deletecustom(customno) {
			console.log("custom_no" + customno);
			var promitInstance = $uibModal.open({
                animation: true,
                templateUrl: 'system/common/promit.html',
                controller:function($scope,$uibModalInstance){
                    $scope.title="操作确认";
                    $scope.text="确认执行该客户的删除操作吗？";
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
				customservice.deletecustom(customno,function(data){
					if(data.result > 0){ 
						for(var i=0;i<$scope.customs.length;i++){
							if($scope.customs[i].customno == customno) {
                                //刷新客户页面              
	                   			$scope.customs.splice(i,1);     
	                   			break;  
							}
						}
				 }
				});
            });
		}
		
	}]);
	
	
	/**
	 * 添加客户信息
	 */
	customapp.controller("addCustomController",["$scope","customservice","$uibModalInstance","$rootScope",function($scope,customservice,$uibModalInstance,$rootScope){
		$scope.ModTitle = "添加客户";          
		
		
		$scope.showvip = function() {
        	$scope.form.$showvipitem=true;
        }
        $scope.hidevip = function() {
            $scope.form.$showvipitem=false;
        }
		
        $scope.save = function save(cusform) {
        	$scope.form.firstconsumetime = new Date();
    		$scope.form.latelyconsumetime = new Date();   
        	if($scope.form.ifvip == "否") {               
				$scope.form.ifvip = "0";          

			} else if($scope.form.ifvip == "是") {      
				$scope.form.ifvip = "1";                       
			}       
        	customservice.addCustom($scope.form,function(data) {
        		console.log(data);
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
        
        $scope.cancel = function cancel(flag){
            $uibModalInstance.dismiss('cancel');
        }
	}]);    
	
	/**
	 *  修改客户信息
	 */
	customapp.controller("modifyCustomController",["$scope","customservice","$uibModalInstance","custominstance",function($scope,customservice,$uibModalInstance,custominstance){
		$scope.ModTitle = "修改客户";         
		$scope.form = custominstance.modify;   

		if($scope.form.ifvip == "否") {    
			$scope.form.$showvipitem=false;
		} else if($scope.form.ifvip == "是") {
			$scope.form.$showvipitem=true;
		}
		
		$scope.showvip = function() {
        	$scope.form.$showvipitem=true;
        }
        $scope.hidevip = function() {
            $scope.form.$showvipitem=false;
        }
		
		$scope.save = function save(cusform) {
			if($scope.form.ifvip == "否") {               
				$scope.form.ifvip = "0";          

			} else if($scope.form.ifvip == "是") {      
				$scope.form.ifvip = "1";                        
			}       
			customservice.updateCustom($scope.form,function(data) {
        		if(data.result > 0) {
    				if($scope.form.ifvip == "1") {          
    					$scope.form.ifvip = "是";
    				} else if($scope.form.ifvip == "0") {
    					$scope.form.ifvip = "否";      
    				}
    				$scope.form.firstconsumetime = new Date(parseInt(data.data.firstconsumetime));
    				$scope.form.latelyconsumetime = new Date(parseInt(data.data.latelyconsumetime));		
        			$uibModalInstance.close(data);       
        			
        		} else {   
        			$scope.error = {
        					haserror: true,
        					errormsg : "修改失败，您可以再试一次！"
        			}
        		}
			});
		}        
		
		$scope.cancel=function cancel(flag){
            $uibModalInstance.dismiss('cancel');
        }
	}]);
	
	
	customapp.controller("customqueryController",["$scope","customservice","custominstance","$uibModal",function($scope,customservice,custominstance,$uibModal){
		$scope.title = "客户查询";
		$scope.querycus = [];        
		$scope.querykey = "";
		$scope.querycustoms = function querycustoms(querykey) {
			console.log("querykey",querykey); 
			customservice.getQueryCus(querykey,function(data) {
				if(data == "" || data == null) {
					$scope.querycus = "";
				} else {
					$scope.querycus = data;     
					console.log("$scope.querycus",$scope.querycus);     
					//查询的客户数据进行处理
					for(var i=0; i<$scope.querycus.length; i++) {
						if($scope.querycus[i].ifvip == "0") {               
							$scope.querycus[i].ifvip = "否";          
						} else if($scope.querycus[i].ifvip == "1") {  
							$scope.querycus[i].ifvip = "是";             
						}
						if($scope.querycus[i].wechathead == "" || $scope.querycus[i].wechathead == null) {
							$scope.querycus[i].wechathead = "image/user.png";      
						}                    
						$scope.querycus[i].firstconsumetime = new Date(parseInt($scope.querycus[i].firstconsumetime));
						$scope.querycus[i].latelyconsumetime = new Date(parseInt($scope.querycus[i].latelyconsumetime));
					}          

				}    
			});
		}
		
		
		$scope.SelectCusSalesInfor = function SelectCusSalesInfor(customno) {
			custominstance.customno = customno;
			
        	var modalInstance = $uibModal.open({
        		animation: true,
        		templateUrl: 'unail/custom/customsalesinfor.html',
        		controller: 'showCusSalesInforController',
        		bindToController: true,                               
        		size: "lg",
        		backdrop: false    
        	});
	        	
        	modalInstance.result.then(function(data) {
        		//取数据成功
        		if(data.result > 0) {     

        			
        		} else {
        			
        		}
        	}, function(flag) {
        		if(flag.indexOf("back") >= 0) {
        			return false;
        		}
        	});

		}
		
		
	}]);
	
	customapp.controller("showCusSalesInforController",["$scope","customservice","custominstance","$uibModalInstance",function($scope,customservice,custominstance,$uibModalInstance){
		var customno = custominstance.customno;
		$scope.details = [];
		customservice.getCusSalesInfor(customno,function(data) {
			if(data == "" || data == null) {

			} else {
				console.log(data);
				$scope.customname = data[0].customname; 
				if(typeof(data[0].cardid) == "undefined") {
					$scope.cardflag = true;
				} else {
					$scope.cardflag = false;
				}
				$scope.cardsalesItems = data;
			}    
		});
		//消费明细查询
		customservice.getCusMoneyInfor(customno,function(data) {
			if(data == "" || data == null) {
				$scope.moneyflag = true;
			} else {

				$scope.moneyflag = false;
				$scope.moneysalesItems = data;
				$scope.moneyType=[];
				for(var i=0;i<data.length;i++){
					if($scope.moneyType.indexOf(data[i].productType)<0){
						$scope.moneyType.push(data[i].productType);
					}

				}
			}    
		});
		$scope.showdetail = function showdetail(cardsalesItem) {
			cardsalesItem.cardid,cardsalesItem.cardno,cardsalesItem.cardkindname
			$scope.details = [];
			console.log(cardsalesItem.cardno);
			console.log(cardsalesItem.cardkindname);
			$scope.cardno = cardsalesItem.cardno;
			$scope.cardname = cardsalesItem.cardkindname;
			console.log(cardsalesItem.cardid);
			customservice.getCusDetails(cardsalesItem.cardid,function(data) {
				if(data == "" || data == null) {

				} else {
					console.log(data);
					$scope.details = data;
				}
				cardsalesItem.$showdetails = true;
			});

			
		}
		
		$scope.hidedetails = function hidedetails(cardsalesItem) {
			cardsalesItem.$showdetails = false;
		}
		
		$scope.cancel=function cancel(flag){
            $uibModalInstance.dismiss('cancel');
        }
	}]);
	
	
	customapp.service("customservice",function customservice($http){
		this.getcustoms = function(callback){
			var url = 'custom/customs/';
			$http({
				url:url,
				method:"GET"
			}).then(function(response) {
				callback(response.data);
			});
		}
		
		this.deletecustom = function(customno,callback){    
            $http({
                url:'custom/delete/'+customno,
                method:'DELETE'
            }).then(function(response) {
                console.log("custom/delete",response);
                callback(response.data,customno);
            });
        }
		
		this.addCustom = function(custom,callback) {
			$http({
				url:'custom/add',
				method:'POST',
				data:custom
			}).then(function(response) {
				callback(response.data);
			},function(response){       

            });
		}
		
		this.getSearchCus = function(searchkey,callback) {
			if(searchkey == "" || searchkey == null) {
				var url = 'custom/customs/';
			}
			var url = 'custom/customs/'+searchkey; 
			$http({
				url:url,
				method:"GET"
			}).then(function(response) {
				callback(response.data);
			});
		}
		
		this.updateCustom = function(custom,callback) {
			$http({
				url:'custom/update',
				method:'POST',
				data:custom
			}).then(function(response){
        		callback(response.data);
        	},function(response){
        		
        	});
		}
		
		this.getQueryCus = function(querykey,callback) {
			if(querykey == "" || querykey == null) {
				callback("");
			} else {
				$http({
					url:'custom/query/'+querykey,
					method:"GET"
				}).then(function(response) {
					callback(response.data);
				});
			}
		}
		
		this.getCusSalesInfor = function(customno,callback) {
			if(customno == "" || customno == null) {
				return;
			} else {
				$http({
					url:'custom/selectCusSalesInfor/'+customno,
					method:"GET"
				}).then(function(response) {
					callback(response.data);
				});
			}
		}
		
		this.getCusMoneyInfor = function(customno,callback) {
			if(customno == "" || customno == null) {
				return;
			} else {
				$http({
					url:'custom/selectCusMoneyInfor/'+customno,
					method:"GET"
				}).then(function(response) {
					callback(response.data);
				});
			}
		}
		
		this.getCusDetails = function(cardid,callback) {
			if(cardid == "" || cardid == null) {
				return;
			} else {
				$http({
					url:'custom/selectDetails/'+cardid,
					method:"GET"
				}).then(function(response) {
					callback(response.data);
				});
			}
		}


		
	});

	customapp.directive("selectCustom",["customservice","$uibModal",function(customservice,$uibModal){
		return {
			restrict: 'EA', //E = element, A = attribute, C = class, M = comment
			scope: {
				selectcustom:"=",
				onselect:"&onselect"
			},
			templateUrl: 'unail/custom/customselect.html',
			link: function (scope, element, attr, ctrl) {
				scope.userselect={};
				loadcustom();
				function loadcustom(){
					customservice.getcustoms(function(result){
						if(result&&result.length>0){
							scope.hasuser=true;
							scope.customs=result.map(function (repo) {
								repo.value = repo.customno;
								return repo;
							});
						}else{
							scope.hasuser=false;
							scope.customs=[].map(function (repo) {
								repo.value = repo.customno;
								return repo;
							});
						}

						scope.userselect={
							isDisabled:false,
							searchTextChange:function(text){

							},
							selectedItemChange:function(item){
								scope.selectcustom=item;
								if(scope.onselect!=null&&item!=null){
									scope.onselect({item:item});
								}
							},
							querySearch:function(query){
								var results = query ? scope.customs.filter( createFilterFor(query) ) : scope.customs;
								return results;
							}
						}
						function createFilterFor(query) {
							var lowercaseQuery = angular.lowercase(query);
							return function filterFn(item) {
								return (item.customname.indexOf(query)>=0||(item.customphone&&item.customphone.indexOf(query)>=0)||(item.vipcardno&&item.vipcardno.indexOf(query)>=0));
							};
						}
					});
				}

				
		        //客户结算时添加新客户
				scope.clickAddCustom = function(){   
		        	var modalInstance = $uibModal.open({
		        		animation: true,
		        		templateUrl: 'unail/custom/addCustom.html',
		        		controller: 'addCustomController',
		        		bindToController: true,                                  
		        		size: "lg",
		        		backdrop: false    
		        	});   
		        	modalInstance.result.then(function(data) {
		        		//添加保存成功
		        		if(data.result > 0) {     
		        			var cus = data.data;
							loadcustom();
							scope.userselect.selectedItem = cus;
		        		} else {

		        		}
		        	}, function(flag) {
		        		if(flag.indexOf("back") >= 0) {
		        			return false;
		        		}
		        	});
		        }
				
			}
		}
	}]);
})();