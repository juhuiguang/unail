/**
 * 
 */

(function(){
	'use strict';
	var staffapp = angular.module('unail.staff',[]);
	staffapp.factory("staffinstance",function(){return {}});
	
	staffapp.controller("staffmanageController",["$scope","staffservice","$uibModal","staffinstance","shopservice","$rootScope",function($scope,staffservice,$uibModal,staffinstance,shopservice,$rootScope){
		$scope.title = "员工维护";  
		$scope.staffs = [];
		$scope.shops = [];
		$scope.currentshop = "";    
		$scope.purview = "";
		console.log("$rootScope",$rootScope); 
		
		if($rootScope.user.purview == "ALL") {
			$scope.purview = true;

		} else {
			$scope.purview = false;
			$scope.currentshop = $rootScope.user.purview;
		}
        $scope.clickShopItem = function clickShopItem(shopitem) {
            console.log(shopitem);
            $scope.currentshop = shopitem.shopNo;

            staffservice.getstaffs(function(data){
                console.log("staff",data);
                if(data == "" || data == null) {

                } else {
                    for(var i=0; i<data.length; i++) {
                        if(data[i].staffstate == "0") {
                            data[i].staffstate = "正常";
                        } else if(data[i].staffstate == "1") {
                            data[i].staffstate = "异常";
                        }
                        data[i].staffentrytime = new Date(parseInt(data[i].staffentrytime));
                    }

                    $scope.staffs = data;

                }
            },shopitem.shopNo);

        }
		
		shopservice.getshops("all",function(data){
    		console.log(data);
    		if(data == "" || data == null) {      
    			
    		} else {
    			$scope.shops = data;
    		}
    	});
		
		staffservice.getstaffs(function(data){
			console.log("staff",data);      
			if(data == "" || data == null) {
				
			} else {
				for(var i=0; i<data.length; i++) {
					if(data[i].staffstate == "0") {
						data[i].staffstate = "正常";
					} else if(data[i].staffstate == "1") {
						data[i].staffstate = "异常";
					}
					data[i].staffentrytime = new Date(parseInt(data[i].staffentrytime));
				}
				
				$scope.staffs = data;
				
			}
		},"all");
		
		$scope.addstaff = function addstaff() {
			var modalInstance = $uibModal.open({
        		animation: true,
        		templateUrl: 'unail/staff/addStaff.html',
        		controller: 'addStaffController',
        		bindToController: true,                               
        		size: "lg",
        		backdrop: false    
        	});
			
			modalInstance.result.then(function(data) {
				//添加保存成功
        		if(data.result > 0) {  
        			var staff = data.data;
        			
        			if(staff.staffstate == "0") {
        				staff.staffstate = "正常";
        			} else if(staff.staffstate == "1") {
        				staff.staffstate = "异常";
        			}
        			staff.staffentrytime = new Date(parseInt(staff.staffentrytime));
        			
        			var staffdata = {
        					staffno:staff.staffno,
        					staffname:staff.staffname,
        					staffphone:staff.staffphone,
        					staffage:staff.staffage,
        					staffentrytime:staff.staffentrytime,
        					staffshop:staff.staffshop, 
        					shopName:staffinstance.shopname,   
        					staffstate:staff.staffstate
        			}
        			$scope.staffs.push(staffdata);

        		} else {
        			    
        		}
			},function(flag) {
				if(flag.indexOf("back") >= 0) {
        			return false;
        		}
			});
		}
		
		
		$scope.updatestaff = function updatestaff(staff) {
			staffinstance.modify = staff;
			var modalInstance = $uibModal.open({     
        		animation: true,
        		templateUrl: 'unail/staff/addStaff.html',
        		controller: 'modifyStaffController',      
        		bindToController: true,                               
        		size: "lg",
        		backdrop: false    
        	});
			
			modalInstance.result.then(function(data) {
				if(data.result >0) {
					console.log(data)
					var staff = data.data;
					for(var i=0; i<$scope.staffs.length; i++) {
						if($scope.staffs[i].staffid == staff.staffid) {
							$scope.staffs[i].staffno = staff.staffno,
							$scope.staffs[i].staffname = staff.staffname,
							$scope.staffs[i].staffphone = staff.staffphone,
							$scope.staffs[i].staffage = staff.staffage,
							$scope.staffs[i].staffshop = staff.staffshop
						}
						break;
					}
					$scope.currentshop = staff.staffshop;  
	    		} else {

	    		}
			}, function(flag) {
        		if(flag.indexOf("back") >= 0) {
        			return false;
        		}
        	});
			
		}
		
		$scope.searchstaff = function searchstaff(searchkey) {
			console.log("searchkey",searchkey);
			staffservice.searchStaff(searchkey,function(data) {
				if(data == "" || data == null) {
					
				} else {
					for(var i=0; i<data.length; i++) {
						if(data[i].staffstate == "0") {
							data[i].staffstate = "正常";
						} else if(data[i].staffstate == "1") {
							data[i].staffstate = "异常";
						}
/*						data[i].staffentrytime = new Date(parseInt(data[i].staffentrytime));
*/					}
					$scope.staffs = data;
				}
			});
		}
		
		$scope.deletestaff = function deletestaff(staffid) {
			console.log("staffid",staffid);
			var promitInstance = $uibModal.open({
                animation: true,
                templateUrl: 'system/common/promit.html',
                controller:function($scope,$uibModalInstance){
                    $scope.title="操作确认";
                    $scope.text="确认执行该员工的删除操作吗？";
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
				staffservice.deletestaff(staffid,function(data){
					if(data.result > 0){ 
						for(var i=0;i<$scope.staffs.length;i++){
							if($scope.staffs[i].staffid == staffid) {
                                //刷新客户页面              
	                   			$scope.staffs.splice(i,1);     
	                   			break;  
							}
						}
					}
				});
            });
		}    
		
		$scope.selectall = function selectall() {
			for(var i=0; i<$scope.staffs.length; i++) {
				if($scope.$isselectall) {
					$scope.staffs[i].$isselected = true;
				} else {
					$scope.staffs[i].$isselected = !$scope.staffs[i].$isselected;
				}
			}  
		}
		
		$scope.getSelects = function() {
			var selects = [];
			for(var i=0; i<$scope.staffs.length; i++) {
				if($scope.staffs[i].$isselected) {
					selects.push($scope.staffs[i]);
				}
			}
			return selects;
		}
		
		$scope.deletestaffs = function deletestaffs() {
			if($scope.getSelects().length == 0) {
				return;
			}
			var promitInstance = $uibModal.open({
                animation: true,
                templateUrl: 'system/common/promit.html',
                controller:function($scope,$uibModalInstance){
                    $scope.title="操作确认";
                    $scope.text="确认对所选员工执行删除操作吗？";
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
    			for(var i=0; i<$scope.staffs.length; i++) {
    				if($scope.staffs[i].$isselected) {      
    					staffservice.deletestaff($scope.staffs[i].staffid,function(data,staffid){
    						if(data.result > 0) {
    							bjs.push(staffid);         
    							for(var k=0; k<$scope.staffs.length; k++) {     
    			    				for(var j=0; j<bjs.length; j++) {     
    			    					if($scope.staffs[k].staffid == bjs[j]) {           
    			    						$scope.staffs.splice(k,1);                                
    			    					}
    			    				}     
    			    			}
    						}
    					});
    				}
    			}
    			
    		});
		}
		      
	}]);
	
	staffapp.controller("addStaffController",["$scope","staffservice","$uibModalInstance","$filter","staffinstance","$rootScope",
		function($scope,staffservice,$uibModalInstance,$filter,staffinstance,$rootScope) {
		$scope.ModTitle = "添加员工";
		$scope.usershop=$rootScope.user.purview;
		function getSelectedShop(){
        	var shops= $filter('filter')($scope.form.shops, { $isselected:true }); 
			if(shops.length>0){
				return shops[0];
			}else{
				return null;
			}
		}
		
		$scope.save = function save(satffform) {
			$scope.form.staffshop = getSelectedShop();
			var shopname = $scope.form.staffshop.shopName;
			staffinstance.shopname = shopname;       
			staffservice.addStaff($scope.form,function(data) {
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
	
	staffapp.controller("modifyStaffController",["$scope","staffservice","$uibModalInstance","$rootScope","$filter","staffinstance",function($scope,staffservice,$uibModalInstance,$rootScope,$filter,staffinstance){
		$scope.ModTitle = "修改员工"; 		
		$scope.form = staffinstance.modify;

/*		console.log($scope.form.staffshop);
		for(var i=0; i<$scope.form.shops.length; i++) {
			if($scope.form.staffshop == $scope.form.shops[i].shopNo) {        
				$scope.form.shops[i].$isselected = true;
			}        
		}*/
		
		function getSelectedShop(){
        	var shops= $filter('filter')($scope.form.shops, { $isselected:true });
        	console.log($scope.form.shops);   
			if(shops.length>0){
				return shops[0];
			}else{
				return null;    
			}
		}
		
		$scope.save = function save(staffform) {
			$scope.form.staffshop = getSelectedShop();
			var shopname = $scope.form.staffshop.shopName;
			if($scope.form.staffstate == "正常") {
				$scope.form.staffstate = "0";
			} else if($scope.form.staffstate == "异常") {
				$scope.form.staffstate = "1";
			}
			$scope.form.staffentrytime = staffinstance.modify.staffentrytime;
			console.log($scope.form.staffentrytime);  
			
			staffservice.updateStaff($scope.form,shopname,function(data) {
				if(data.result >0) {
					if($scope.form.staffstate == "0") {
						$scope.form.staffstate = "正常";
					} else if($scope.form.staffstate == "1") {
						$scope.form.staffstate = "异常";
					}
					$scope.form.shopName = shopname;
					$scope.form.staffentrytime = staffinstance.modify.staffentrytime;
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
	
	staffapp.service("staffservice",function staffservice($http){
		this.getstaffs = function(callback,shop) {
			$http({
				url:'staff/shopstaffs/'+shop,
				method:"GET"
			}).then(function(response){
				callback(response.data);
			});
		}
		
		this.deletestaff = function(staffid,callback) {
			$http({
                url:'staff/delete/'+staffid,
                method:'DELETE'
            }).then(function(response) {
                console.log("staff/delete",response);
                callback(response.data,staffid);
            });
		}
		
		this.addStaff = function(staff,callback) {
			$http({
				url:'staff/add',
				method:'POST',
				data:staff
			}).then(function(response) { 
				callback(response.data);    
			},function(response){       

            });
		}
		
		this.updateStaff = function(staff,shopname,callback) {
			$http({
				url:'staff/update',
				method:'POST',
				data:staff
			}).then(function(response){
        		callback(response.data,shopname);
        	},function(response){
        		
        	});
		}
		
		this.searchStaff = function(searchkey,callback) {
			if(searchkey == "" || searchkey == null) {
				var url = 'staff/staffs/'
			}
			var url = 'staff/staffs/'+searchkey;
			$http({
				url:url,
				method:"GET"
			}).then(function(response) {
				callback(response.data);
			});
		}

	});
	
	staffapp.directive("selectStaff",["staffservice","$uibModal",function(staffservice,$uibModal){
		return {
			restrict: 'EA',
			scope: {
				selectstaff:"=",
				shop:"@"
			},
			templateUrl:'unail/staff/staffselect.html',
			link:function(scope,element,attr,ctrl) {
				scope.staffselect = {};
				staffservice.getstaffs(function(result) {
					if(result && result.length>0) {
						scope.hasstaff = true;
						scope.staffs = result.map(function(rep) {
							rep.value = rep.staffno;
							return rep;
						});
					} else {
						scope.hasstaff = false;
						scope.staffs = [].map(function(rep) {
							rep.value = rep.staffno;
							return rep;
						});
					}
					
					scope.staffselect = {
							isDisabled:false,
							searchTextChange:function(text) {
								
							},
							selectedItemChange:function(item) {
								scope.selectstaff=item;
							},
							querySearch:function(query) {
								var results = query ? scope.staffs.filter(createFilterFor(query)): scope.staffs;
								return results;
							}
					}
					function createFilterFor(query) {
						var lowercaseQuery = angular.lowercase(query);
						return function filterFn(item) {
							return (item.staffname.indexOf(query)>=0 || item.staffno.indexOf(query)>=0);	
						};
					}
				},scope.shop);
				
			}
		}
	}]);
	
})();