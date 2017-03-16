<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" />
</head>
<body>
<dl>
<#list unreadNovels as unreadNovel>
  <dt><a href="${unreadNovel.url}">${unreadNovel.title}</a></dt>
  <#list unreadNovel.novelChapters as unreadNovelChapter>
    <dd>
      ${unreadNovelChapter.novelChapterInfo.modifiedDate?string["yyyy/MM/dd"]}
      <a href="${unreadNovelChapter.url}">${unreadNovelChapter.title}</a>
      <#if unreadNovelChapter.createDate != unreadNovelChapter.updateDate>(更新)</#if>
    </dd>
  </#list>
</#list>
</dl>
</body>
</html>
