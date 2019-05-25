<#--

    Symphony - A modern community (forum/BBS/SNS/blog) platform written in Java.
    Copyright (C) 2012-present, b3log.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

-->
<li>
    <div class="article_sys_item">
        <#if article.articleAnonymous == 0>
            <a rel="nofollow" style="background-image:url('${article.articleAuthorThumbnailURL48}')" class="avatar" href="${servePath}/member/${article.articleAuthorName}"></a>
        </#if>
        <#if article.articleAnonymous != 0>
            <div class="avatar" style="background-image:url('${article.articleAuthorThumbnailURL48}')"></div>
        </#if>
        <div class="contents">
<#--最多2行显示，置顶图标+内容-->
            <@icon article.articlePerfect article.articleType></@icon>
            <div class="post_info">
                <a class="abstract" href="${servePath}${article.articlePermalink}">
                    ${article.articlePreviewContent}
                </a>
                <div class="reply_info">
                    <div class="reply_info_count">
                        <#if article.articleCommentCount != 0>
                            <a class="ft-fade tooltipped tooltipped-se" aria-label="跟贴计数" href="${servePath}${article.articlePermalink}#comments"><b class="article-level<#if article.articleCommentCount lt 40>${(article.articleCommentCount/10)?int}<#else>4</#if>">${article.articleCommentCount}</b></a>
                        </#if>
                        <#if article.articleViewCount != 0>
                            <a class="ft-fade  tooltipped tooltipped-se" href="${servePath}${article.articlePermalink}" aria-label="浏览计数">
                                <span class="article-level<#if article.articleViewCount lt 400>${(article.articleViewCount/100)?int}<#else>4</#if>"><#if article.articleViewCount < 1000>${article.articleViewCount}<#else>${article.articleViewCntDisplayFormat}</#if></span>
                               </a>
                        </#if>
                    </div>
                    <div class="fn-ellipsis ft-fade ft-smaller list-info">
                        <#if article.articleAnonymous == 0>
                        <a rel="nofollow" class="author"
                           href="${servePath}/member/${article.articleAuthorName}"></#if>
                            ${article.articleAuthorName}
                            <#if article.articleAnonymous == 0></a></#if>

                        <#if article.articleAuthor.userIntro != '' && article.articleAnonymous == 0>
                            - ${article.articleAuthor.userIntro}
                        </#if>
                        <br>
                        <#if "" != article.articleLatestCmterName>
                            <#if article.articleLatestCmterName != 'someone'>
                                <a rel="nofollow" class="author" href="${servePath}/member/${article.articleLatestCmterName}"></#if><span class="author">${article.articleLatestCmterName}</span><#if article.articleLatestCmterName != 'someone'></a>
                        </#if>
                            ${article.cmtTimeAgo}${cmtLabel}
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>

</li>