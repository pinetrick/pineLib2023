// ------------ File ---------------
vm.fileBaseTree     = null;
vm.fileUpLayers     = null;
vm.filesTree        = null;
vm.fileOpenedFile   = null;
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
    if (vm.fileBaseTree == null) {
        vm.fileBaseTree = response.data;
    }
    else {
        vm.hookToBaseTree(response.data, vm.fileBaseTree);
    }
    vm.filesTree = response.data;
    vm.fileUpLayers = vm.generateUpLayers()
}

vm.generateUpLayers = function() {
    r = [];
    dir = vm.filesTree.fullDir.split('/');
    value = "";
    for (const k in dir) {
        if (k == 0) continue;
        key = dir[k];
        value += "/" + key;

        r.push({key: key, value: value});
    }
    return r;
}

vm.hookToBaseTree = function(data, baseTree) {
    for (const key in baseTree.subFiles) {
        if (baseTree.subFiles[key].fullDir == data.fullDir) {
            baseTree.subFiles[key] = data;
            return;
        }
        if (key.subFiles != null) {
            vm.hookToBaseTree(data, baseTree.subFiles[key]);
        }
    }
}

vm.fileLoadDir = function(dir){
    $http({
        method:"POST",
        url: "/file/loadDir?dir=" + dir,
        data:  {
        },
        transformRequest: transformRequest,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },

    }).then(vm.onTree);
}

vm.fileDelete = function(file){
    if (confirm("Remove this File / Dir ?")) {
        $http({
            method:"POST",
            url: "/file/delete?file=" + file.fullDir,
            data:  {
                content: vm.fileOpenedContent
            },
            transformRequest: transformRequest,
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },

        }).then(vm.onFileDeleted);
    }
}

vm.onFileDeleted = function(response) {
    vm.onSuccess(response);
    vm.fileLoadDir(vm.filesTree.fullDir);
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

    }).then(vm.onSuccess);

}



vm.onFile = function(response) {
    vm.fileOpenedFile = response.data.name;
    vm.fileOpenedContent = response.data.content;

}