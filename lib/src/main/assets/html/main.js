var bbsApp = angular.module("pinelib", ["ngTouch"]);
bbsApp.directive("ngOnhold", ["$swipe", "$parse", function($swipe, $parse){
    //长按触发事件需要的时间
    var ON_HOLD_TIMEMS = 500;
    
    return function(scope, element, attr) {
        
        var onholdHandler = $parse(attr["ngOnhold"]);
        var run;
        
        $swipe.bind(element, {
            'start': function(coords, event) {
                run = setTimeout(function(){
                    scope.$apply(function() {
                        element.triggerHandler("onhold");
                        onholdHandler(scope, {$event: event});
                    });
                }, ON_HOLD_TIMEMS);
            },
            'cancel': function(event) {
                if(run)clearTimeout(run);
            },
            'move' : function(event){
                if(run)clearTimeout(run);
            },
            'end': function(coords, event) {
                if(run)clearTimeout(run);
            }
        }, ['touch', 'mouse']);
    }
}]);


function transformRequest(obj) {  
	var str = [];  
	for (var s in obj) {  
		str.push(encodeURIComponent(s) + "=" + encodeURIComponent(obj[s]));  
	}  
	return str.join("&");  
} 


Number.prototype.formatMoney = function (places, symbol, thousand, decimal) {
    places = !isNaN(places = Math.abs(places)) ? places : 2;
    symbol = symbol !== undefined ? symbol : "$";
    thousand = thousand || ",";
    decimal = decimal || ".";
    var number = this,
        negative = number < 0 ? "-" : "",
        i = parseInt(number = Math.abs(+number || 0).toFixed(places), 10) + "",
        j = (j = i.length) > 3 ? j % 3 : 0;
    return symbol + negative + (j ? i.substr(0, j) + thousand : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + thousand) + (places ? decimal + Math.abs(number - i).toFixed(places).slice(2) : "");
};



String.prototype.untilNow = function () {  
    var date = new Date(this);//就是这么简单
    var today = new Date();//获取当前日期时间
    var number = (today.getTime() - date.getTime()) / 1000;

    if (number < 60){
        return number + " 秒前";  
    }
    number /= 60;
    number = Math.round(number);

    if (number < 60){
        return number + " 分前";  
    }
    number /= 60;
    number = Math.round(number);

    if (number < 24){
        return number + " 时前";  
    }
    number /= 24;
    number = Math.round(number);

    if (number < 365){
        return number + " 天前";  
    }
    number /= 365;
    number = Math.round(number);

    return number + " 年前";  
   
};  






String.prototype.monthDayOnly = function () {  
    var date = new Date(this);
    var m = date.getMonth() + 1;  
    m = m < 10 ? ('0' + m) : m;  
    var d = date.getDate();  
    d = d < 10 ? ('0' + d) : d;  
    
    return m + "-" + d;  
};  

Date.prototype.formatDate = function () {  

    var y = this.getFullYear();  
    var m = this.getMonth() + 1;  
    m = m < 10 ? ('0' + m) : m;  
    var d = this.getDate();  
    d = d < 10 ? ('0' + d) : d;  
    var h = this.getHours();  
    h = h < 10 ? ('0' + h) : h;  
    var minute = this.getMinutes();  
    minute = minute < 10 ? ('0' + minute) : minute; 
    var second= this.getSeconds();  
    second = second < 10 ? ('0' + second) : second;  

    return y + '-' + m + '-' + d+' '+h+':'+minute+':'+ second;  
};  

Date.prototype.formatDateNoSec = function () {  

    var y = this.getFullYear();  
    var m = this.getMonth() + 1;  
    m = m < 10 ? ('0' + m) : m;  
    var d = this.getDate();  
    d = d < 10 ? ('0' + d) : d;  
    var h = this.getHours();  
    h = h < 10 ? ('0' + h) : h;  
    var minute = this.getMinutes();  
    minute = minute < 10 ? ('0' + minute) : minute; 
    var second= this.getSeconds();  
    second = second < 10 ? ('0' + second) : second;  

    return y + '-' + m + '-' + d+' '+h+':'+minute;  
};  

Date.prototype.formatDateOnly = function () {  
    var y = this.getFullYear();  
    var m = this.getMonth() + 1;  
    var d = this.getDate();  

    
    return d + '/' + m + '/' + y;  
};  

Date.prototype.formatDateOnly1 = function () {  
    var y = this.getFullYear();  
    var m = this.getMonth() + 1;  
    var d = this.getDate();  
    m = m < 10 ? ('0' + m) : m;  
    d = d < 10 ? ('0' + d) : d;   
    return y + '-' + m + '-' + d;  
};  



function copyToClip(content) {
    var aux = document.createElement("textarea"); 
    aux.value = content; 
    //aux.html(content);
    document.body.appendChild(aux); 
    aux.select();
    document.execCommand("copy"); 
    document.body.removeChild(aux);
    return true;
}




function toast(title, body = ""){
	$(document).Toasts('create', {
	  title: title,
	  body: body
	})
}



bbsApp.controller('PageController', PageController);


function PageController($http, $rootScope, $sce, $timeout) {
	vm 			    = this;
	
	// ------------ Public ------------
    vm.nornalFunctionCall = function(url){
		$http({
            method:"POST",
            url: "/" + url,
            data:  {
              
            },
            transformRequest: transformRequest,
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },

        }).then(vm.onSuccess);
    }

    vm.nornalFunctionCallWithConfirm = function(url){
		if (confirm("Do you want " + url)) {
			vm.nornalFunctionCall(url)
		}
    }


    vm.onSuccess = function(response) {
		toastr.success(response.data);
    }





};

