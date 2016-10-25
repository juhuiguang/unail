/**
 * Created by 橘 on 2016/7/1.
 */
(function() {
    'use strict';
    var cardapp = angular.module('unail.card', ["unail.product","unail.custom","ngMaterial","ui.router"]);
    cardapp.value("cardkindtype",[{name:"充值卡"},{name:"疗程卡"},{name:"促销卡"},{name:"其他"}]);
    cardapp.config(["$stateProvider",function($stateProvider){
        $stateProvider.state("product.card",{
            url: '/product/card',
            views: {
                "":{
                    title: '卡券维护',
                    templateUrl: 'unail/card/card.html',
                    controller:"cardManageController"
                },
                "addCard@product.card": {
                    templateUrl:"unail/card/addCard.html",
                    controller:"addCardController"
                }
            }

        })
        .state("shop.salecard",{
            url: '/shop/salecard',
            title: '卡券销售',
            templateUrl: 'unail/card/cardkindlist.html',
            controller:"salecardmainController"
        });
    }]);
    cardapp.factory("cardinstance",function(){return {}});

    cardapp.controller("cardManageController",["$scope","$timeout","$mdSidenav","cardkindtype","cardservice","$mdToast","$uibModal","cardinstance","$mdDialog"
        ,function($scope, $timeout, $mdSidenav,cardkindtype,cardservice,$mdToast,$uibModal,cardinstance,$mdDialog){
            $scope.alert={ type:'danger', msg:'操作提示' };
            $scope.alertflag="";
        $scope.openAdd =function(){
            $scope.openresult={result:null};
            $mdSidenav("cardform",true).then(function(instance){
               instance.open();
            });
        };
            $scope.$watch("openresult",function(newvalue,oldvalue,scope){
                console.log("go into watch>>>",newvalue,oldvalue,scope);
                if(newvalue!=null&&newvalue.result!=null){
                    var type=newvalue.result.type;
                    for(var i=0;i<$scope.cardkindtypes.length;i++){
                        if($scope.cardkindtypes[i].name==type){
                            $scope.loadtypecardkind($scope.cardkindtypes[i]);
                            break;
                        }
                    }
                }
            },true);
        $scope.alltype=true;
        $scope.cardkindtypes=cardkindtype;

        $scope.loadallcardkind=function(){
            for(var i=0;i<$scope.cardkindtypes.length;i++){
                $scope.cardkindtypes[i].$isselected=false;
            }
            $scope.alltype=true;
            cardservice.getCardKinds({name:"全部"},function(data){
                console.log("loadallcardkind",data);
                $scope.cardkinds=data.data;
            });
        };
            $scope.loadallcardkind();
        $scope.loadtypecardkind=function(type){
            $scope.alltype=false;
            for(var i=0;i<$scope.cardkindtypes.length;i++){
                $scope.cardkindtypes[i].$isselected=false;
            }
            type.$isselected=true;

            cardservice.getCardKinds(type,function(data){
                console.log("loadallcardkind",data);
                $scope.cardkinds=data.data;
            });
        };
            $scope.delcardkind=function(cardkind){
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
                    cardservice.delCardkind(cardkind,function(result){
                        if(result.result>0){
                            for(var i=0;i<$scope.cardkinds.length;i++){
                                if($scope.cardkinds[i].cardkindno==cardkind.cardkindno){
                                    $scope.cardkinds.splice(i,1);
                                    break;
                                }
                            }
                        }else{
                            $mdToast.show(
                                $mdToast.simple()
                                    .textContent(result.errormsg)
                                    .position({top:true} )
                                    .hideDelay(2000));
                        }
                    });
                });
            }
            $scope.changestatus=function(cardkind){
                if(cardkind.cardkindstatus==1){
                    cardkind.cardkindstatus=0;
                }else{
                    cardkind.cardkindstatus=1;
                }
                cardservice.updateCardkindStatus(cardkind,function(result){
                    if(result.result>0){
                        // $mdToast.show(
                        //     $mdToast.simple()
                        //         .textContent(result.message)
                        //         .hideDelay(1000));
                        $mdToast.showSimple(result.message);
                    }else{
                        $mdToast.show(
                            $mdToast.simple()
                                .textContent(result.errormsg)
                                .hideDelay(1000));
                    }

                });
            }
            $scope.openCardManage=function(cardkind){
                cardinstance.cardkind=cardkind;
                $mdDialog.show({
                    controller: "cardListController",
                    templateUrl: 'unail/card/cardmanage.html',
                    clickOutsideToClose:false
                })
            };

            //修改卡片种类
            $scope.openupdatekind=function(cardkind){
                cardinstance.cardkind=cardkind;
                $mdDialog.show({
                    controller: "updatecardkindController",
                    templateUrl: 'unail/card/addCard.html',
                    clickOutsideToClose:false
                })
                .then(function(result) {
                    $mdToast.showSimple(result.message);
                }, function() {

                });
            };

            //售卖卡片
            $scope.salecard=function(cardkind){
                cardinstance.cardkind=cardkind;
                $mdDialog.show({
                    templateUrl: 'unail/card/createcard.html',
                    controller:"salecardController",
                    clickOutsideToClose:false
                }).then(function(data){
                    cardkind.cardcount++;
                });
            }
            //批量生成卡片
            $scope.gencard=function(cardkind){
                cardinstance.cardkind=cardkind;
                $mdDialog.show({
                    templateUrl: 'unail/card/createcardmulti.html',
                    controller:"gencardController",
                    clickOutsideToClose:false
                }).then(function(data){
                    cardkind.cardcount+=data.value;
                });
            }
    }]);
    cardapp.controller("addCardController",["$scope","$timeout","$mdSidenav","productService","$rootScope","cardservice","cardkindtype",
        function($scope, $timeout, $mdSidenav,productService,$rootScope,cardservice,cardkindtype){
            $scope.form={};
        function initaddform() {
            $scope.cardkindtypes = cardkindtype;
            $scope.balancetypes = ["金额结算", "次数结算"];
            $scope.form.$showproducts = false;
            $scope.form.title = '添加新卡片种类';
            $scope.form.cardkindtype = "充值卡";
            $scope.form.balancetype = '金额结算';
            $scope.form.cardtimes = 0;
            $scope.form.cardmoney = 0;
            $scope.form.products=[];
        };

        $scope.showproduct=function(){
            $scope.form.$showproducts=true;
        }
        $scope.hideproduct=function(){
            $scope.form.$showproducts=false;
            for(var i=0;$scope.form.addproducts&&i<$scope.form.addproducts.length;i++){
                $scope.form.addproducts[i].servediscount=$scope.form.servediscount;
                $scope.form.addproducts[i].servetimes=$scope.form.servetimes;
                $scope.form.addproducts[i].servecycle=$scope.form.servecycle;
                var ishas=false;
                for(var j=0;j<$scope.form.products.length;j++){
                    if($scope.form.products[j].productno==$scope.form.addproducts[i].productno){
                        ishas=true;
                        break;
                    }
                }
                if(!ishas){
                    $scope.form.products.push($scope.form.addproducts[i]);
                }
            }
        }
        
        initaddform();

        $scope.save=function(){
            //console.log("save",$scope.form);
            $scope.form.cardkindduetime2=new Date($scope.form.cardkindduetime).getTime();
            cardservice.addCardKind($scope.form,function(result){
                console.log("addcardkindresult",result);
                if($scope.openresult){
                    $scope.openresult.result=result;
                    $scope.openresult.result.type=$scope.form.cardkindtype;
                }
                $mdSidenav('cardform').close().then(function(){
                    //console.log("before",$scope);
                    initaddform();
                    //console.log("after",$scope);
                });
            });
        }

            $scope.delproduct=function(product){
                for(var i=0;i<$scope.form.products.length;i++){
                    if($scope.form.products[i].productno==product.productno){
                        $scope.form.products.splice(i,1);
                        break;
                    }
                }
            }

        $scope.cancel = function () {
            // Component lookup should always be available since we are not using `ng-if`
            $mdSidenav('cardform').close().then(function(){
                //console.log("before",$scope);
                initaddform();
                //console.log("after",$scope);
            });

        };

            console.log("addscope",$scope);
    }]);
    cardapp.controller("updatecardkindController",["$scope","$mdDialog","cardinstance","cardkindtype","$filter","cardservice",function($scope,$mdDialog,cardinstance,cardkindtype,$filter,cardservice){
        $scope.ispop=true;//弹出显示标注，可不使用
        $scope.cardkindtypes=cardkindtype;
        $scope.balancetypes = ["金额结算", "次数结算"];
        var cardkind=cardinstance.cardkind;

        cardkind.cardkindduetime=new Date(cardkind.cardkindduetime);
        console.log(cardkind.cardkindduetime.toLocaleDateString());
        $scope.form=cardkind;
        $scope.form.title="修改卡片种类信息";


        if($scope.form.ifcalculatesales == "1") {
            $scope.form.ifcalculatesales = true;
        } else if($scope.form.ifcalculatesales == "0") {
            $scope.form.ifcalculatesales = false;
        }

        cardservice.loadCardkindProducts(cardkind.cardkindno,function(rep){
            //console.log("loadCardkindProducts result",rep);
            $scope.form.products=rep.data;
        });

        $scope.showproduct=function(){
            $scope.form.$showproducts=true;
        }
        $scope.hideproduct=function(){
            $scope.form.$showproducts=false;
            for(var i=0;$scope.form.addproducts&&i<$scope.form.addproducts.length;i++){
                $scope.form.addproducts[i].servediscount=$scope.form.servediscount;
                $scope.form.addproducts[i].servetimes=$scope.form.servetimes;
                $scope.form.addproducts[i].servecycle=$scope.form.servecycle;
                var ishas=false;
                for(var j=0;j<$scope.form.products.length;j++){
                    if($scope.form.products[j].productno==$scope.form.addproducts[i].productno){
                        ishas=true;
                        break;
                    }
                }
                if(!ishas){
                    $scope.form.products.push($scope.form.addproducts[i]);
                }
            }
        }

        $scope.delproduct=function(product){
            for(var i=0;i<$scope.form.products.length;i++){
                if($scope.form.products[i].productno==product.productno){
                    $scope.form.products.splice(i,1);
                    break;
                }
            }
        }

        $scope.save=function(){
            console.log("date before>>",$scope.form.cardkindduetime);
            $scope.form.cardkindduetime2=new Date($scope.form.cardkindduetime).getTime();
            console.log("date after>>",$scope.form.cardkindduetime)
            cardservice.updateCardKind($scope.form,function(result){
                console.log("updatecardkindresult",result);
                $mdDialog.hide(result);
            });
        }
        $scope.cancel=function(){
            $mdDialog.cancel();
        }
    }]);
    cardapp.controller("cardListController",["$scope","$timeout","$mdToast","cardinstance","cardservice","$mdDialog","$uibModal",
        function($scope, $timeout,$mdToast,cardinstance,cardservice,$mdDialog,$uibModal){  
        $scope.modal={};
        $scope.modal.cardkind=cardinstance.cardkind;

        cardservice.getCards($scope.modal.cardkind.cardkindno,1,10,function(data){
            $scope.cards=data.data.content;
        });

        $scope.selectall = function selectall(){
        	for(var i=0; i<$scope.cards.length; i++) {
        		if($scope.$isselectall) {
        			$scope.cards[i].$isselected = true;
        		} else {
        			$scope.cards[i].$isselected = !$scope.cards[i].$isselected;
        		}
        	}
        }
        
        $scope.getSelects = function() {
        	var selects = [];
        	for(var i=0; i<$scope.cards.length; i++) {
        		if($scope.cards[i].$isselected) {
        			selects.push($scope.cards[i]);
        		}
        	}
        	return selects;
        }
        
        $scope.deleteCards = function deleteCards() {
        	if($scope.getSelects().length == 0) {
        		return;
        	}
        	var promitInstance = $uibModal.open({
                animation: true,
                templateUrl: 'system/common/promit.html',
                controller:function($scope,$uibModalInstance){
                    $scope.title="操作确认";
                    $scope.text="确认对所选卡片执行删除操作吗？";
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
    			for(var i=0; i<$scope.cards.length; i++) {
    				if($scope.cards[i].$isselected) {      
    					cardservice.deletecard($scope.cards[i].cardid,function(data,cardid){
    						if(data.result > 0) {
    							bjs.push(cardid);         
    							for(var k=0; k<$scope.cards.length; k++) {     
    			    				for(var j=0; j<bjs.length; j++) {     
    			    					if($scope.cards[k].cardid == bjs[j]) {           
    			    						$scope.cards.splice(k,1);                                
    			    					}
    			    				}     
    			    			}
    						}
    					});
    				}
    			}
    			
    		});
        	
        }
        
        $scope.updateCardstatus = function updateCardstatus(flag,cardid) {
        	console.log("cardid",flag,cardid);  
        	cardservice.updatecardstatus(flag,cardid,function(data,flag,cardid){
        		if(data == "" || data == null) {

				} else {
					if(data.result>0) {
						for(var i=0; i<$scope.cards.length; i++) {
							if($scope.cards[i].cardid == cardid) {
								if(flag == "disable") {
									$scope.cards[i].cardstatus = "0";
									
								} else if(flag == "enable") {  
									$scope.cards[i].cardstatus = "1";
								}
								break;   
							}
						}						
					}
				}    
        	});
        }
        
        $scope.cancel=function(){
            $mdDialog.cancel();
        }

    }]);

    cardapp.controller("salecardmainController",["$scope","cardinstance","$mdDialog","cardservice","$mdToast","$rootScope","cardkindtype",
        function($scope,cardinstance,$mdDialog,cardservice,$mdToast,$rootScope,cardkindtype){
            $scope.alltype=true;
            $scope.cardkindtypes=cardkindtype;

            $scope.loadallcardkind=function(){
                for(var i=0;i<$scope.cardkindtypes.length;i++){
                    $scope.cardkindtypes[i].$isselected=false;
                }
                $scope.alltype=true;
                cardservice.getCardKinds({name:"全部"},function(data){
                    console.log("loadallcardkind",data);
                    $scope.cardkinds=data.data;
                });
            };
            $scope.loadallcardkind();
            $scope.loadtypecardkind=function(type){
                $scope.alltype=false;
                for(var i=0;i<$scope.cardkindtypes.length;i++){
                    $scope.cardkindtypes[i].$isselected=false;
                }
                type.$isselected=true;

                cardservice.getCardKinds(type,function(data){
                    console.log("loadallcardkind",data);
                    $scope.cardkinds=data.data;
                });
            };
            //售卖卡片
            $scope.salecard=function(cardkind){
                cardinstance.cardkind=cardkind;
                $mdDialog.show({
                    templateUrl: 'unail/card/createcard.html',
                    controller:"salecardController",
                    clickOutsideToClose:false
                }).then(function(data){
                    cardkind.cardcount++;
                });
            };

    }]);
    cardapp.controller("salecardController",["$scope","cardinstance","$mdDialog","cardservice","$mdToast","consumeservice","$rootScope",
        function($scope,cardinstance,$mdDialog,cardservice,$mdToast,consumeservice,$rootScope){
        cardservice.getCardKinds({name:"全部"},function(data){
            $scope.cardkinds=data.data;
            if($scope.cardkind!=null){
                $scope.setCurrentCardkind($scope.cardkind.cardkindno);
            }
        });
        $scope.cardkind=cardinstance.cardkind;
        $scope.setCurrentCardkind=function(cardkind){
            for(var i=0;i<$scope.cardkinds.length;i++){
                if($scope.cardkinds[i].cardkindno==cardkind){
                    $scope.cardkind=$scope.cardkinds[i];
                    var duetime=new Date($scope.cardkind.cardkindduetime);
                    $scope.cardduetime=duetime;
                    return;
                }
            }
        }
        $scope.price=$scope.cardkind.cardkindsales;
        $scope.remainprice=$scope.cardkind.cardkindsales;//当前结算剩余价格
        $scope.cash=0;//已付现金价格
        $scope.dealprice=0;//卡片抵用价格
        $scope.custom=null;//当前客户
        $scope.useCard=function(card){
            console.log("use card",card);
            card.$isused=true;
            if(!card.cardkind.cardkindrate)card.cardkind.cardkindrate=1;
            if(card.surplussales*card.cardkind.cardkindrate>$scope.remainprice){
                card.dealprice=Math.round($scope.remainprice/card.cardkind.cardkindrate);
                card.surplussales-=Math.round($scope.remainprice/card.cardkind.cardkindrate);
                if(card.surplussales<0)card.surplussales=0;
                $scope.dealprice+=$scope.remainprice;
            }else{
                $scope.dealprice+=Math.round(card.surplussales*card.cardkind.cardkindrate);
                card.dealprice=card.surplussales;
                card.surplussales=0;
            }
            if($scope.dealprice>$scope.price){
                $scope.dealprice=$scope.price;
            }
        }
        $scope.unuseCard=function(card){
            console.log("unuse card",card);
            card.$isused=false;
            if(!card.cardkind.cardkindrate)card.cardkind.cardkindrate=1;
            if(card.dealprice>0){
                $scope.dealprice-=Math.round(card.dealprice*card.cardkind.cardkindrate);
                card.surplussales+=card.dealprice;
                card.dealprice=0;
            }

        }
        var calc=function(){
            $scope.remainprice=$scope.price-$scope.cash-$scope.dealprice;
        }
        $scope.calc=function(){
            calc();
        }
        $scope.$watch("dealprice",function(newvalue,oldvalue){
            calc();
        });
        $scope.$watch("custom",function(newvalue,oldvalue){
            if(newvalue==null)return;
            consumeservice.loadUserCard(newvalue.customno,function(result){
                var data=result.data;
                var usercards=[];
                console.log("loadusercard",data);
                for(var i=0;i<data.length;i++){
                    var cardkind=data[i].cardkind;
                    if(cardkind.balancetype=="金额结算"&&cardkind.cardkindtype=="充值卡"&&data[i].surplussales>0){
                        usercards.push(data[i]);
                    }
                }
                $scope.customcards=usercards;
            });
        });
        
        $scope.cancel=function(){
            $mdDialog.cancel();
        }

        $scope.newcardseq=function(){
            cardservice.newCardSeq($scope.cardkind,function(result){
                if(result.result>0){
                    $scope.cardseq=result.data.value;
                }else{
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent(result.errormsg)
                            .hideDelay(1000));
                }
            })
        }
        $scope.save=function(){
            var form={
                cardkind:$scope.cardkind,
                cardseq:$scope.cardseq,
                price:$scope.price,
                dealprice:$scope.dealprice,
                cash:$scope.cash,
                cards:$scope.customcards,
                cardduetime:$scope.cardduetime.getTime(),
                custom:$scope.custom,
                staff:$rootScope.user
            }
            cardservice.newCard(form,function(result){
                if(result.result>0){
                    $mdDialog.hide(result.data);
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent(result.message)
                            .hideDelay(1000));
                }else{
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent(result.errormsg)
                            .hideDelay(1000));
                }
            });
        }

    }]);
    cardapp.controller("gencardController",["$scope","cardinstance","$mdDialog","cardservice","$mdToast",function($scope,cardinstance,$mdDialog,cardservice,$mdToast){
        cardservice.getCardKinds({name:"全部"},function(data){
            $scope.cardkinds=data.data;
            if($scope.cardkind!=null){
                $scope.setCurrentCardkind($scope.cardkind.cardkindno);
            }
        });
        $scope.cardkind=cardinstance.cardkind;
        $scope.setCurrentCardkind=function(cardkind){
            for(var i=0;i<$scope.cardkinds.length;i++){
                if($scope.cardkinds[i].cardkindno==cardkind){
                    $scope.currentcardkind=$scope.cardkinds[i];
                    $scope.currentcardkindno=$scope.cardkinds[i].cardkindno;
                    return;
                }
            }
        };

        $scope.save=function(){
            var form={
                cardkind:$scope.cardkind,
                cardseqstart:$scope.cardnostart,
                cardseqend:$scope.cardnoend,
                cardduetime:$scope.cardduetime.getTime()
            }
            cardservice.genCards(form,function(result){
                if(result.result>0){
                    $mdDialog.hide(result.data);
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent(result.message)
                            .hideDelay(1000));
                }else{
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent(result.errormsg)
                            .hideDelay(1000));
                }
            });
        }

        $scope.cancel=function(){
            $mdDialog.cancel();
        };
    }]);

    //注册卡券的服务
    cardapp.service("cardservice",["$http","$rootScope",function($http,$rootScope){
        //获得全部卡种
        this.getCardKinds=function(type,callback){
            $http({
                url:'card/cardkinds',
                method:'POST',
                data:{
                    shop:$rootScope.user.purview,
                    type:type.name
                }
            }).then(function(data){
                callback(data.data);
            });
        }
        //加载某一个卡片种类的关联产品
        this.loadCardkindProducts=function(cardkindno,callback){
            $http({
                url:'card/loadCardkindProducts',
                method:'POST',
                data:{
                    cardkindno:cardkindno
                }
            }).then(function(data){
                callback(data.data);
            });
        }
        //添加卡种
        this.addCardKind=function(form,callback){
            // //处理截止时间
            // if(form.cardkindduetime && form.cardkindduetime.indexOf("T")>0){
            //     form.cardkindduetime=form.cardkindduetime.split("T")[0];
            // }
            //处理没有特别设置的产品
            for(var i=0;form.products&&i<form.products.length;i++){
                if(!form.products[i].servediscount){
                    form.products[i].servediscount=form.servediscount;
                }
                if(!form.products[i].servetimes){
                    form.products[i].servetimes=form.servetimes;
                }
                if(!form.products[i].servecycle){
                    form.products[i].servecycle=form.servecycle;
                }
            }
            $http({
                url:'card/addkind',
                method:'POST',
                data:form
            }).then(function(data){
                callback(data.data);
            });
        }

        this.updateCardkindStatus=function(cardkind,callback){
            $http({
                url:'card/updateCardkindStatus',
                method:'POST',
                data:cardkind
            }).then(function(data){
                callback(data.data);
            });
        }
        //更新卡种
        this.updateCardKind=function(cardkind,callback){
            console.log("update cardkind>>",cardkind);
            $http({
                url:'card/updatekind',
                method:'POST',
                data:cardkind
            }).then(function(data){
                callback(data.data);
            });
        }
        //生成卡片，一张或批量生成卡片
        this.newCardSeq=function(cardkind,callback){
            $http({
                url:'card/newcardseq',
                method:'POST',
                data:cardkind
            }).then(function(data){
                callback(data.data);
            });
        }
        //获取某卡种下的卡片
        this.getCards=function(cardkindno,pageindex,pagesize,callback){
            $http({
                url:'card/cards',
                method:'POST',
                data:{
                    cardkindno:cardkindno,
                    pageindex:pageindex,
                    pagesize:pagesize
                }
            }).then(function(data){
                console.log("getcards>>>>",data);
                callback(data.data);
            });
        }
        //删除某张卡片或批量删除卡片
        this.delCardkind=function(cardkind,callback){
            $http({
                url:'card/delcardkind',
                method:'POST',
                data:{cardkindno:cardkind.cardkindno}
            }).then(function(data){
                callback(data.data);
            });
        }

        this.newCard=function(data,callback){
            $http({
                url:'card/newcard',
                method:'POST',
                data:data
            }).then(function(data){
                callback(data.data);
            });
        }

        this.genCards=function(data,callback){
            $http({
                url:'card/gencards',
                method:'POST',
                data:data
            }).then(function(data){
                callback(data.data);
            });
        }

        //搜索卡片
        this.searchCard=function(){

        }
        
        //卡片删除
        this.deletecard = function(cardid,callback) {
        	$http({
        		url:'card/delete/'+cardid,
        		method:'DELETE'
        	}).then(function(response) {
        		callback(response.data,cardid);
        	});
        }
        
        //卡片状态更改
        this.updatecardstatus = function(flag,cardid,callback) {
        	if(flag == "" || flag == null) {
        		return;
        	} else {
        		$http({
            		url:'card/updatecardstatus/'+flag+"/"+cardid,
            		method:'GET'
            	}).then(function(response) {
            		callback(response.data,flag,cardid);
            	});
        	}
        	
        }
        
    }]);
})();
