// ------------ Database ------------
vm.databases    = null;
vm.choosedDbName = null;
vm.tableData    = null;
vm.dbEditing    = null;


vm.dbRefreshAll = function(){
    $http({
        method:"POST",
        url: "/db/listDb",
        data:  {

        },
        transformRequest: transformRequest,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },

    }).then(vm.onDbList);
}

vm.onDbList = function(response) {
    vm.databases = response.data;
}

vm.dbLoadTableData = function(dbName, tableName){
    vm.choosedDbName = dbName;
    $http({
        method:"POST",
        url: "/db/select/" + dbName + "/" + tableName,
        data:  {

        },
        transformRequest: transformRequest,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },

    }).then(vm.onTableData);
}

vm.onTableData = function(response) {
    vm.tableData = response.data;
}

vm.dbTabelStructure = function(dbName, tableName){
    vm.choosedDbName = dbName;
    $http({
        method:"POST",
        url: "/db/structure/" + dbName + "/" + tableName,
        data:  {

        },
        transformRequest: transformRequest,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },

    }).then(vm.onTableData);
}


vm.dbOnDataChange = function(dbName, table, key, value, record){

    value = value.replace(/'/g, "''");
    sql = "UPDATE [" + table + "] SET [" + key + "]='" + value + "' WHERE [" + record.pk + "]='" + record.values[record.pk] + "'";


    vm.dbRawExec(dbName, sql);

    vm.dbEditing = null;
}

vm.dbRawExec = function(dbName, sql){
    $http({
        method:"POST",
        url: "/db/exec/" + dbName + "/" + encodeURIComponent(sql) + "",
        data:  {

        },
        transformRequest: transformRequest,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },

    }).then(vm.dbOnRawExec);
}

vm.dbOnRawExec = function(response) {
    toastr.success(response.data);
}

vm.dbRawQuery = function(dbName, sql){
    $http({
        method:"POST",
        url: "/db/query/" + dbName + "/" + encodeURIComponent(sql) + "",
        data:  {

        },
        transformRequest: transformRequest,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },

    }).then(vm.dbOnRawQuery);
}

vm.dbOnRawQuery = function(response) {
    toastr.success("Request Sent");
    vm.onTableData(response);
}
