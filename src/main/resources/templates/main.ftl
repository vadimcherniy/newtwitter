<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
<div>
    <@l.logout/>
</div>
<div>
    <form method="post">
        <input type="text" name="message" placeholder="Put the message"/>
        <input type="text" name="tag" placeholder="Tag"/>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button type="submit">Submit</button>
    </form>
</div>
<div>Message list</div>
<form method="get" action="/main">
    <input type="text" name="tag"/>
    <button type="submit">Find</button>
</form>
<#list messages as message>
<div>
    <b>${message.id}</b>
    <span>${message.message}</span>
    <i>${message.tag}</i>
    <strong>${message.authorName}</strong>
</div>
</#list>
</@c.page>