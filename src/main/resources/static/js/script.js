const modalEditUser = document.getElementById('edit-user-modal')
const modalDeleteUser = document.getElementById('delete-user-modal')

modalEditUser.addEventListener('show.bs.modal', event => {

    const button = event.relatedTarget


    const tableRow = button.parentNode.parentNode;

    const userId = document.getElementById('edited-user-id')
    const userEmail = document.getElementById('edited-user-email')
    const userPassword = document.getElementById('edited-user-password')
    const userName = document.getElementById('edited-user-name')
    const userAge = document.getElementById('edited-user-age')

    userId.value = tableRow.querySelector('td:nth-child(1)').textContent;
    userEmail.value = tableRow.querySelector('td:nth-child(2)').textContent;
    userName.value = tableRow.querySelector('td:nth-child(3)').textContent;
    userAge.value = tableRow.querySelector('td:nth-child(4)').textContent;
})

modalDeleteUser.addEventListener('show.bs.modal', event => {
    console.log('button delete')
    const button = event.relatedTarget
    const tableRow = button.parentNode.parentNode;

    const userId = document.getElementById('delete-user-id')
    const userEmail = document.getElementById('delete-user-email')
    const userName = document.getElementById('delete-user-name')
    const userAge = document.getElementById('delete-user-age')

    userId.value = tableRow.querySelector('td:nth-child(1)').textContent;
    userEmail.value = tableRow.querySelector('td:nth-child(2)').textContent;
    userName.value = tableRow.querySelector('td:nth-child(3)').textContent;
    userAge.value = tableRow.querySelector('td:nth-child(4)').textContent;
})