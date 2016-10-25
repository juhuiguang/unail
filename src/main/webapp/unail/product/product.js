/**
 * Created by 橘 on 2016/6/16.
 */
(function(){
    'use strict';
    var productapp=angular.module('unail.product', ["unail.shop"]);
    productapp.factory("productinstance",function(){return {}});
    /**
     * 产品查询
     */
    productapp.controller("productController",["$scope","productService","$uibModal","productinstance",function($scope,productService,$uibModal,productinstance){
        var vm=this;
        vm.products=[];
        vm.currenttype={};
        vm.currentkeywords="";
        vm.alert={ type:'danger', msg:'操作提示' }
        //获得大类
        var gettypes=function gettypes(){
            //获得产品大类型
            productService.getProductType(function(types){
                vm.producttypes=types;
                if(types!=null&&types.length>0){
                    productinstance.producttypes=vm.producttypes;
                    vm.producttypes[0].$isselected=true;
                    vm.currenttype=vm.producttypes[0];
                    changetype(vm.currenttype);
                }
            });
        }
        gettypes();
        function getType(type){
            for(var i=0;i<vm.producttypes.length;i++){
                if(vm.producttypes[i].name==type){
                    return vm.producttypes[i];
                }
            }
            return null;
        }
        //获得大类下产品
        function getps(keywords){
            productService.getProducts(vm.currenttype.name,keywords,function(products){
                vm.products=products;
            });
        }
        vm.getProducts=getps
        function changetype(type) {
            //console.log("changetype", vm.currenttype, type);
            vm.currenttype.$isselected = false;
            type.$isselected = true;
            vm.currenttype = type;
            vm.currentkeywords="";
            if (vm.currenttype != null) {
                getps(vm.currentkeywords);
            }
        }

        vm.loadtypeproduct=changetype;

        //新增弹出框
        function showAddProduct(){
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'unail/product/addProduct.html',
                controller: 'addProductController',
                bindToController:true,
                size: "lg",
                backdrop:false
            });

            modalInstance.result.then(function (result) {
                //保存成功
                if(result.result>0){
                    var p=result.data;
                    vm.alert.type="success";
                    vm.alert.msg="产品保存成功";
                    vm.alertflag="show";
                    changetype(getType(p.producttype1));
                }else{ //保存失败
                    vm.alert.type="danger";
                    vm.alert.msg="产品保存失败";
                    vm.alertflag="show";
                }
            }, function (flag) {
                if(flag.indexOf("back")>=0){
                    return false;
                }
            });
        }

        vm.showAdd=showAddProduct;

        vm.selectall=function selectall(){
            for(var i=0;i<vm.products.length;i++){
                if(vm.$isselectall){
                    vm.products[i].$isselected=true;
                }else{
                    vm.products[i].$isselected=!vm.products[i].$isselected;
                }
            }
        }

        vm.getSelections=function() {
            var selections = [];
            for (var i = 0; i < vm.products.length; i++) {
                if (vm.products[i].$isselected) {
                    selections.push(vm.products[i]);
                }
            }
            return selections;
        }

        function deleteProduct(){
            if(vm.getSelections().length==0){
                return;
            }
            var promitInstance = $uibModal.open({
                animation: true,
                templateUrl: 'system/common/promit.html',
                controller:function($scope,$uibModalInstance){
                    $scope.title="操作确认";
                    $scope.text="确认执行该删除操作吗？";
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
                for(var i=0;i<vm.products.length;i++){
                    console.log(vm.products,i);
                    if(vm.products[i].$isselected){
                        console.log(vm.products,i);
                        productService.deleteProduct(vm.products[i].productno,function(data){
                            console.log("delete callback",data);
                            if(data.result>0){
                                //刷新产品
                                vm.getProducts("");
                            }else{
                                vm.alert.type="danger";
                                vm.alert.msg=data.errormsg;
                                vm.alertflag="show";
                            }

                        });
                    }
                }
            });

        }

        vm.deleteProduct=deleteProduct;

        vm.showUpdate=function(p){
            productinstance.modify=p;
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'unail/product/updateProduct.html',
                controller: 'modifyProductController',
                bindToController:true,
                size: "lg",
                backdrop:false
            });

            modalInstance.result.then(function (result) {
                //保存成功
                if(result.result>0){
                    var p=result.data;
                    vm.alert.type="success";
                    vm.alert.msg="产品保存成功";
                    vm.alertflag="show";
                    changetype(getType(p.producttype1));
                }else{ //保存失败
                    vm.alert.type="danger";
                    vm.alert.msg="产品更新失败";
                    vm.alertflag="show";
                }
            }, function (flag) {
                if(flag.indexOf("back")>=0){
                    return false;
                }
            });
        }
        
        vm.operateProductType = operateProductType;
        
        function operateProductType() {
        	var modalInstance = $uibModal.open({
        		animation: true,
        		templateUrl: 'unail/product/productType.html',
        		controller: 'productTypeController',
        		bindToController: true,                                  
        		size: "sm",
        		backdrop: false    
        	});   
        	modalInstance.result.then(function(data) {
        		//添加保存成功
        		if(data.result > 0) {     
        			var ptype = data.data;   
        			console.log("ptype",ptype);                

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
     * 维护产品类型
     */
    productapp.controller("productTypeController",["$scope","productService","$uibModalInstance","$uibModal","productinstance",function($scope,productService,$uibModalInstance,$uibModal,productinstance){
    	$scope.ptypes = [];
    	$scope.ptypes = productinstance.producttypes;  
    	
    	$scope.showdddproducttype = showAddProductType;
    	function showAddProductType() {
    		$scope.$showaddptype = true;         
    	}
    	
    	$scope.addProductType = addProductType;
    	function addProductType(ptypename) {        
    		$scope.$showaddptype = false;        
    		productService.addProductType(ptypename,function(data){
    			console.log(data);
    			if(data.result >0) {
    				var ptype = data.data;
    				var producttype={
						name:ptype.producttypename,
						no:ptype.producttypeno
    				}
    				$scope.ptypes.push(producttype);  
    			}  
    		});
    	}
    	
    	$scope.updateproducttype = function updateproducttype(ptype) {
    		productService.updProductType(ptype,function(data){
    			if(data == "" || data == null) {

				} else {
					   
				}  
    		});
    	}   
    	
    	$scope.deleteproducttype = function deleteproducttype(ptypeno) {
    		var promitInstance = $uibModal.open({
                animation: true,
                templateUrl: 'system/common/promit.html',
                controller:function($scope,$uibModalInstance){
                    $scope.title="操作确认";
                    $scope.text="确认对该产品类型执行删除操作吗？";
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
    			productService.delProductType(ptypeno,function(data){
        			if(data.result >0) {
        				for(var i=0; i<$scope.ptypes.length; i++) {
        					if($scope.ptypes[i].no == ptypeno) {
        						$scope.ptypes.splice(i,1);
        					}
        				}
        			} 
        		});
    		});
    		
    	}
    	
    	$scope.cancel=function cancel(flag){
            $uibModalInstance.dismiss('cancel');
        }
    }]);
    
    /**
     * 新增产品
     */
    productapp.controller("addProductController",["$scope","productService","$uibModalInstance","productinstance","$rootScope",function($scope,productService,$uibModalInstance,productinstance,$rootScope){
        console.log("addcontroller",$scope);
        //新增产品默认值
        $scope.form={
            advoption:false
        }
        $scope.isallshops=true;
        $scope.producttypes=productinstance.producttypes;
        //如果调用controller时，类型并没有初始化，则调用获取类型方法
        if($scope.producttypes==null||$scope.producttypes.length==0){
            productService.getProductType(function(types){
                if(types!=null&&types.length>0) {
                    for (var i = 0; i < types.length; i++) {
                        $scope.producttypes.push({
                            name: types[i],
                            $isselected:false
                        });
                    }
                }
            });
        }

        $scope.save=function save(submitform){
            $scope.form.specialshop=$rootScope.rolepurview;
            $scope.form.shops=[];

            //不需要特殊指定每个门店差异
            if(!$scope.form.advoption){

            }else{
                //需要特殊指定门店差异
                for(var i=0;i<$scope.shops.length;i++){
                    if($scope.shops[i]!=null&& $scope.shops[i].$isselected){
                        console.log($scope.shops[i]);
                        if($scope.shops[i].price1==null||$scope.shops[i].price1==""){
                            $scope.shops[i].price1=$scope.form.price1;
                        }
                        if($scope.shops[i].price2==null||$scope.shops[i].price2==""){
                            $scope.shops[i].price2=$scope.form.price2;
                        }
                        console.log($scope.shops[i]);
                        $scope.form.shops.push($scope.shops[i]);
                    }
                }
            }

            //console.log($scope,$scope.addform,submitform,$scope.shops);

            $scope.loading=true;
            productService.addProduct($scope.form,function(result){
                $scope.loading=false;
                if(result){
                    $uibModalInstance.close(result);
                }else{
                    $scope.error={
                        haserror:true,
                        errormsg:"保存错误，请重试"
                    }
                }
            });
        }

        $scope.cancel=function cancel(flag){
            $uibModalInstance.dismiss('cancel');
        }
    }]);

    productapp.controller("modifyProductController",["$scope","productService","$uibModalInstance","productinstance","$timeout",function($scope,productService,$uibModalInstance,productinstance,$timeout){
        if(productinstance.modify==null)return;
        $scope.producttypes=productinstance.producttypes;
        $scope.form=productinstance.modify;
        $scope.form.advoption=true;
        $scope.shops=[];
        //加载门店数据
        productService.loadProductshops($scope.form.productno,function(result){
            //加载产品关联的门店
            //$scope.form.shops=result.data;
            //如果此时已经加载了备选门店
            if($scope.shops.length>0){
                setformShops(result.data);
            }else{
                //此时门店数据还未加载得到,3秒后再次执行,共重试两次
                $timeout(function(){
                    if($scope.shops.length>0){
                        setformShops(result.data);
                    }else{
                        $timeout(function(){
                            setformShops(result.data);
                        },1000)
                    }
                },500)
            }

            function setformShops(formshops){
                for(var i=0;i<formshops.length;i++){
                    for(var j=0;j<$scope.shops.length;j++){
                        if(formshops[i].shopno==$scope.shops[j].shopNo){
                            $scope.shops[j].$isselected=true;
                            $scope.shops[j].price1=formshops[i].productprice1;
                            $scope.shops[j].price2=formshops[i].productprice2;
                        }
                    }
                }
            }

        });

        $scope.form.shops=[];



        $scope.update=function(){
            //需要特殊指定门店差异
            for(var i=0;i<$scope.shops.length;i++){
                if($scope.shops[i]!=null&&$scope.shops[i].$isselected){
                    console.log($scope.shops[i]);
                    if($scope.shops[i].price1==null||$scope.shops[i].price1==""){
                        $scope.shops[i].price1=$scope.form.price1;
                    }
                    if($scope.shops[i].price2==null||$scope.shops[i].price2==""){
                        $scope.shops[i].price2=$scope.form.price2;
                    }
                    console.log($scope.shops[i]);
                    $scope.form.shops.push($scope.shops[i]);
                }
            }

            $scope.loading=true;
            productService.updateProduct($scope.form,function(result){
                $scope.loading=false;
                if(result){
                    $uibModalInstance.close(result);
                }else{
                    $scope.error={
                        haserror:true,
                        errormsg:"保存错误，请重试"
                    }
                }
            });
        }

        $scope.cancel=function cancel(flag){
            $uibModalInstance.dismiss('cancel');
        }
    }]);

    productapp.service("productService",["$http",function($http){
        this.getAllProducts=function(special,callback){
            var url='product/products/';
            $http({
                url:url,
                method:"POST",
                data:{special:special},
            }).then(function(response){
                callback(response.data);
            });
        }
        this.getProducts=function(protype,keywords,callback){
            console.log(protype,keywords);
            var url='product/products/'+protype;
            if(keywords!=null&&keywords!=""){
                url='product/products/'+protype+"/"+keywords;
            }
            console.log(url);
            $http({
                url:url,
                method:"GET"
            }).then(function(response){
                callback(response.data);
            });
        }
        this.getProductType=function(callback){
        	$http({
        		url:'producttype/producttypes',
        		method:"GET"
        	}).then(function(response){
        		var types=[];
        		for(var j=0; j<response.data.length; j++) {
        			types.push({
        				typename:response.data[j].producttypename,
        				typeno:response.data[j].producttypeno
        			});      
        		}              
        		var producttypes=[];          
                for(var i=0;i<types.length;i++){
                    producttypes.push({
                        name:types[i].typename,
                        no:types[i].typeno, 
                        $isselected:false
                    });
                } 
                callback(producttypes);
        	});

        }
        this.addProduct=function(product,callback){
            $http({
                url:'product/add',
                method:'POST',
                data:product
            }).then(function(response){
                callback(response.data);
            },function(response){

            });
        }
        //删除产品数据
        this.deleteProduct=function(productno,callback){
            $http({
                url:'product/delete/'+productno,
                method:'DELETE'
            }).then(function(response){
                console.log("product/delete",response);
                callback(response.data);
            });
        }
        //更新产品数据
        this.updateProduct=function(product,callback){
            $http({
                url:'product/update',
                method:'POST',
                data:product
            }).then(function(response){
                callback(response.data);
            },function(response){

            });
        }
        //加载产品对应的门店数据
        this.loadProductshops=function(productno,callback){
            $http({
                url:'product/getshops/'+productno,
                method:'POST'
            }).then(function(response){
                callback(response.data);
            },function(response){

            });
        }
        
        this.addProductType=function(ptypename,callback){
			if(ptypename == "" || ptypename == null) {
				return;
			}
        	$http({
        		url:'producttype/addproducttype',
        		method:'POST',
        		data:{
        			ptypename:ptypename
        		}
        	}).then(function(response) {
        		callback(response.data);
			},function(response){       
                
            });
        }
        
        this.delProductType=function(ptypeno,callback){
        	$http({
        		url:'producttype/delete/'+ptypeno,
        		method:'DELETE'
        	}).then(function(response){
        		callback(response.data);
        	});
        }
    }]);

    productapp.directive("productList",["productService","productinstance","$filter",function(productService,productinstance,$filter){
        return {
            restrict: 'EA', //E = element, A = attribute, C = class, M = comment
            scope:{
                rolepurview:'@', //权限
                mode:'@', //选择模式
                selectproducts:"="  //被选中的产品
            },
            templateUrl: 'unail/product/productlist.html',
            link: function(scope,element,attr,ctrl) {
                console.log("productlist link");
                //获得产品
                productService.getAllProducts(scope.rolepurview,function(data){
                    if(data.result>0){
                        scope.products=data.data;
                        scope.resultproducts=data.data;
                        productService.getProductType(function(data){
                            scope.producttypes=data;
                        });

                        for(var i=0;i<scope.resultproducts.lenth;i++){
                            scope.resultproducts[i].$isselected=false;
                        }
                    }else{
                        scope.error=data.errormsg;
                    }
                });
                scope.hasProduct=function(type){
                    for(var i=0;scope.resultproducts&&i<scope.resultproducts.length;i++){
                        if(scope.resultproducts[i].producttype1==type.name){
                            return true;
                        }
                    }
                    return false;
                }
                scope.typeselect=function(type){
                    var productarr=$filter('filter')(scope.resultproducts, { producttype1: type.name });
                    for(var i=0;i<productarr.length;i++){
                        productarr[i].$isselected=type.$isselected;
                    }
                    scope.getselect();
                }
                scope.getselect=function(){
                    scope.selectproducts=$filter('filter')(scope.resultproducts, { $isselected:true });
                }
                scope.filterproduct=function(){
                    scope.resultproducts=$filter('findproduct')(scope.products, scope.filtertext);
                    if(scope.resultproducts.length==1){
                        scope.resultproducts[0].$isselected=true;
                        scope.getselect();
                    }
                }
            }

        }
    }]);

    productapp.filter("findproduct",function(){
        var filter = function(input, findstr){
            console.log(input,findstr);
            var result=[];
            for(var i=0;i<input.length;i++){
                if(input[i].productname.indexOf(findstr)>=0||input[i].productletter.indexOf(findstr)>=0||input[i].productprice2==findstr){
                    result.push(input[i]);
                }
            }
            return result;
        };
        return filter;
    });
})()