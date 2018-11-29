<#import "parts/common.ftl" as c>

<@c.page>
User editor
<form method="post" action="/user">
    <input type="hidden" value="${_csrf.token}" name="_csrf">
    <input type="hidden" value="${user.id}" name="userId">
    <input type="text" name="name" value="${user.name}">
    <#list roles as role>
    <div>
        <label><input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked", "")}>${role}</label>
    </div>
    </#list>
    <button type="submit">Submit</button>
</form>
</@c.page>