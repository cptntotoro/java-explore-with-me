<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%--@elvariable id="userForm" type="ru.practicum.user.dto.NewUserRequestDto"--%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>Регистрация</title>
</head>

<body>
<div class="container">

<%-- На этой странице используется тег form из библиотеки тегов,
с помощью него осуществляется связка атрибута модели userForm
(мы добавили его на страницу при GET запросе в контроллере) и формы: --%>

  <form:form method="POST" modelAttribute="userForm">
    <h2>Регистрация</h2>
    <div>

      <form:input type="text" path="name" placeholder="Name"/>

    </div>

    <div>
    <%-- Также необходимо указать путь для привязки свойств userForm: --%>
<%--      <form:input type="text" path="username" placeholder="Username" autofocus="true"></form:input>--%>
<%--      <form:errors path="username"></form:errors>      --%>
      <form:input type="text" path="username" placeholder="Username" autofocus="true" />
      <form:errors path="username" />
<%--            ${usernameError}--%>
    </div>

    <div>
        <%--      <form:input type="password" path="password" placeholder="Password"></form:input>--%>
      <form:input type="email" path="email" placeholder="Email" />
    </div>

    <div>
<%--      <form:input type="password" path="password" placeholder="Password"></form:input>--%>
      <form:input type="password" path="password" placeholder="Password" />
    </div>

    <div>
<%--      <form:input type="password" path="passwordConfirm" placeholder="Confirm your password"></form:input>--%>
<%--      <form:errors path="password"></form:errors>--%>
      <form:input type="password" path="passwordConfirm" placeholder="Confirm your password" />
      <form:errors path="password" />
<%--        ${passwordError}--%>
    </div>

    <button type="submit">Зарегистрироваться</button>
  </form:form>

  <a href="/">Главная</a>
</div>
</body>
</html>
