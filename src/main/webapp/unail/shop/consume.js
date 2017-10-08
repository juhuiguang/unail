/**
 * Created by 橘 on 2016/7/9.
 */
(function() {
    'user strict';
    var consumeapp=angular.module('unail.consume',["unail.product","unail.card","unail.custom","unail.shop","ngMaterial","ui.router"]);
    consumeapp.config(["$stateProvider",function($stateProvider){
        $stateProvider.state('shop.deal', {
            url: '/shop/deal',
            title: '客户结算',
            templateUrl: 'unail/shop/consume.html',
            controller:"consumeController"
        });
    }]);
    consumeapp.controller("consumeController",["$scope","$mdDialog","$mdMedia","consumeservice","$rootScope","shopservice","$timeout","$filter","custominstance","$uibModal",
        function($scope,$mdDialog, $mdMedia,consumeservice,$rootScope,shopservice,$timeout,$filter,custominstance,$uibModal){
        var _this=$scope;
        $scope.products=[];
        $scope.producttypes=null;

        $scope.purviewSingle = "";
        $scope.purviewALL = "";
        $scope.currentshop = "";
        $scope.shops = [];   
		console.log("$rootScope",$rootScope); 
		
		shopservice.getshops($rootScope.user.purview,function(data){    
    		console.log(data);
    		if(data == "" || data == null) {  
    			
    		} else {
    			$scope.shops = data;
    			
    			if($rootScope.user.purview == "ALL") {
    				$scope.purviewSingle = false;
    				$scope.purviewALL = true;
                    $scope.currentshop = $scope.shops[0]
    			} else {
    				//console.log("aaaa");
    				$scope.purviewSingle = true;
    				$scope.purviewALL = false;
    				console.log($scope.shops);  
    				$scope.currentshopname = $scope.shops.shopName;
    				$scope.currentshop = $scope.shops;
    				console.log($scope.currentshop);
    			}
    		}
    	});

        //当前消费情况
        $scope.consume={
            consumeproducts:[],//记录消费的产品，每个产品需记录产品服务员工，产品消费方式：现金，卡抵用
            custom:null,//当前消费的客户
            customcards:[],//客户拥有的卡片，以及客户结账时输入的卡片
            totalprice:0,//消费总价格
            cashprice:0,//支付现金总价
            dealprice:0,
            extradiscount:0

        };
        $scope.showcustomer=function(customer){
            custominstance.customno = customer.customno;

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
        //应用卡片
        $scope.getcard=function(){
            consumeservice.findCard($scope.searchcard,function(result){
                if(result.result>0){
                    $scope.solocard=result.data;
                    var addflag=true;
                    for(var i=0;i<$scope.consume.customcards.length;i++){
                        if($scope.consume.customcards[i].cardno==$scope.solocard.cardno){
                            addflag=false;
                            break;
                        }
                    }
                    if(addflag){
                        $scope.consume.customcards.push(result.data);
                    }
                    if($scope.consume.consumeproducts.length>0){
                        for(var i=0;i<$scope.consume.consumeproducts.length;i++){
                            $scope.consume.consumeproducts[i].addcard($scope.solocard);
                        }
                    }

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

        $scope.getTotal=function(){
            $scope.consume.totalprice=0;//消费总价格
            $scope.consume.cashprice=0;//支付现金总价
            $scope.consume.dealprice=0;
            $scope.consume.extradiscount=0;
            for(var i=0;i<$scope.consume.consumeproducts.length;i++){
                $scope.consume.totalprice+=$scope.consume.consumeproducts[i].price*$scope.consume.consumeproducts[i].productcount;
                $scope.consume.cashprice+=$scope.consume.consumeproducts[i].cash;
                $scope.consume.dealprice+=$scope.consume.consumeproducts[i].dealprice;
                $scope.consume.extradiscount+=$scope.consume.consumeproducts[i].extradiscount;
            }
        };

        //提交结算
        $scope.submit=function(){
            $scope.$consuming=true;//避免重复提交
            $scope.consume.currentshop=$scope.currentshop;//将当前门店传递
            consumeservice.deal($scope.consume,function(result){
                if(result.result>0){
                    var data=result.data;
                    var success=data.success;
                    $mdDialog.show(
                        $mdDialog.alert()
                            .clickOutsideToClose(true)
                            .title('系统提示')
                            .textContent('结算已成功完成，感谢使用。')
                            .ok('确定')
                    );
                    //打印小票
                    printConsume($scope.consume);
                    for(var i=0;success!=null&&i<success.length;i++){
                        var successconsume={
                            product:{
                                productno:success[i].dealproduct
                            }
                        };
                        //成功的结算直接删除
                        $scope.removeConsumeproduct(successconsume);
                    }
                    resetconsume();
                }else{
                    var errors=data.errors;
                    var errtip="";
                    for(var i=0;errors!=null&&i<errors.length;i++){
                        errtip+=errors[i].error+"\n";
                    }
                    $mdDialog.show(
                        $mdDialog.alert()
                            .clickOutsideToClose(true)
                            .title('系统提示')
                            .textContent('很抱歉，结算过程中发生以下错误：\n'+errtip)
                            .ok('确定')
                    );
                }
                $scope.$consuming=false;
            },function(result){
                $scope.$consuming=false;
            });
        }
        $scope.chooseproduct=function($event){
            var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))  && $scope.customFullscreen;
            $mdDialog.show({
                parent: angular.element(document.body),
                controller: function(scope){
                    scope.hide = function() {
                        $mdDialog.hide();
                    };
                    scope.cancel = function() {
                        $mdDialog.cancel();
                    };
                    scope.answer = function(answer) {
                        $mdDialog.hide(answer);
                    };
                },
                targetEvent: $event,
                template: '<md-dialog flex="70" aria-label="产品目录">' +
                                '<md-dialog-content>' +
                                    '<product-list  selectproducts="products" rolepurview="'+$scope.currentshop.shopNo+'" mode="multiselect"></product-list>' +
                                '</md-dialog-content>' +
                                '<md-dialog-actions layout="row">' +
                                    '<button class="btn btn-success" ng-click="answer(products)">确定</button>' +
                                    '<button class="btn btn-danger" ng-click="cancel()">取消</button>'+
                                '</md-dialog-actions>'+
                            '</md-dialog>',
                clickOutsideToClose:true,
                fullscreen: useFullScreen
            })
            .then(function(products) {//选择完产品后，添加到现有产品列表
                for(var i=0;i<products.length;i++){
                    if(products[i].$isselected){
                        if(getConsumeproduct(products[i])!=null){
                            continue;
                        }else{
                            $scope.products.push(products[i]);//添加新的产品服务
                            var conproduct=new consumeproduct(products[i]);
                            //如果用户卡片那已经存在，读取用户卡片
                            if($scope.consume.customcards&&$scope.consume.customcards.length>0){
                                for(var i=0;i<$scope.consume.customcards.length;i++){
                                    conproduct.addcard($scope.consume.customcards[i]);
                                }
                            }
                            _this.consume.consumeproducts.push(conproduct);//记录到消费数组


                        }
                    }
                }

            }, function() {
                //$scope.status = 'You cancelled the dialog.';
            });
        }

        $scope.removeConsumeproduct=function(consumeproduct){
            for(var i=0;i<$scope.consume.consumeproducts.length;i++){
                if($scope.consume.consumeproducts[i].product.productno==consumeproduct.product.productno){
                    $scope.consume.consumeproducts.splice(i,1);
                    break;
                }
            }
        }

        function resetconsume(){
            $scope.solocard=null;
            for(var i=0;i<$scope.consume.consumeproducts.length;i++) {
                $scope.consume.consumeproducts[i].reset();
            }
        }
        $scope.$watch("currentshop",function(newvalue,oldvalue){
            $scope.consume.custom=null;
            $scope.consume.customcards=[];
            resetconsume();
        });
        //客户更替后重新加载卡片数据
        $scope.$watch("consume.custom",function(newvalue,oldvalue){
            $scope.consume.customcards=[];
            resetconsume();
            //加载用户卡片
            if(!newvalue){
                return;
            }
            consumeservice.loadUserCard(newvalue.customno,function(result){
                var data=result.data;
                console.log("loadusercard",data);
                $scope.consume.customcards=data;
                //如果已经有消费项目，绑定卡券
                for(var i=0;i<$scope.consume.consumeproducts.length;i++){
                    for(var j=0;j<$scope.consume.customcards.length;j++){
                        var card=$scope.consume.customcards[j];
                        $scope.consume.consumeproducts[i].addcard(card);
                    }
                }
            });
        },true);

        //监听消费项目发生变化
        $scope.$watch("consume.consumeproducts",function(){
            $scope.getTotal();//计算汇总值
        },true);

        $scope.getextradiscount=function(){
            for(var i=0;i<$scope.consume.consumeproducts.length;i++){
                var item=$scope.consume.consumeproducts[i];
                item.extradiscount=item.price*item.productcount-item.dealprice-item.cash;
            }
        }
        $scope.cancelextradiscount=function(){
            for(var i=0;i<$scope.consume.consumeproducts.length;i++){
                var item=$scope.consume.consumeproducts[i];
                item.extradiscount=0;
            }
        }

        var printConsume = function(printdata) {
            console.log(printdata);
            var printContents = "";
            printContents+="<p>"+$filter("date")(new Date(),"yyyy-MM-dd HH:mm:ss")+"</p>";
            printContents+="<p>客户 "+printdata.custom.customname+" 共消费"+printdata.consumeproducts.length+"个项目</p>";
            printContents+="<hr style='width:100%;' />";
            printContents+="<ul style='list-style:none;padding:0;margin:0;'>";
            for(var i=0;printdata.consumeproducts&&i<printdata.consumeproducts.length;i++){
                var citem=printdata.consumeproducts[i];
                printContents+="<li>";
                printContents+="<p>"+citem.product.productname+",价格："+citem.price+"元</p>";
                printContents+="<p>卡券共抵用："+citem.dealprice+"元,</p><p>现金或刷卡："+citem.price+"元，</p><p>额外折扣："+citem.extradiscount+"元</p>";

                if(citem.dealcards&&citem.dealcards.length>0){
                    printContents+="<p>卡券抵用明细：</p>";
                    for(var j=0;j<citem.dealcards.length;j++){
                        var cditem=citem.dealcards[j];
                        if(cditem.surplussales>0){
                            printContents+="<p>卡号："+cditem.card.cardno+"</p>";
                            printContents+="<p>卡片种类："+cditem.card.cardkind.cardkindname+"</p>";
                            printContents+="<p>结算方式："+cditem.card.cardkind.balancetype+"</p>";
                            printContents+="<p>结算折扣："+cditem.discount+"</p>";
                            printContents+="<p>结算额度："+cditem.surplussales+"</p>";
                        }

                    }
                }
                printContents+="<hr style='width:100%;' />";
                printContents+="</li>";
            }
            printContents+="</ul>";
            printContents+="<p>总消费：<strong>"+printdata.totalprice+" 元</strong>，</p><p>卡片抵用：<strong>"+printdata.dealprice+"元</strong>，</p><p>现金或刷卡：<strong>"+printdata.cashprice+"元</strong></p>";
            printContents+="<p>"+$filter("date")(new Date(),"yyyy-MM-dd HH:mm:ss")+"</p>";
            var popupWin = window.open('', '_blank', 'width=190,height=570');
            popupWin.document.open();
            popupWin.document.write('<html><head><title>结算打印</title></head><body onload="window.print()" style="font-size:90%;width:100%;height:auto;">' + printContents + '</body></html>');
            popupWin.document.close();
        }

            function getConsumeproduct(product){
            for(var i=0;i<$scope.consume.consumeproducts.length;i++){
                if($scope.consume.consumeproducts[i].product.productno==product.productno){
                    return $scope.consume.consumeproducts[i];
                }
            }
            return null;
        }

        function consumeproduct(product){
            var _this=this;
            this.product=product; //消费产品
            this.productcount=1;//产品数量
            this.dealcount=0;//已经模拟结算的次数
            var isclear=false;//产品是否结清
            this.price=product.productprice2; //产品现价
            this.dealprice=0;//已抵用金额
            this.extradiscount=0;//额外折扣，需申请得到
            var paytype=null; //产品
            this.cash=0;//现金支付金额
            this.staff=null;//服务员工
            this.cards=[];//可抵用的卡券（非充值卡）
            this.dealcards=[];//充值卡券
            this.usedcards=[];//被使用的卡券

            this.getextradiscount=function(number){
                this.extradiscount=number;
            }

            this.addcount=function(){
                _this.productcount++;
            }
            this.minuscount=function(){
                if(_this.productcount>1){
                    _this.productcount--;
                }
            }

            //验证一张卡片是否可用于本次结算
            this.validateCard=function(card){
                var cardkind=card.cardkind;
                //判断是否当前门店可用的卡片
                if(cardkind.cardkinduseshop.indexOf(",")>0){
                    if(cardkind.cardkinduseshop.indexOf($scope.currentshop.shopNo+",")<0&&cardkind.cardkinduseshop.indexOf(","+$scope.currentshop.shopNo)<0){
                        return false;
                    }
                }else{
                    if(cardkind.cardkinduseshop!=($scope.currentshop.shopNo+"")){
                        return false;
                    }
                }
                for(var i=0;i<cardkind.products.length;i++){
                    if(cardkind.products[i].productno==_this.product.productno){
                        card.discount=cardkind.products[i].servediscount;
                        if(cardkind.balancetype=='次数结算'){
                            if(card.surplustimes==0){//计次卡，次数不足返回不可用
                                return false;
                            }
                        }else if(cardkind.balancetype=='金额结算') {
                            if (cardkind.products[i].servediscount==1&&card.surplussales == 0) {
                                return false;//非折扣或现金抵用卡，且没有余额，卡片不可用
                            }
                        }
                        return true;
                    }
                }
                return false;
            }
            //增加一张可用的抵用卡
            this.addcard=function(card){
                if(!_this.validateCard(card)){
                    return;
                }
                if(card.cardkind.balancetype=='金额结算'&&card.surplussales>0){//现金卡
                    var flag=true;
                    for(var i=0;i<_this.dealcards.length;i++){
                        if(_this.dealcards[i].card.cardno==card.cardno){
                            flag=false;
                            break;
                        }
                    }
                    if(flag){
                        _this.dealcards.push(new cardDeal(card));
                    }
                }else{
                    var flag=true;
                    for(var i=0;i<_this.cards.length;i++){
                        if(_this.cards[i].card.cardno==card.cardno){
                            flag=false;
                            break;
                        }
                    }
                    if(flag){
                        _this.cards.push(new cardDeal(card));
                    }
                }

            }

            //重置结算
            this.reset=function(){
                _this.cards=[];
                _this.dealcards=[];
                _this.dealcount=0;
                _this.dealprice=0;
                _this.extradiscount=0;
                _this.cash=0;
                _this.usedcards=[];
            }

            this.getcarddiscount=function(card){
                //获得对应产品的折扣
                for(var i=0;i<card.cardkind.products.length;i++){
                    if(card.cardkind.products[i].productno==_this.product.productno){
                        if($scope.currentshop.shopName.indexOf("德基")>=0 && (card.cardkind.cardkindname.indexOf("3000")||card.cardkind.cardkindname.indexOf("5000")||card.cardkind.cardkindname.indexOf("10000"))){
                            var discount=card.cardkind.products[i].servediscount;
                            if(discount>0&&discount<0.9){
                                discount+=0.1;
                                if(discount>1)discount=1;
                                return discount;
                            }else{
                                return discount;
                            }
                        }else{
                            return card.cardkind.products[i].servediscount;
                        }
                        //return card.cardkind.products[i].servediscount;
                    }
                }
            }

            //创建卡片消费类型
            function cardDeal(card){
                var _deal=this;
                this.card=card;
                this.discount=_this.getcarddiscount(card);
                this.usediscount=true;
                this.surplussales=0;
                this.surplustimes=0;
                this.dealprice=0;
                this.undeal=function(){
                    if(_deal.surplussales>0){
                        card.surplussales+=_deal.surplussales;
                        _deal.surplussales=0;
                    }
                    if(_deal.surplustimes>0){
                        card.surplustimes+=_deal.surplustimes;
                        _this.dealcount-= _deal.surplustimes;
                        _deal.surplustimes=0;
                    }
                    if(_deal.dealprice>0){
                        _this.dealprice-=_deal.dealprice;
                        if(_this.dealprice<0){
                            _this.dealprice=0;
                        }
                        _deal.dealprice=0;
                    }
                }

                this.deal=function(){
                    if(card.cardkind.balancetype=='金额结算'&&card.surplussales>0){
                        var remainprice=_this.price*_this.productcount-_this.dealprice-_this.cash;
                        if(_deal.usediscount){//如果使用折扣
                            if(_deal.discount>0&&_deal.discount<=1) {//含有折扣,折扣为比例
                                remainprice = Math.round(remainprice * _deal.discount*100)/100;
                            }else{//折扣为现金
                                remainprice = Math.round(remainprice - _deal.discount*100)/100;
                            }
                        }else{//取消折扣
                            if(card.cardkind.cardkindrate>0) {
                                remainprice = Math.round(remainprice /card.cardkind.cardkindrate*100)/100;
                            }
                        }
                        if(remainprice>card.surplussales){//余额不足的情况
                            _deal.surplussales+=card.surplussales;
                        }else{
                            _deal.surplussales+=remainprice;
                        }
                        if(_deal.usediscount){//计算卡片抵用价值
                            if(_deal.discount>0&&_deal.discount<=1){//含有折扣,折扣为比例
                                _deal.dealprice+=Math.round(_deal.surplussales/_deal.discount*100)/100;
                            }else{//折扣为现金折扣
                                _deal.dealprice+=_deal.surplussales+_deal.discount;
                            }
                        }else{
                            if(card.cardkind.cardkindrate>0){
                                _deal.dealprice+=_deal.surplussales*card.cardkind.cardkindrate;
                            }else{
                                _deal.dealprice+=_deal.surplussales;
                            }

                        }
                        //前台模拟扣除卡内余额
                        card.surplussales-=_deal.surplussales;
                        _this.dealprice+=_deal.dealprice;//抵扣金额
                        if(_this.dealprice>_this.price*_this.productcount){//如果出现四舍五入超出总金额的情况
                            _this.dealprice=_this.price*_this.productcount;
                        }
                    }else{//抵用类卡券
                        if(card.cardkind.balancetype=='次数结算'){
                            if(card.surplustimes>0){
                                if(card.surplustimes>=(_this.productcount-_this.dealcount)){//卡内剩余次数大于消费产品次数
                                    _deal.surplustimes=(_this.productcount-_this.dealcount);
                                }else{
                                    _deal.surplustimes=card.surplustimes
                                }
                                _deal.dealprice=Math.round(_deal.surplustimes*_this.price*100)/100;//计算抵扣掉的价格
                                //模拟扣除卡片剩余次数
                                _this.dealcount+=_deal.surplustimes;
                                card.surplustimes-=_deal.surplustimes;
                                _this.dealprice+= _deal.dealprice;
                            }
                        }else{//如果不是次数结算，考虑打折、现金抵用问题
                            if(_deal.discount>0&&_deal.discount<=1){//是折扣
                                _deal.dealprice=Math.round(_this.price*(1-_deal.discount)*100)/100;
                            }else{
                                _deal.dealprice=_deal.discount;
                            }
                            _this.dealprice+=_deal.dealprice;
                            if(_this.dealprice>_this.price*_this.productcount){
                                _this.dealprice=_this.price*_this.productcount;
                            }
                        }
                    }
                }
            }

            this.isusedcard=function(card){
                for(var i=0;i<_this.usedcards.length;i++){
                    if(_this.usedcards[i].card){
                        if(_this.usedcards[i].card.cardid==card.cardid){
                            return true;
                        }
                    }
                }
            }

            //使用卡片
            this.useCard=function(carddeal){
                carddeal.deal();
                _this.usedcards.push(carddeal);
            }

            //取消使用卡片
            this.unuseCard=function(carddeal){
                carddeal.undeal();
                for(var i=0;i<_this.usedcards.length;i++){
                    if(_this.usedcards[i].card){
                        if(_this.usedcards[i].card.cardid==carddeal.card.cardid){
                            _this.usedcards.splice(i,1);
                        }
                    }
                }
            }


        }
    }]);

    consumeapp.service("consumeservice",["$http",function($http){
        //加载用户卡片
        this.loadUserCard=function(customno,callback){
            $http({
                url:"card/loadcustomcard",
                method:"POST",
                data:{customno:customno}
            }).then(function(result){
               callback(result.data);
            });
        };

        //使用卡片对产品进行结算
        this.deal=function(dealobject,callback){
            $http({
                url:"shop/consume",
                method:"POST",
                data:dealobject
            }).then(function(result){
                callback(result.data);
            });
        }

        this.findCard=function(cardno,callback){
            $http({
                url:"card/findcard",
                method:"POST",
                data:{cardno:cardno}
            }).then(function(result){
                callback(result.data);
            });
        }


    }]);
})();