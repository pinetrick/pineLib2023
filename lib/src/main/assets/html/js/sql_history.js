// ------------ SQL History ---------------
vm.sqlLists     = null;


vm.sqlRefresh = function(){
    $http({
        method:"POST",
        url: "/sql/show",
        data:  {

        },
        transformRequest: transformRequest,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },

    }).then(vm.sqlOnRefresh).catch(vm.onFail);

}

vm.sqlOnRefresh = function(response){
    vm.sqlLists = response.data;
}

vm.sqlRun = function(sql) {
    vm.page = "db";
    vm.dbRawQuery(sql.db, sql.sql);
}