/**
 * Created by 橘 on 2016/6/20.
 */
(function(){
    'user strict';
    var shopapp=angular.module('unail.shop',[]);
    shopapp.factory("shopinstance",function(){return {}});

    shopapp.controller("shopmanageController",["$scope","shopservice","$uibModal","shopinstance",function($scope,shopservice,$uibModal,shopinstance){
    	$scope.title = "门店维护";
    	$scope.shops = [];
    	shopservice.getshops("all",function(data){    
    		console.log(data);
    		if(data == "" || data == null) {      
    			
    		} else {
    			$scope.shops = data;
    			dealShopInfor($scope.shops);
    			
    		}
    	});
    	
    	function dealShopInfor(shops) {
    		for(var i=0; i<shops.length; i++) {      
    			if(shops[i].shopAddr != null && shops[i].shopAddr.length >15) {      
    				$scope.shops[i].shopAddrall = shops[i].shopAddr;
    				shops[i].shopAddr = shops[i].shopAddr.substring(0,14) + "...";     
    			   
    			}
    			shops[i].shoptime= new Date(parseInt(shops[i].shoptime));    
    		} 
    	}
    	
    	$scope.updateshop = showUpdShop;
    	
    	//修改弹出框
    	function showUpdShop(updformshop) {
    		shopinstance.modify = updformshop;
    		var modalInstance = $uibModal.open({     
        		animation: true,
        		templateUrl: 'unail/shop/addShop.html',
        		controller: 'modifyShopController',      
        		bindToController: true,                               
        		size: "lg",
        		backdrop: false    
        	});
    		
    		modalInstance.result.then(function(data) {
				if(data.result >0) {
					$scope.alert.type = "success"; 
	    			$scope.alert.msg = "门店信息修改成功";
	    			$scope.alertflag = "show";   
	    			
	    		} else {
	    			$scope.alert.type = "danger";
	    			$scope.alert.msg = "门店信息修改失败";
	    			$scope.alertflag = "show";
	    		}
			}, function(flag) {
        		if(flag.indexOf("back") >= 0) {
        			return false;
        		}
        	});
    		
    	}
    	
    	$scope.addshop = showAddShop;
    	
    	//添加弹出框
    	function showAddShop() {
    		var modalInstance = $uibModal.open({
        		animation: true,
        		templateUrl: 'unail/shop/addShop.html',
        		controller: 'addShopController',
        		bindToController: true,                               
        		size: "lg",
        		backdrop: false    
        	});
    		
    		modalInstance.result.then(function(data) {
        		//添加保存成功
        		if(data.result > 0) {     
        			var shop = data.data;  
	   			    shop.shoptime = new Date(parseInt(shop.shoptime));    
        			var shop={
        				  shopNo:shop.shopNo,
        				  shopName:shop.shopName,
        				  shopAddr:shop.shopAddr,
        				  shopcode:shop.shopcode,
        				  shoptime:shop.shoptime,
        				  shopPhone1:shop.shopPhone1,
        				  shopPhone2:shop.shopPhone2
            		}
        			$scope.shops.push(shop);               
        			
        			$scope.alert.type = "success"; 
        			$scope.alert.msg = "门店信息添加成功";
        			$scope.alertflag = "show";   
        			
        		} else {
        			$scope.alert.type = "danger";
        			$scope.alert.msg = "门店信息添加失败";
        			$scope.alertflag = "show";
        		}
        	}, function(flag) {
        		if(flag.indexOf("back") >= 0) {
        			return false;
        		}
        	});
    	}
    	
    	$scope.selectall = function selectall(){
    		for(var i=0; i<$scope.shops.length; i++) {
    			if($scope.$isselectall) {
    				$scope.shops[i].$isselected = true;
    			} else {
    				$scope.shops[i].$isselected = !$scope.shops[i].$isselected;
    			}
    		}
    	}
    	
    	$scope.deleteshops = deleteShops;
    	
    	$scope.getSelects = function() {
    		var selects = [];
    		for(var i=0; i<$scope.shops.length; i++) {
    			if($scope.shops[i].$isselected) {
    				selects.push($scope.shops[i]);
    			}
    		}
    		return selects;
    	}
    	
    	function deleteShops() {
    		if($scope.getSelects().length == 0) {
    			return;
    		}
    		var promitInstance = $uibModal.open({
                animation: true,
                templateUrl: 'system/common/promit.html',
                controller:function($scope,$uibModalInstance){
                    $scope.title="操作确认";
                    $scope.text="确认对所选门店执行删除操作吗？";
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
    			for(var i=0; i<$scope.shops.length; i++) {
    				if($scope.shops[i].$isselected) {
    					shopservice.deleteshop($scope.shops[i].shopNo,function(data,shopid){
    						if(data.result > 0) {
    							bjs.push(shopid);         
    							for(var k=0; k<$scope.shops.length; k++) {     
    			    				for(var j=0; j<bjs.length; j++) {     
    			    					if($scope.shops[k].shopNo == bjs[j]) {           
    			    						$scope.shops.splice(k,1);                    
    			    					}
    			    				}     
    			    			}
    						}
    					});
    				}
    			}
    			
    		});
    		
    	}
    	
    	$scope.searchshop = function searchshop(searchkey) {
    		shopservice.getSearchShop(searchkey,function(data) {
    			if(data == "" || data == null) {
    				
    			} else {
    				$scope.shops = data;
    				dealShopInfor($scope.shops);
    			}
    		});
    	}
    	
    	$scope.deleteshop = function deleteshop(shopid) {
    		console.log("shop_id" + shopid);
    		var promitInstance = $uibModal.open({
                animation: true,
                templateUrl: 'system/common/promit.html',
                controller:function($scope,$uibModalInstance){
                    $scope.title="操作确认";
                    $scope.text="确认执行该门店的删除操作吗？";
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
    			shopservice.deleteshop(shopid,function(data){
					if(data.result > 0) { 
						for(var i=0;i<$scope.shops.length;i++) {
							if($scope.shops[i].shopNo == shopid) {
                                //刷新门店页面              
	                   			$scope.shops.splice(i,1);     
	                   			break;  
							}
						}
					}
				});
            });
    		
    	}
    	
    }]);
    
    /**
     * 修改门店信息
     */
    shopapp.controller("modifyShopController",["$scope","shopservice","$uibModalInstance","shopinstance",function($scope,shopservice,$uibModalInstance,shopinstance){
    	$scope.ModTitle = "修改门店";
    	console.log("updshopController",$scope);         
    	$scope.form = shopinstance.modify;   
    	console.log($scope.form);         
    	$scope.form.advoption = true;
    	
    	$scope.save = function save(shopform) {    
			$scope.loading = true;
			shopservice.updateShop($scope.form,function(data) {
				console.log($scope.form);  
				console.log(data);
        		$scope.loading = false;
        		if(data.result > 0) {
    				$scope.form.shoptime = new Date(parseInt(data.data.shoptime));
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
    
    
    /**
     * 添加门店信息
     */
    shopapp.controller("addShopController",["$scope","shopservice","$uibModalInstance","$rootScope",function($scope,shopservice,$uibModalInstance,$rootScope){
    	$scope.ModTitle = "添加门店";      
    	console.log("addcontroller",$scope);
    	//添加门店默认值
    	$scope.form = {
    			advoption: false
    	}
    	$scope.save = function save(shopform) {
    		console.log(shopform);   
    		$scope.loading = true;
    		shopservice.addShop($scope.form,function(data) {
    			console.log($scope.form);
    			console.log(data);
    			$scope.loading = false;
    			if(data.result >0) {
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

	shopapp.controller("shopdailyController",["$scope","$mdDialog","$mdMedia","consumeservice","$rootScope","shopservice","$timeout","$filter",
		function($scope,$mdDialog, $mdMedia,consumeservice,$rootScope,shopservice,$timeout,$filter){
			var userpurview=$rootScope.user.purview;
			$scope.srchdate=new Date();
			$scope.cash=0;
			$scope.extra=0;
			$scope.customs=[];
			$scope.total=0;
			//获取门店
			shopservice.getshops(userpurview,function(result){
				$scope.shops=result;
				if($scope.shops instanceof Array){
					$scope.shop=$scope.shops[0];
				}else{
					$scope.shop=$scope.shops;
				}
				$scope.cshopNo=$scope.shop.shopNo;
				loaddata();
				$scope.loaddata=loaddata;
			});


			// $scope.$watch("shop",function(newvalue){
			// 	if(newvalue==null)return;
			// 	loaddata();
			// });

			function findcustom(cus){
				for(var i=0;i<$scope.customs.length;i++){
					if(cus==$scope.customs[i].name){
						return i;
					}
				}
				return -1;
			}
			function loaddata(){
				shopservice.getshopconsume($scope.cshopNo,$filter("date")($scope.srchdate,"yyyy-MM-dd"),function(result){
					if(result.result>0){
						$scope.consumes=result.data;//结算记录
						shopservice.getshopcardsale($scope.cshopNo,$filter("date")($scope.srchdate,"yyyy-MM-dd"),function(result){
							$scope.cash=0;
							$scope.extra=0;
							$scope.customs=[];
							$scope.total=0;
							if(result.result>0){
								$scope.salecards=result.data;//售卡记录
								for(var i=0;i<$scope.consumes.length;i++){
									$scope.cash+=$scope.consumes[i].cashprice;
									$scope.total+=$scope.consumes[i].pay_total;
									$scope.extra+=$scope.consumes[i].extraprice;
									if(findcustom($scope.consumes[i].customname)<0){
										$scope.customs.push({
											name:$scope.consumes[i].customname,
											no:$scope.consumes[i].customno
										})
									}
								}
								for(var i=0;i<$scope.salecards.length;i++){
									$scope.cash+=$scope.salecards[i].salesmoney;
									$scope.total+=$scope.salecards[i].sales;
									if(findcustom($scope.salecards[i].customername)<0){
										$scope.customs.push({
											name:$scope.salecards[i].customername,
											no:"temp"+i
										})
									}
								}
								console.log($scope);

							}else{
								$mdDialog.show(
									$mdDialog.alert()
										.clickOutsideToClose(true)
										.title('错误提示')
										.textContent(result.errormsg)
										.ok('确定')
								);
							}
						});
					}else{
						$mdDialog.show(
							$mdDialog.alert()
								.clickOutsideToClose(true)
								.title('错误提示')
								.textContent(result.errormsg)
								.ok('确定')
						);
					}
				});
			}
		}]);

	shopapp.service("shopservice",function shopservice($http){
        this.getshops=function(shopid,callback){
            var url='shop/shops/'+shopid;
            if(shopid=="all"||shopid=="ALL"){
                url='shop/shops/';
            }

            $http({
                url:url,
                method:"POST"
            }).then(function(response){
                callback(response.data);
            });
        }
        
        this.deleteshop = function(shopid,callback) {
        	$http({
        		url:'shop/delete/'+shopid,
        		method:'DELETE'
        	}).then(function(response) {
        		console.log("shop/delete",response);
        		callback(response.data,shopid);      
        	});
        }
             
        this.addShop = function(shop,callback) {
        	$http({
        		url:'shop/add',
        		method:'POST',
        		data:shop
        	}).then(function(response) {
        		callback(response.data);
        	},function(response) {
        		
        	});           
        }
                   
        this.getSearchShop = function(searchkey,callback) {
        	if(searchkey == "" || searchkey == null) {
        		var url = 'shop/shops/';
        	}
        	var url = 'shop/shops/'+searchkey;
        	$http({
        		url:url,
        		method:'GET'
        	}).then(function(response){
        		callback(response.data);
        	});
        }
        
        this.updateShop = function(shop,callback) {
        	$http({
        		url:'shop/update',
        		method:'POST',
        		data:shop
        	}).then(function(response){
        		callback(response.data);
        	},function(response){
        		
        	});
        }

        this.getshopconsume=function(shop,date,callback){
			$http({
				url:'shop/consumedetail',
				method:'POST',
				data:{
					shop:shop,
					date:date
				}
			}).then(function(response){
				callback(response.data);
			},function(response){

			});
		}

		this.getshopcardsale=function(shop,date,callback){
			$http({
				url:'shop/cardsaledetail',
				method:'POST',
				data:{
					shop:shop,
					date:date
				}
			}).then(function(response){
				callback(response.data);
			},function(response){

			});
		}

    });

    shopapp.directive("shopList",function shopList(shopservice){
        return {
            restrict: 'EA', //E = element, A = attribute, C = class, M = comment
            scope:{
                usershop:'@',
                mode:'@',
                shops:'=',
				selectitems:"@"
            },
            templateUrl: 'unail/shop/shoplist.html',
            link: function(scope,element,attr,ctrl) {
                console.log(scope,ctrl);
				shopservice.getshops(scope.usershop,function(data){
					console.log("getshops",data);
					if(data instanceof Array){
						scope.shops=data;
					}else{
						scope.shops=[data];
					}

					console.log("load",scope);
					setSelect();
				});
				function setSelect(){
					if(scope.selectitems!=null){
						if(scope.selectitems.split(",").length==scope.shops.length){
							scope.selectall=true;
						}else{
							scope.selectall=false;
						}
						for(var i=0;i<scope.shops.length;i++){
							if(scope.selectitems.indexOf(scope.shops[i].shopNo)>=0){
								scope.shops[i].$isselected=true;
							}else{
								scope.shops[i].$isselected=false;
							}
						}
					}
				}
                scope.selectAll=function(){
                    if(scope.mode=="multiselect"){
                        for(var i=0;(scope.shops&&i<scope.shops.length);i++){
                            scope.shops[i].$isselected=true;
                        }
                    }
                }
                scope.deselect=function(){
                    if(scope.mode=="multiselect"){
                        for(var i=0;(scope.shops&&i<scope.shops.length);i++){
                            scope.shops[i].$isselected=!scope.shops[i].$isselected;
                        }
                    }
                }
                scope.getselect=function(){
                    scope.selectshops=[];
                    for(var i=0;i<scope.shops.length;i++){
                        if(scope.shops[i].$isselected){
                            scope.selectshops.push(scope.shops[i]);
                        }
                    }
                }
                scope.radiochange=function(shop){
                	for(var i=0;i<scope.shops.length;i++){
                		if(scope.shops[i].shopNo==shop.shopNo){
                			continue;
						}else{
							scope.shops[i].$isselected=false;
						}
					}
				}
                
            }
        }
    });

})();