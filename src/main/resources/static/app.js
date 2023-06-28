window.onload = function() {
    const savedHost = localStorage.getItem('host');
    const savedPort = localStorage.getItem('port');
    const savedUser = localStorage.getItem('user');
    const savedPassword = localStorage.getItem('password');

    if (savedHost) document.getElementById('host').value = savedHost;
    if (savedPort) document.getElementById('port').value = savedPort;
    if (savedUser) document.getElementById('user').value = savedUser;
    if (savedPassword) document.getElementById('password').value = savedPassword;
}

document.getElementById('mailboxForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const host = document.getElementById('host').value;
    const port = document.getElementById('port').value;
    const user = document.getElementById('user').value;
    const password = document.getElementById('password').value;

    localStorage.setItem('host', host);
    localStorage.setItem('port', port);
    localStorage.setItem('user', user);
    localStorage.setItem('password', password);

    const mailboxConfiguration = {
        host: host,
        port: parseInt(port),
        user: user,
        password: password
    };

    fetch('http://localhost:8080/api/mailbox/configure', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(mailboxConfiguration)
    }).then(function() {
        const source = new EventSource("http://localhost:8080/api/outlook/emails");

        source.onmessage = function(event) {
            const email = JSON.parse(event.data);
            const emailRow = `<tr><td>${email.uuid}</td><td>${email.subject}</td><td>${email.content}</td><td>${JSON.stringify(email.attachment)}</td></tr>`;
            document.querySelector('#emailTable tbody').insertAdjacentHTML('beforeend', emailRow);
        };

        document.getElementById('mailboxForm').style.display = 'none';
    });
});
