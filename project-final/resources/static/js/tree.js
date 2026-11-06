$(function () {
    setupContextMenu('fancytree-node',
        ((node) => {
            prepareContextMenu(node)
        }), () => {
            unsetNodeAndUnselect();
        });
    $(document).ajaxError((event, jqXHR) => {
        failNoty(jqXHR);
    });
    $("#treeElement").fancytree({
        extensions: ['glyph'],
        glyph: {
            preset: "awesome5",
            map: {}
        },
        source: {
            url: "/api/tree/projects",
            cache: false,
        },
        loadChildren: (event, data) => addSubNodesToChildren(event, data),
        lazyLoad: (event, data) => loadChildrenNodes(event, data),
        activate: (event, data) => loadEntityInfo(event, data),
        renderNode: (event, data) => addClassByNodeType(event, data),
        icon: (event, data) => addIcon(data)
    }).addClass('tree-container');
});

function loadChildrenNodes(event, data) {
    let ajaxUrl;
    switch (data.node.data.nodeType) {
        case 'PROJECT': {
            ajaxUrl = '/api/tree/projects/' + data.node.data.id + '/sprints';
            break;
        }
        case 'SPRINT': {
            if (data.node.data.id === 0) { //is backlog
                ajaxUrl = '/api/tree/projects/' + data.node.parent.data.id + '/backlog/tasks';
            } else {
                ajaxUrl = '/api/tree/sprints/' + data.node.data.id + '/tasks';
            }
            break;
        }
    }
    data.result = {
        url: ajaxUrl,
        data: {mode: "children", parent: data.node.key},
        cache: false
    };
}

function addSubNodesToChildren(event, data) {
    if (data.node.data.nodeType === undefined) {
        return;
    }
    const subNodes = data.node.data.subNodes;
    if (subNodes.length > 0) {
        for (let children of subNodes) {
            data.node.addChildren({
                key: children.key,
                lazy: children.lazy,
                children: null,
                title: children.title,
                subNodes: children.subNodes,
                nodeType: children.nodeType,
                id: children.id
            }, data.node.children.length > 0 ? data.node.children[0] : null)
        }
    }
}

function loadEntityInfo(event, data) {
    const nodeData = data.node.data;
    const id = nodeData.id;
    const entityInfo = $('#entityInfo');

    switch (nodeData.nodeType) {
        case "SPRINT": {
            if (nodeData.id === 0) {
                document.getElementById('infoCard').hidden = true;
            } else {
                entityInfo.load(`/ui/sprints/${id}?fragment=true`, showEntityInfoElement);
            }
            break
        }
        case "TASK": {
            entityInfo.load(`/ui/tasks/${id}?fragment=true`, showEntityInfoElement);
            break
        }
        case "PROJECT": {
            entityInfo.load(`/ui/projects/${id}?fragment=true`, showEntityInfoElement);
            break
        }
    }
}

function showEntityInfoElement() {
    document.getElementById('infoCard').hidden = false;
}

function addClassByNodeType(event, data) {
    const node = data.node;
    let extraClass;
    switch (node.data.nodeType) {
        case 'PROJECT': {
            extraClass = 'node-project';
            break;
        }
        case 'SPRINT': {
            extraClass = 'node-sprint';
            break;
        }
        case 'TASK': {
            extraClass = 'node-task';
            break;
        }
    }
    node.extraClasses = extraClass;
    node.renderTitle();
}

function addIcon(data) {
    const node = data.node;
    switch (node.data.nodeType) {
        case 'PROJECT': {
            return 'fa-solid fa-diagram-project';
        }
        case 'SPRINT': {
            if (node.data.id === 0) {
                return 'fa-solid fa-list';
            } else {
                return 'fa-solid fa-share';
            }
        }
        case 'TASK': {
            return 'fa-solid fa-bars-progress';
        }
    }
}

//make info card stick to header (or navbar if window is small enough) instead of viewport
function setInfoCardTopStickyPosition() {
    const headerHeight = document.getElementById('header').clientHeight;
    const navbarHeight = document.getElementById('navbar').clientHeight;
    let targetTop;
    //on small window sometimes hidden header have non-zero height, filter it by checking navbar height
    if (headerHeight === 0 || navbarHeight < 200) {
        targetTop = navbarHeight;
    } else {
        targetTop = headerHeight;
    }
    document.getElementById('infoCard').style.top = targetTop + 'px';
}

document.addEventListener('DOMContentLoaded', function () {
    setInfoCardTopStickyPosition();
});

window.addEventListener('resize', function () {
    setInfoCardTopStickyPosition();
});

//CONTEXT MENU

let selectedNode;

function prepareContextMenu(node) {
    unsetNodeAndUnselect();
    setNodeAndSelect($.ui.fancytree.getNode(node));
    switch (selectedNode.data.nodeType) {
        case 'PROJECT': {
            prepareProjectContextMenu()
            break;
        }
        case 'SPRINT': {
            prepareSprintContextMenu()
            break;
        }
        case 'TASK': {
            prepareTaskContextMenu();
            break;
        }
    }
}

function setNodeAndSelect(node) {
    selectedNode = node;
    selectedNode.setSelected(true);
    selectedNode.setFocus(true);
}

function unsetNodeAndUnselect() {
    if (selectedNode !== undefined) {
        selectedNode.setSelected(false);
        selectedNode.setFocus(false);
        selectedNode = undefined;
    }
}

//TASK CONTEXT MENU

function prepareTaskContextMenu() {
    //subtask sprint change is not allowed
    if (isManager && selectedNode.parent.data.nodeType !== 'TASK') {
        prepareTaskMoveToItem(selectedNode, getProjectNodeByTask(selectedNode).children);
    }
    const parentId = selectedNode.data.id;
    generateContextMenuItem('create SubTask', () => {
        window.location.href = `/ui/tasks/new?parentId=${parentId}`;
    })
}

function moveTaskNodeToSprintNode(taskNode, sprintNode) {
    if (sprintNode.children === null) {
        taskNode.remove();
    } else {
        taskNode.moveTo(sprintNode);
    }
}

function prepareTaskMoveToItem(taskNode, sprintsAndBacklog) {
    const moveToSubItemsArray = [];
    for (let sprintNode of sprintsAndBacklog) {
        if (sprintNode.data.id === taskNode.parent.data.id || sprintNode.data.nodeType === 'PROJECT') {
            continue;
        }
        const taskId = taskNode.data.id;
        moveToSubItemsArray.push({
            text: sprintNode.data.code,
            onClickEvent: () => {
                //set id=null instead of 0 if target is backlog
                const id = sprintNode.data.id === 0 ? null : sprintNode.data.id;
                $.ajax({
                    type: 'PATCH',
                    url: `/api/tasks/${taskId}/change-sprint`,
                    data: {
                        sprintId: id
                    }
                }).done(() => {
                    moveTaskNodeToSprintNode(taskNode, sprintNode);
                });
            }
        })
    }
    generateContextMenuItemWithSubItems('move to', moveToSubItemsArray);
}

function getProjectNodeByTask() {
    let currentNode = selectedNode;
    while (currentNode.data.nodeType !== 'PROJECT') {
        currentNode = currentNode.parent;
    }
    return currentNode;
}

//SPRINT CONTEXT MENU

function prepareSprintContextMenu() {
    if (isManager) {
        generateContextMenuItem('assign as Manager', () => {
            //TODO: call API when implemented
        })
    }
    const sprintId = selectedNode.data.id;
    generateContextMenuItem('create Task', () => {
        //backlog
        if (sprintId === 0) {
            window.location.href = '/ui/tasks/new?projectId=' + selectedNode.parent.data.id;
        } else {
            window.location.href = `/ui/tasks/new?sprintId=${sprintId}`;
        }
    })
}

//PROJECT CONTEXT MENU

function prepareProjectContextMenu() {
    if (isManager) {
        generateContextMenuItem('assign as Manager', () => {
            //TODO: call API when implemented
        })
        const projectId = selectedNode.data.id;
        generateContextMenuItem('create Sprint', () => {
            window.location.href = `/ui/mngr/sprints/new?projectId=${projectId}`;
        })
        generateContextMenuItem('create SubProject', () => {
            window.location.href = `/ui/mngr/projects/new?parentId=${projectId}`;
        })
    }
}
