const codeTitleMap = new Map();

function changeInputContactCodeToTitleAndReturnCode(row) {
    let typeCode = row.cells[0].children[0].value;
    const typeTitle = codeTitleMap.get(typeCode);
    if (typeTitle !== undefined) {
        row.cells[0].children[0].value = typeTitle;
        return typeCode;
    } else {
        return row.cells[0].children[1].value;
    }
}

function buildHiddenContactTypeCodeInput(counter, typeCode, row) {
    const hiddenInput = document.createElement('input');
    hiddenInput.type = 'hidden';
    hiddenInput.name = 'contacts[' + counter + '].code';
    hiddenInput.value = typeCode;
    row.cells[0].appendChild(hiddenInput);
}

function addRemoveRowButtonIfAbsent(row) {
    if (row.cells[2].children[0] === undefined) {
        const button = document.createElement('input');
        button.type = 'button';
        button.className = 'btn btn-close';
        button.onclick = (ev) => removeContactRow(ev)
        row.cells[2].appendChild(button);
    }
}

function refreshContactTableElements() {
    let counter = 0;
    for (let row of document.getElementById('contactsTableBody').rows) {
        let typeCode = changeInputContactCodeToTitleAndReturnCode(row);
        if (row.cells[0].children[1] !== undefined) {
            row.cells[0].children[1].remove();
        }
        buildHiddenContactTypeCodeInput(counter, typeCode, row);
        row.cells[1].children[0].name = 'contacts[' + counter + '].value';
        addRemoveRowButtonIfAbsent(row);
        counter++;
    }
}

function addContactRow(event) {
    event.target.disabled = true;
    const row = document.getElementById('contactsTableBody').insertRow(0);
    buildFirstCell(row, event);
    buildSecondCell(row);
    refreshContactTableElements();
}

function buildFirstCell(row, event) {
    let cell1 = row.insertCell(0);
    cell1.className = 'col-sm-3';
    const readonlyInput = document.createElement('input');
    readonlyInput.value = event.target.value;
    readonlyInput.className = 'form-control-plaintext';
    readonlyInput.readOnly = true;
    cell1.appendChild(readonlyInput);
}

function buildSecondCell(row) {
    let cell2 = row.insertCell(1);
    const input = document.createElement('input');
    input.className = 'col-sm-9';
    input.type = 'text';
    input.setAttribute('maxlength', '32');
    cell2.appendChild(input);
    row.insertCell(2);
}

function removeContactRow(event) {
    const row = event.target.closest('tr');
    const value = row.cells[0].children[0].value;
    document.getElementById('add' + value).disabled = false;
    row.remove();
    refreshContactTableElements();
}

document.addEventListener('DOMContentLoaded', function () {
    for (let contact of document.getElementsByClassName('addContactButton')) {
        codeTitleMap.set(contact.value, contact.textContent);
        contact.addEventListener('click', ev => addContactRow(ev))
    }
    refreshContactTableElements();
}, false);