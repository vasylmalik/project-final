const projectSelector = $('#project-selector');
const sprintTitleField = $('#sprint-title-field');
const sprintDaysLeftField = $('#sprint-days-left-field');
let sprintId;
let assignments;
const todoTaskColumn = $('#todo-task-column');
const inProgressTaskColumn = $('#in-progress-task-column');
const reviewTaskColumn = $('#review-task-column');
const testTaskColumn = $('#test-task-column');
const doneTaskColumn = $('#done-task-column');
const tasksByColumns = new Map();
const oneDayMillis = 1000 * 60 * 60 * 24;

$(window).on('load', () => init());

function init() {
    setupProjectSelector();
    setupTasksByColumns();
    setupContextMenu('task', ((taskItemInContext) => {
        prepareContextMenu(taskItemInContext)
    }));
    $(document).ajaxError((event, jqXHR) => {
        failNoty(jqXHR);
    });
    let projectId = sessionStorage['projectId'];
    if (projectId) {
        projectSelector.val(projectId);
        sessionStorage.removeItem('projectId');
    }
    if (projectSelector.val() !== '-1') {
        getActiveSprint(projectSelector.val());
    }
}

function setupProjectSelector() {
    projectSelector.on('change', () => {
        let projectId = projectSelector.val();
        clearTaskColumns();
        getActiveSprint(projectId);
    });
}

function setupTasksByColumns() {
    tasksByColumns.set(todoTaskColumn, []);
    tasksByColumns.set(inProgressTaskColumn, []);
    tasksByColumns.set(reviewTaskColumn, []);
    tasksByColumns.set(testTaskColumn, []);
    tasksByColumns.set(doneTaskColumn, []);
}

function getActiveSprint(projectId) {
    $.ajax({
        url: '/api/sprints/by-project-and-status',
        data: {projectId: projectId, statusCode: 'active'}
    }).done(sprints => {
        if (sprints.length === 0) {
            sprintTitleField.attr('hidden', false);
            sprintTitleField.html('No active sprint');
            sprintDaysLeftField.attr('hidden', true);
        } else {
            let sprint = sprints[0];
            sprintTitleField.attr('hidden', false);
            sprintTitleField.html(sprint.code);
            sprintDaysLeftField.attr('hidden', false);
            sprintDaysLeftField.html(`Days left: ${sprint.endpoint != null ? daysUntilDate(sprint.endpoint) : 'sprint endpoint not set'}`);
            sprintId = sprint.id;
            getTasks(sprintId);
        }
    });
}

function getTasks(sprintId) {
    $.ajax({
        url: `/api/tasks/by-sprint`,
        data: `sprintId=${sprintId}`
    }).done(tasks => {
        $.ajax({
            url: `/api/tasks/assignments/by-sprint`,
            data: `sprintId=${sprintId}`
        }).done(taskAssignments => {
            assignments = taskAssignments;
            fillTaskColumns(tasks);
        });
    });
}

function fillTaskColumns(tasks) {
    tasks.forEach(task => {
        let taskColumn = getTaskColumn(task.statusCode);
        if (taskColumn != null) {
            let indented = tasksByColumns.get(taskColumn).includes(task.parentId);
            taskColumn.append(generateTaskCard(task, indented));
            tasksByColumns.get(taskColumn).push(task.id);
        }
    });
}

function getTaskColumn(statusCode) {
    if (statusCode === 'todo') {
        return todoTaskColumn;
    } else if (statusCode === 'in_progress' || statusCode === 'ready_for_review') {
        return inProgressTaskColumn;
    } else if (statusCode === 'review' || statusCode === 'ready_for_test') {
        return reviewTaskColumn;
    } else if (statusCode === 'test') {
        return testTaskColumn;
    } else if (statusCode === 'done') {
        return doneTaskColumn;
    }
    return null;
}

function generateTaskCard(task, indented) {
    let card = $('<div></div>').addClass('card task mt-1').css('cursor', 'pointer');
    if (indented) {
        card.addClass('ms-2');
    }
    let cardBody = $('<div></div>').addClass('card-body p-2 bg-light text-start').html(`<div class="small card-title text-truncate m-0">${task.title}</div>`);
    let infoRow = $('<div></div>').addClass('row').html(`<div class="col-7 small text-primary text-truncate"></div><div class="col-5 small text-end">${task.code}</div>`);
    cardBody.append(infoRow);
    let possibleUserType = (task.statusCode === 'ready_for_review') ? 'task_developer' : (task.statusCode === 'ready_for_test') ? 'task_reviewer' : taskStatusRefs[task.statusCode].aux.split('|')[1];
    let hasAssignment = this.hasAssignment(task.id, possibleUserType);
    if (hasAssignment) {
        cardBody.removeClass('bg-light').addClass('user-belong');
    }
    if (task.statusCode === 'ready_for_review' || task.statusCode === 'ready_for_test') {
        if (!hasAssignment) {
            cardBody.removeClass('bg-light').addClass('ready-for');
        }
        infoRow.find('.col-7').html(`<i class="fa-solid fa-check"></i> Ready for ${task.statusCode.substring(task.statusCode.lastIndexOf('_') + 1)}`);
    }
    card.append(cardBody);
    card.data('id', task.id).data('status-code', task.statusCode);
    card.on('dblclick', () => showTaskInfo(task));
    return card;
}

function hasAssignment(taskId, userType) {
    if (userType.length) {
        for (let assignment of assignments) {
            if (assignment.objectId === taskId && assignment.userTypeCode === userType) {
                return true;
            }
        }
    }
    return false;
}

function showTaskInfo(task) {
    let taskInfo = $('#task-info');
    taskInfo.empty();
    taskInfo.load(`/ui/tasks/${task.id}?fragment=true`);
    $('#task-info-modal').modal('toggle');
}

function clearTaskColumns() {
    todoTaskColumn.empty();
    inProgressTaskColumn.empty();
    reviewTaskColumn.empty();
    testTaskColumn.empty();
    doneTaskColumn.empty();
    tasksByColumns.forEach((v, k, map) => map.set(k, []));
}

function daysUntilDate(date) {
    let daysLeft = Math.round((Date.parse(date) - Date.now()) / oneDayMillis);
    return daysLeft < 0 ? 0 : daysLeft;
}

// TASK CONTEXT MENU PART

function prepareContextMenu(taskItemInContext) {
    let taskId = taskItemInContext.data('id');
    let selectedTaskStatusRef = taskStatusRefs[taskItemInContext.data('status-code')];
    let splittedAux = selectedTaskStatusRef.aux.split('|');
    let possibleUserType = splittedAux[1];
    if (possibleUserType.length) {
        if (hasAssignment(taskId, possibleUserType)) {
            generateContextMenuItem('unassign', () => {
                unAssignTask(taskId, possibleUserType);
            });
        } else {
            generateContextMenuItem('assign to me', () => {
                assignTask(taskId, possibleUserType);
            });
        }
    }
    let possibleStatuses = splittedAux[0].split(',');
    possibleStatuses.forEach(possibleStatus => {
        generateContextMenuItem(`to "${taskStatusRefs[possibleStatus].title}"`, () => {
            changeTaskStatus(taskId, possibleStatus);
        });
    });
}

function changeTaskStatus(taskId, statusCode) {
    $.ajax({
        url: `/api/tasks/${taskId}/change-status`,
        type: 'PATCH',
        data: `statusCode=${statusCode}`
    }).done(() => {
        clearTaskColumns();
        getTasks(sprintId);
    });
}

function assignTask(taskId, userType) {
    $.ajax({
        url: `/api/tasks/${taskId}/assign`,
        type: 'PATCH',
        data: `userType=${userType}`
    }).done(() => {
        clearTaskColumns();
        getTasks(sprintId);
    });
}

function unAssignTask(taskId, userType) {
    $.ajax({
        url: `/api/tasks/${taskId}/unassign`,
        type: 'PATCH',
        data: `userType=${userType}`
    }).done(() => {
        clearTaskColumns();
        getTasks(sprintId);
    });
}
