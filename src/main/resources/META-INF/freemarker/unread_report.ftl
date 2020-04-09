<#escape x as x?html>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" />
</head>
<body>
<dl>
<#list userNovelInfos as userNovelInfo>
    <dt>
        <#if userNovelInfo.favorite>★</#if>
        <a href="${userNovelInfo.novel.url}">${userNovelInfo.novel.title}</a>
    </dt>
  <#list userNovelInfo.novel.novelChapters as novelChapter>
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
