<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>防盗链演示</title>
</head>
<body>
<div>
    <form method="post" action="/addUser">
        <input name="token" type="text" value="${token}">
        <input name="username" type="text"/>
        <input name="password" type="text"/>
        <input type="submit">
    </form>
</div>
<#--<span>欢迎来到首页</span>-->
<#--<img src="http://80.demo_one.com/img/图片一.jpg"/>-->

</body>
</html>