function deleteAtt(attId) {
    $.ajax({
        url: '/api/attachments/' + attId,
        type: 'DELETE',
        success: function () {
            successNoty("File deleted");
            $('#table-body').empty();
            getAttachments();
        },
        error: function (response) {
            failNoty(response);
        }
    });
}

function getAttachments() {
    $.ajax({
        url: `/api/attachments/for-object`,
        data: `objectId=${objectId}&type=${objectType}`
    }).done(atts => {
        atts.forEach(att => {
            let row = `<tr><th><a href="/api/attachments/download/${att.id}" download>${att.name}</a> (<a href="/api/attachments/download/${att.id}" target="_blank">open</a>)</th>
                <th><a href="#" id="delete-att" onclick="deleteAtt(${att.id})"><span class="fa fa-remove"></span></a></th></tr>`;
            $('#table-body').append(row);
        });
    });
}

$('#upload-btn').on('click', (e) => {
    e.preventDefault();
    let data = new FormData();
    let filesField = $('#att-upload');
    let file = filesField.prop('files')[0];

    data.append('type', objectType);
    data.append('objectId', objectId);
    data.append('file', file);

    $.ajax({
        url: `/api/attachments`,
        type: 'POST',
        data: data,
        contentType: false,
        cache: false,
        processData: false,
        success: function (response) {
            successNoty(`File: ${response.name}  uploaded`)
            $('#table-body').empty();
            getAttachments();
        },
        error: function (response) {
            failNoty(response);
            console.error(response);
        }
    });
    filesField.val('');
    $('#upload-btn').attr('disabled', 'true')
});

$('#att-upload').on('input', (e) => {
    let button = $('#upload-btn');
    if ($('#att-upload').val() !== undefined) {
        button.removeAttr('disabled');
    } else {
        button.attr('disabled', 'true')
    }
});