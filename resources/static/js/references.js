let selectReftype = document.getElementById('select-reftype');
let modalRef = document.getElementById('modalRef');
let modalCaller = '';

getRefsByType(selectReftype.value);
selectReftype.addEventListener('change', () => {
    let refType = selectReftype.value;
    getRefsByType(refType);
})

function deleteRef(type, code) {
    fetch(
        `/api/admin/refs/${type}/${code}`, {
            method: 'DELETE',
        }
    ).then((res) => {
        if (res.status === 204) {
            showMessage('success', `Deleted!`);
            getRefsByType(type);
        } else {
            return res.text().then(text => {
                throw new Error(text)
            })
        }
    }).catch(err => {
        console.error(err);
        showMessage('danger', err);
    })
}

function updateRef(type, code, title) {
    fetch(
        `/api/admin/refs/${type}/${code}?title=${title}`, {
            method: 'PUT'
        }
    ).then((res) => {
        if (res.status === 204) {
            showMessage('success', `Updated!`);
            getRefsByType(type);
        } else {
            return res.text().then(text => {
                throw new Error(text)
            })
        }
    }).catch(err => {
        console.error(err);
        showMessage('danger', err);
    })
}

function createRef(ref) {
    fetch(
        `/api/admin/refs`, {
            method: 'POST',
            body: JSON.stringify(ref),
            headers: {
                'Content-Type': "application/json"
            }
        }
    ).then((res) => {
        if (res.status === 201) {
            showMessage('success', `Created!`);
            getRefsByType(ref.refType);
        } else {
            return res.text().then(text => {
                throw new Error(text)
            })
        }
    }).catch(err => {
        console.error(err);
        showMessage('danger', err);
    })
}


function getRefsByType(refType) {
    let table = document.getElementById('table-body');
    while (table.rows.length > 0) {
        table.deleteRow(0);
    }
    let fetchRes = fetch(
        `/api/admin/refs/${refType}`, {
            method: 'GET'
        }
    );
    fetchRes.then(res =>
        res.json()).then(refs => {
        Object.values(refs).map((ref, k) => {
            let row = table.insertRow(k);
            row.insertCell(0).innerHTML = ref.title;
            row.insertCell(1).innerHTML = ref.code;
            row.insertCell(2).innerHTML = ref.aux;
            let cell4 = row.insertCell(3);
            cell4.style.textAlign = 'right';
            cell4.innerHTML = `<a href="#" id="edit" data-bs-toggle="modal" data-bs-target="#modalRef" 
					data-bs-title=${ref.title} data-bs-code=${ref.code} data-bs-aux=${ref.aux} data-bs-type=${ref.refType}><span class="fa fa-pen"></span></a>`;
            let cell5 = row.insertCell(4);
            cell5.style.textAlign = 'center';
            cell5.innerHTML = `<a href="#" id="delete-ref" onclick="deleteRef('${ref.refType}','${ref.code}')"><span class="fa fa-remove"></span></a>`;
        })
    })
}

function showMessage(type, message) {
    let alert = document.getElementById('alert');
    let div = document.createElement("div");
    div.innerHTML = `<div class="alert alert-${type}"  role="alert">${message}</div>`
    div.style.position = 'fixed';
    div.style.zIndex = '9999';
    div.style.marginTop = '-1.8em';
    alert.appendChild(div);
    setTimeout(() => {
        alert.removeChild(div);
    }, 3000)
}

modalRef.addEventListener('show.bs.modal', function (event) {
    let button = event.relatedTarget;
    modalCaller = button.getAttribute('id');
    let title = button.getAttribute('data-bs-title');
    let code = button.getAttribute('data-bs-code');
    let aux = button.getAttribute('data-bs-aux');

    let modalTitle = modalRef.querySelector('.modal-title');
    if (modalCaller === 'create') {
        modalTitle.textContent = `Create new reference`;
        modalRef.querySelector('#ref-code').removeAttribute('disabled');
        modalRef.querySelector('#ref-aux').removeAttribute('disabled');
        modalRef.querySelector('#ref-type').removeAttribute('disabled');
        modalRef.querySelector('#ref-code').setAttribute('required', 'true');
        modalRef.querySelector('#ref-title').setAttribute('required', 'true');
        modalRef.querySelector('#ref-type').setAttribute('required', 'true');
    } else {
        modalTitle.textContent = `Edit reference with title ${title}`;
        modalRef.querySelector('#ref-code').setAttribute('disabled', 'true');
        modalRef.querySelector('#ref-aux').setAttribute('disabled', 'true');
        modalRef.querySelector('#ref-type').setAttribute('disabled', 'true');
        modalRef.querySelector('#ref-type').value = button.getAttribute('data-bs-type');
    }
    modalRef.querySelector('#ref-title').value = title;
    modalRef.querySelector('#ref-code').value = code;
    modalRef.querySelector('#ref-aux').value = aux;

})

document.getElementById('modal-form').addEventListener('submit', (e) => {
    console.log(modalCaller);
    e.preventDefault();
    let title = modalRef.querySelector('#ref-title').value;
    let code = modalRef.querySelector('#ref-code').value;
    let aux = modalRef.querySelector('#ref-aux').value;
    let type = modalRef.querySelector('#ref-type').value;
    if (modalCaller === 'edit') {
        updateRef(type, code, title);
    }
    if (modalCaller === 'create') {
        const ref = {};
        ref.title = title;
        ref.code = code;
        ref.refType = type;
        ref.aux = aux;
        createRef(ref);
    }
    bootstrap.Modal.getInstance(modalRef).hide();
})
