<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>${title} | Web Checkers</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css" />
</head>
<body>
  <div class="page">

    <h1>Web Checkers</h1>

    <div class="navigation">
      <a href="/">my home</a>
    </div>

    <div class="body">
      <p>
        ${message}
      </p>
      <p>
        Usernames must include at least one letter or number, and may only use
        letters, numbers, and spaces.
      </p>
      <form action="/signin" method="post">
        <label for="username">Username</label>
        <input type="text" id="username" name="username" placeholder="Username" /><br>
        <input type="submit" value="Sign In" />
      </form>
    </div>

  </div>
</body>
</html>
