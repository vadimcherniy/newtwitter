<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>
<@c.page>
${message?ifExists}
<div class="mb-2">Add new user:</div>
<@l.login "/registration" true/>
</@c.page>