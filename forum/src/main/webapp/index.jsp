<!DOCTYPE html>
<html>
<head>
    <title>Test</title>
</head>
<body>
<h2>Welcome!</h2>

<h3>GET: http://localhost/users - getAllUsers</h3>
<button onclick="GET('http://localhost/users/')">GET</button>
<br><br>

<h3>GET: http://localhost/users/{userId} - getUserById</h3>
<input type="text" id="userId" placeholder="userId">
<button onclick="GET('http://localhost/users/')">GET</button>
<br><br>

<h3>POST: http://localhost/users - add a new User</h3>
<br>
<input type="text" id="login" placeholder="login">
<input type="text" id="password" placeholder="password">
<button onclick="postNewUser()">POST</button>


<br><br><br><br><br><br>
<p class="result" style="color:black">


</body>

<script>
    function postNewUser() {
        let login = document.querySelector('#login');
        let password = document.querySelector('#password');
        let result = document.querySelector('.result');
        let xhr = new XMLHttpRequest();
        let url = "http://localhost/users";
        xhr.open("POST", url, true);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                result.innerHTML = this.responseText;
            }
        };
        const data = JSON.stringify({"login": login.value, "password": password.value});
        xhr.send(data);
    }

    function GET(url) {
        let id = document.querySelector('#userId');
        let result = document.querySelector('.result');
        let xhr = new XMLHttpRequest();
        let url2 = url + id.value;
        xhr.open("GET", url2, true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                result.innerHTML = this.responseText;
            }
        };
        xhr.send();
    }
</script>