<#escape x as x?html>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" />
</head>
<body>
<dl>
<#list unreadNovels as novel>
    <dt>
        <#if novel.novelInfo.favorite>★</#if>
        <a href="${novel.url}">${novel.title}</a>
    </dt>
  <#list novel.novelChapters as novelChapter>
    <dd>
        ${novelChapter.novelChapterInfo.modifiedDate.format("yyyy/MM/dd")}
        <a href="${novelChapter.url}">${novelChapter.title}</a>
        <#if !novelChapter.createDate.isEqual(novelChapter.updateDate)>(更新)</#if>
    </dd>
  </#list>
</#list>
</dl>
</body>
</html>
</#escape>
