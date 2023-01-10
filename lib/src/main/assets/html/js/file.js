// ------------ File ---------------
vm.fileBaseTree     = null;
vm.fileUpLayers     = null;
vm.filesTree        = null;
vm.fileOpened       = [];
vm.fileOpenedId     = null;


vm.fileRefreshBaseTree = function(){
    $http({
        method:"POST",
        url: "/file/refreshBaseTree",
        data:  {

        },
        transformRequest: transformRequest,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },

    }).then(vm.onTree).catch(vm.onFail);

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

    }).then(vm.onTree).catch(vm.onFail);
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

        }).then(vm.onFileDeleted).catch(vm.onFail);
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

    }).then(vm.onFile).catch(vm.onFail);

}

vm.fileRename = function(file){
    file.renaming = false;
    $http({
        method:"POST",
        url: "/file/renameFile?file=" + file.fullDir + "&newName=" + file.name,
        data:  {

        },
        transformRequest: transformRequest,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },

    }).then(vm.onSuccess).catch(vm.onFail);
}


vm.fileSave = function(){
    $http({
        method:"POST",
        url: "/file/saveFile?file=" + vm.fileOpened[vm.fileOpenedId].name,
        data:  {
            content: vm.fileOpened[vm.fileOpenedId].content
        },
        transformRequest: transformRequest,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },

    }).then(vm.onSuccess).catch(vm.onFail);

}

vm.onFile = function(response) {

    obj = new Object();
    obj.name = response.data.name;
    obj.content = response.data.content;

    //检查是否可以重复利用标签
     for (const f in vm.fileOpened) {
        if (vm.fileOpened[f].name == obj.name) {
            vm.fileOpened[f] = obj;
            vm.fileOpenedId = f;
            return;
        }
    }

    if ((vm.fileOpenedId == null)) {
        vm.fileOpenedId = 0;
    } else {
        vm.fileOpenedId++;
    }
    vm.fileOpened[vm.fileOpenedId] = obj;
}

vm.fileClose = function(id) {
    vm.fileOpened.splice(id, 1);
    vm.fileOpenedId--;

}

vm.fileGetFileNameOrPath = function(file) {
    for (const f in vm.fileOpened) {
        if (vm.fileOpened[f].name == file.name) continue;
        if (vm.getFileName(vm.fileOpened[f].name) == vm.getFileName(file.name)) {
            return vm.fileOpened[f].name;
        }
    }
    return vm.getFileName(file.name);
}

vm.getFileName = function(nameWithPath) {
    return nameWithPath.split('/').pop();
}

vm.fileTagActiveClass = function(id) {
    if (vm.fileOpenedId == id) {
        return "active";
    }
    else {
        return "";
    }

}