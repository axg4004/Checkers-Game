<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="refresh" content="10">
    <title>${title} | Web Checkers</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css" />
</head>
<body>
  <div class="page">
  
    <h1>Web Checkers</h1>
    
    <div class="navigation">
      <#if isUserSignedIn == true>
      <a href="/signout">sign out</a>
      <#else>
      <a href="/signin">sign in</a>
      </#if>
      <a href="/">my home</a>
    </div>
    
    <div class="body">
      <p>Welcome to the world of online Checkers.</p>
      <#if isUserSignedIn == true>
        <ul style="list-style-type:none">
        <#list signedInPlayers as player>
            <li>
            <form method = "get" action = "/game">
            <input type="hidden" name="username" value="${player}">
            <input type="submit" value="${player}">
            </form>
            </li>
        </#list>
        </ul>
        <p>Current games:</p>
        <ul style="list-style-type:none">
            <#list currentGameOpponentNames as player>
              <li>
              <form method = "get" action = "/game">
              <input type="hidden" name="id" value="${currentGameIDs[player?index]}">
              <input type="submit" value="${player}">
              </form>
              </li>
        </#list>
        </ul>
      <#else>
        <p> Number of Players online: ${numPlayersOnline}</p>
      </#if>

    </div>
    
  </div>
</body>
</html>
