// ------------ Crash ---------------
vm.crashes      = null;
vm.crash        = null;
vm.crashRefresh = function(){
    $http({
        method:"POST",
        url: "/crash/listCrash",
        data:  {

        },
        transformRequest: transformRequest,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },

    }).then(vm.onCrashList);
}

vm.onCrashList = function(response) {
    vm.crashes = response.data;
}

vm.crashShow = function(crash) {
    vm.crash = crash;
    vm.crash.values.details = vm.crash.values.details.replace(/\n/g, '<br>');
    $http({
        method:"POST",
        url: "/crash/show/" + crash.values.id,
        data:  {

        },
        transformRequest: transformRequest,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },

    }).then(vm.onSuccess);
}
