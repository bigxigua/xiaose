package org.b3log.symphony.service;

import org.b3log.latke.cache.Cache;
import org.b3log.latke.cache.redis.RedisCache;
import org.b3log.latke.ioc.Inject;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.service.annotation.Service;
import org.b3log.symphony.cache.CommunalCache;
import org.b3log.symphony.model.Comment;
import org.b3log.symphony.model.UserExt;
import org.b3log.symphony.util.Symphonys;
import org.json.JSONObject;

import java.util.*;

/**
 * <p>Create Time: 2019年05月21日</p>
 * <p>@author tangxd</p>
 **/
@Service
public class ArticleCommentService {
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ArticleCommentService.class);

    private final static String ARTICLE_COMMENT_MEMBERS_KEY = "article_comment_memebrs:%s";

    @Inject
    private CommunalCache cache;

    @Inject
    private UserQueryService userQueryService;

    @Inject
    private CommentQueryService commentQueryService;

    private boolean isRedisCache() {
        return cache.get() instanceof RedisCache;
    }

    private RedisCache rCache() {
        return (RedisCache) cache.get();
    }

    public void addArticleCommentMemeber(final JSONObject comment) {
        if (!isRedisCache()) {
            return;
        }
        String articleId = comment.getString(Comment.COMMENT_ON_ARTICLE_ID);
        rCache().zAdd(String.format(ARTICLE_COMMENT_MEMBERS_KEY, articleId),
                comment.getDouble(Comment.COMMENT_CREATE_TIME),
                new JSONObject().put(Comment.COMMENT_AUTHOR_ID, comment.getString(Comment.COMMENT_AUTHOR_ID)));
    }

    public List<JSONObject> getArticleCommentMembers(final String articleId, final int limit) {
        if (!isRedisCache()) {
            return Collections.emptyList();
        }
        final String key = String.format(ARTICLE_COMMENT_MEMBERS_KEY, articleId);
        List<JSONObject> uids = Collections.emptyList();
        if (rCache().existKey(key)) {
            uids = rCache().zGet(key, limit);
        } else {
            List<JSONObject> comments = commentQueryService.getArticleComments(articleId, 1, Symphonys.ARTICLE_COMMENTS_CNT, UserExt.USER_COMMENT_VIEW_MODE_C_TRADITIONAL);
            if (!comments.isEmpty()) {
                Map<String, Double> values = new LinkedHashMap<>();
                for (JSONObject cmt : comments) {
                    values.put(new JSONObject().put(Comment.COMMENT_AUTHOR_ID, cmt.getString(Comment.COMMENT_AUTHOR_ID)).toString(),
                            cmt.getDouble(Comment.COMMENT_CREATE_TIME));
                }
                rCache().zAdd(key, values);
                uids = rCache().zGet(key, limit);
            }
        }
        List<JSONObject> members = new ArrayList<>();
        for (JSONObject uid : uids) {
            JSONObject member = userQueryService.getUser(uid.getString(Comment.COMMENT_AUTHOR_ID));
            members.add(member);
        }
        return members;
    }
}
