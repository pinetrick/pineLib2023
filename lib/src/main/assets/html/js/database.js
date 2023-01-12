// ------------ Database ------------
vm.databases            = null;
vm.choosedDbName        = null;
vm.tableData            = null;
vm.dbEditing            = null;
vm.dbNewRecord          = [];
vm.dbAllRecordChosen    = false;

vm.dbRefreshAll = function(){
    $http({
        method:"POST",
        url: "/db/listDb",
        data:  {

        },
        transformRequest: transformRequest,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },

    }).then(vm.onDbList).catch(vm.onFail);
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

    }).then(vm.onTableData).catch(vm.onFail);
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

    }).then(vm.onTableData).catch(vm.onFail);
}

vm.deleteSelected = function(){
    if (confirm("Do you want delete those data?")) {
        pkList = [];

        for (const id in vm.tableData.records) {
            line = vm.tableData.records[id];

            if (line.isChosen) {
                pkList.push(line.values[line["pk"]]);
            }
        }



        $http({
            method:"POST",
            url: "/db/delete/" +  vm.tableData.dbName + "/" + vm.tableData.tableName,
            data: {pk: pkList},
            transformRequest: transformRequest,
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },

        }).then(vm.dbOnRefreshBtnClick).catch(vm.onFail);
    }
}

vm.dbSaveNewRecord = function(){
    $http({
        method:"POST",
        url: "/db/newRecord/" +  vm.tableData.dbName + "/" + vm.tableData.tableName,
        data: vm.dbNewRecord,
        transformRequest: transformRequest,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },

    }).then(vm.dbOnRefreshBtnClick).catch(vm.onFail);
}

vm.dbOnRefreshBtnClick = function(){
    vm.dbNewRecord = [];
    vm.dbAllRecordChosen = false;
    vm.dbRawQuery(vm.choosedDbName ,vm.tableData.sql);
}

vm.dbOnDataChange = function(dbName, table, key, value, record){

    //value = value.replace(/'/g, "''");
    //sql = "UPDATE [" + table + "] SET [" + key + "]='" + value + "' WHERE [" + record.pk + "]='" + record.values[record.pk] + "'";

    vm.dbRawUpdate(dbName, table, key, value, record.values[record.pk]);

    //vm.dbRawExec(dbName, sql);

    vm.dbEditing = null;
}
vm.dbChooseAll = function(isChosen) {
     for (const id in vm.tableData.records) {
        vm.tableData.records[id].isChosen = isChosen;
     }
}

vm.dbRawUpdate = function(dbName, tableName, key, newValue, pkValue) {
    $http({
        method:"POST",
        url: "/db/update/" + dbName,
        data:  {
            tableName: tableName,
            key: key,
            newValue: newValue,
            pkValue: pkValue
        },
        transformRequest: transformRequest,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },

    }).then(vm.dbOnRawExec).catch(vm.onFail);
}

vm.dbRawExec = function(dbName, sql){
    $http({
        method:"POST",
        url: "/db/exec/" + dbName + "/" + encodeURIComponent(sql) + "",
        data:  {

        },
        transformRequest: transformRequest,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },

    }).then(vm.dbOnRawExec).catch(vm.onFail);
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

    }).then(vm.dbOnRawQuery).catch(vm.onFail);
}

vm.dbOnRawQuery = function(response) {
    toastr.success("Request Sent");
    vm.onTableData(response);
}
