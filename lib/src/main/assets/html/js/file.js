// ------------ Crash ---------------
vm.filesTree      = null;
vm.fileOpenedFile = null;
vm.fileOpenedContent = null;

vm.fileRefreshBaseTree = function(){
    $http({
        method:"POST",
        url: "/file/refreshBaseTree",
        data:  {

        },
        transformRequest: transformRequest,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },

    }).then(vm.onTree);

}

vm.onTree = function(response) {
    vm.filesTree = response.data;
}

vm.fileLoadDir = function(dir){
    $http({
        method:"POST",
        url: "/file/loadDir?dir=" + dir.fullDir,
        data:  {
        },
        transformRequest: transformRequest,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },

    }).then(vm.onTree);
}

vm.fileReadFile = function(file){
    $http({
        method:"POST",
        url: "/file/loadFile?file=" + file.fullDir,
        data:  {
        },
        transformRequest: transformRequest,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },

    }).then(vm.onFile);

}
vm.fileSave = function(){
    $http({
        method:"POST",
        url: "/file/saveFile?file=" + vm.fileOpenedFile,
        data:  {
            content: vm.fileOpenedContent
        },
        transformRequest: transformRequest,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },

    }).then(vm.onFile);

}

vm.onFile = function(response) {
    vm.fileOpenedFile = response.data.name;
    vm.fileOpenedContent = response.data.content;

}