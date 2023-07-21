const projectSelector = $('#project-selector');
const sprintSelector = $('#sprint-selector');
const sprintHeaderRow = $('#sprint-header-row');
const sprintInfoRow = $('#sprint-info-row');
const taskSummariesRow = $('#task-summaries-row');
const taskStatusRow = $('#task-status-row');
const taskTotalRow = $('#task-total-row');
let sprintList = [];
const oneDayMillis = 1000 * 60 * 60 * 24;

$(window).on('load', () => init());

function init() {
    setupSelectors();
    $(document).ajaxError((event, jqXHR) => {
        failNoty(jqXHR);
    });
    let projectId = sessionStorage['projectId'];
    if (projectId) {
        projectSelector.val(projectId);
        sessionStorage.removeItem('projectId');
    }
    if (projectSelector.val() !== '-1') {
        getFinishedSprints(projectSelector.val());
    }
}

function setupSelectors() {
    projectSelector.on('change', () => {
        let projectId = projectSelector.val();
        getFinishedSprints(projectId);
    });

    if (sprintSelector.length > 0) {
        sprintSelector.on('change', () => {
            makeReport(sprintList[sprintSelector.prop('selectedIndex')]);
        });
    }
}

function getFinishedSprints(projectId) {
    clearSprintSelector();
    $.ajax({
        url: '/api/sprints/by-project-and-status',
        data: {projectId: projectId, statusCode: 'finished'}
    }).done(sprints => {
        if (sprints.length === 0) {
            disableSprintSelector();
            hideReportRows();
        } else {
            fillSprintSelector(sprints);
            makeReport(sprintList[sprintSelector.prop('selectedIndex')]);
        }
    });
}

function clearSprintSelector() {
    sprintSelector.attr('disabled', false);
    sprintSelector.empty();
    sprintList = [];
}

function disableSprintSelector() {
    sprintSelector.attr('disabled', true);
    sprintSelector.append($('<option></option>').html('No finished sprints'));
}

function hideReportRows() {
    sprintHeaderRow.attr('hidden', true);
    sprintInfoRow.attr('hidden', true);
    taskSummariesRow.attr('hidden', true);
}

function fillSprintSelector(sprints) {
    sprints.forEach(sprint => {
        sprintSelector.append($('<option></option>').val(sprint.id).html(sprint.code));
        sprintList.push(sprint);
    });
    let sprintId = sessionStorage['sprintId'];
    if (sprintId) {
        sprintSelector.val(sprintId);
        sessionStorage.removeItem('sprintId');
    }
}

function makeReport(sprint) {
    showReportRows();
    clearTasksSummary();
    let sprintStart = new Date(sprint.startpoint);
    let sprintEnd = new Date(sprint.endpoint);
    sprintInfoRow.find('td').eq(0).html(`${sprint.statusCode.charAt(0).toUpperCase()}${sprint.statusCode.slice(1)}`);
    sprintInfoRow.find('td').eq(1).html(sprintStart.toLocaleString());
    sprintInfoRow.find('td').eq(2).html(sprintEnd.toLocaleString());
    sprintInfoRow.find('td').eq(3).html(`${daysBetween(sprintStart, sprintEnd)} days`);
    getTaskSummaries(sprint.id);
}

function showReportRows() {
    sprintHeaderRow.attr('hidden', false);
    sprintInfoRow.attr('hidden', false);
    taskSummariesRow.attr('hidden', false);
}

function clearTasksSummary() {
    taskStatusRow.empty();
    taskTotalRow.empty();
}

function daysBetween(start, end) {
    let days = Math.round((end.getTime() - start.getTime()) / oneDayMillis);
    return days < 0 ? 0 : days;
}

function getTaskSummaries(sprintId) {
    $.ajax({
        url: `/api/reports/${sprintId}`
    }).done(taskSummaries => {
        taskSummaries.forEach(taskSummary => {
            taskStatusRow.append($('<th></th>').html(taskSummary.status));
            taskTotalRow.append($('<td></td>').html(taskSummary.total));
        })
    });
}
