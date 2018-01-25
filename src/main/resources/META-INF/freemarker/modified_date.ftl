<#escape x as x?html>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" />
</head>
<body>
<dl>
<#list novels as novel>
    <dt>
        <#if novel.novelInfo.favorite>★</#if>
        <a href="${novel.url}">${novel.title}</a>
        ${novel.novelInfo.modifiedDate?string["yyyy/MM/dd"]}
    </dt>
</#list>
</dl>
</body>
</html>
</#escape>
