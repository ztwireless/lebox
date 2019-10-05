package com.mgc.letobox.happy.find.util;

import android.content.Context;
import android.support.annotation.Keep;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kymjs.rxvolley.RxVolley;
import com.ledong.lib.leto.mgc.model.PendingMGCRequest;
import com.ledong.lib.leto.utils.JsonUtil;
import com.leto.game.base.bean.BaseRequestBean;
import com.leto.game.base.bean.LetoError;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.http.HttpParamsBuild;
import com.leto.game.base.util.MResource;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.circle.bean.CircleCreateTieZiRequest;
import com.mgc.letobox.happy.circle.bean.CircleIdRequest;
import com.mgc.letobox.happy.circle.bean.CircleQuitRequest;
import com.mgc.letobox.happy.circle.bean.CircleTieZiListRequest;
import com.mgc.letobox.happy.circle.bean.CreateCircleBean;
import com.mgc.letobox.happy.circle.bean.PostCommentRequest;
import com.mgc.letobox.happy.circle.bean.PostIdRequest;
import com.mgc.letobox.happy.find.bean.ArticleCommentListRequestBean;
import com.mgc.letobox.happy.find.bean.ArticleCommentRequestBean;
import com.mgc.letobox.happy.find.bean.ArticleDetailRequestBean;
import com.mgc.letobox.happy.find.bean.ArticleRequestBean;
import com.mgc.letobox.happy.find.bean.FollowRequestBean;
import com.mgc.letobox.happy.find.bean.GameCommentListRequestBean;
import com.mgc.letobox.happy.find.bean.GameCommentRequestBean;
import com.mgc.letobox.happy.find.bean.GameDetailRequestBean;
import com.mgc.letobox.happy.find.bean.GameFavoriteRequestBean;
import com.mgc.letobox.happy.find.bean.KOLRequest;
import com.mgc.letobox.happy.find.bean.LikeRequestBean;
import com.mgc.letobox.happy.find.bean.PageSizeRequest;
import com.mgc.letobox.happy.find.model.NewsCategory;

import java.util.List;

@Keep
public class FindApiUtil {
	/**
	 * 延迟执行一个mgc api请求, 先获取全局配置
	 */
	private static void delayExecute(final Context ctx, final PendingMGCRequest req) {
//		getNewsCategory(ctx, new HttpCallbackDecode<List<NewsCategory>>(ctx, null, new TypeToken<List<NewsCategory>>() {
//		}.getType()) {
//			@Override
//			public void onDataSuccess(List<NewsCategory> data) {
//				if(data != null) {
//					doRequest(ctx, req);
//				} else {
//					req.callback.onFailure("500",
//						ctx.getString(MResource.getIdByName(ctx, "R.string.leto_mgc_failed_get_coin_config")));
//				}
//			}
//
//			@Override
//			public void onFailure(String code, String msg) {
//				// call failure
//				if(req != null && req.callback != null) {
//					req.callback.onFailure("500", msg);
//				}
//			}
//		});
	}

	private static void delayOrNow(Context ctx, BaseRequestBean bean, Object url, boolean post, boolean encrypt, HttpCallbackDecode callback) {
		if(true) {
			doRequest(ctx, bean, url, post, encrypt, callback);
		} else {
			PendingMGCRequest req = new PendingMGCRequest();
			req.bean = bean;
			req.url = url;
			req.callback = callback;
			req.post = post;
			req.encrypt = encrypt;
			delayExecute(ctx, req);
		}
	}

	private static void doRequest(Context ctx, PendingMGCRequest req) {
		doRequest(ctx, req.bean, req.url, req.post, req.encrypt, req.callback);
	}

	private static void doRequest(Context ctx, BaseRequestBean bean, Object url, boolean post, boolean encrypt, HttpCallbackDecode callback) {
		// decide url, url can be UrlChooser or String
		String urlStr = "";
		if(url instanceof String) {
			urlStr = (String) url;
		} else if(url instanceof PendingMGCRequest.UrlChooser) {
			urlStr = ((PendingMGCRequest.UrlChooser) url).chooseUrl();
		}

		try {
			// get args
			String args = new Gson().toJson(bean);

			// log
			Log.d("LetoHttp", String.format("http req: url: %s, args: %s", urlStr, args));

			// request
			if(post) {
				// build http params, encrypt or not
				HttpParamsBuild httpParamsBuild = new HttpParamsBuild(args, encrypt);
				if(encrypt) {
					callback.setAuthkey(httpParamsBuild.getAuthkey());
				}
				(new RxVolley.Builder()).url(urlStr).params(httpParamsBuild.getHttpParams()).httpMethod(1).callback(callback).setTag(ctx).shouldCache(false).doTask();
			} else {
				// get
				String urlArg = JsonUtil.getMapParams(args);
				(new RxVolley.Builder())
					.setTag(ctx)
					.shouldCache(false)
					.url(urlStr + "?" + urlArg)
					.params(HttpParamsBuild.createHeaders())
					.callback(callback)
					.doTask();
			}
		} catch(Exception e) {
			e.printStackTrace();
			if(callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void getNewsCategory(final Context ctx, final HttpCallbackDecode callback) {
		try {
			// build request
			BaseRequestBean bean = new BaseRequestBean();

			// wrap callback, so we can preprocess
			HttpCallbackDecode cb = new HttpCallbackDecode<List<NewsCategory>>(ctx, null, new TypeToken<List<NewsCategory>>() {
			}.getType()) {
				@Override
				public void onDataSuccess(List<NewsCategory> data) {
					if(data != null) {
						// save globally
						MgctUtil.saveJson(ctx, new Gson().toJson(data), MgctUtil.NEWS_CATEGORY);

						// forward
						if(callback != null) {
							callback.onDataSuccess(data);
						}
					} else {
						if(callback != null) {
							callback.onFailure("500", ctx.getString(R.string.lebox_get_news_category_failed));
						}
					}
				}

				@Override
				public void onFailure(String code, String msg) {
					super.onFailure(code, msg);
					if(callback != null) {
						callback.onFailure(code, msg);
					}
				}

				@Override
				public void onFinish() {
					super.onFinish();
					if(callback != null) {
						callback.onFinish();
					}
				}
			};
			cb.setShowTs(false);
			cb.setLoadingCancel(false);
			cb.setShowLoading(false);
			cb.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			String args = new Gson().toJson(bean);
			HttpParamsBuild httpParamsBuild = new HttpParamsBuild(args, false);
			(new RxVolley.Builder())
				.url(FindApi.getNewsCategory())
				.params(httpParamsBuild.getHttpParams())
				.httpMethod(1)
				.callback(cb)
				.setTag(ctx)
				.shouldCache(false)
				.doTask();
		} catch(Exception e) {
			e.printStackTrace();
			if(callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void getArticleList(Context ctx, int type, int page, int categoryId, HttpCallbackDecode callback) {
		try {
			ArticleRequestBean bean = new ArticleRequestBean();
			bean.setType(type);
			bean.setPage(page);
			bean.setCategory(categoryId);
			String url = FindApi.getArticleList();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void followUser(Context ctx, int memId, int type, HttpCallbackDecode callback) {
		try {
			FollowRequestBean bean = new FollowRequestBean();
			bean.setFollow_who(memId);
			bean.setType(type);
			String url = FindApi.getUserFollow();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void loadArticleData(Context ctx, int id, HttpCallbackDecode callback) {
		try {
			ArticleDetailRequestBean bean = new ArticleDetailRequestBean();
			bean.setNews_id(id);
			String url = FindApi.getNewsDetail();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void loadCommentData(Context ctx, int newsId, int page, HttpCallbackDecode callback) {
		try {
			ArticleCommentListRequestBean bean = new ArticleCommentListRequestBean();
			bean.setNews_id(newsId);
			bean.page = page;
			String url = FindApi.getNewsCommentList();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void kolComment(Context ctx, int newsId, String content, HttpCallbackDecode callback) {
		try {
			ArticleCommentRequestBean bean = new ArticleCommentRequestBean();
			bean.setNews_id(newsId);
			bean.setContent(content);
			String url = FindApi.getNewsComment_twhp();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void gameComment(Context ctx, int gameId, String content, int star, HttpCallbackDecode callback) {
		try {
			GameCommentRequestBean bean = new GameCommentRequestBean();
			bean.setGame_id(gameId);
			bean.setContent(content);
			bean.setStar(star);
			String url = FindApi.getGameCommentTwhp();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void gameFavorite(Context ctx, String gameId, int type, HttpCallbackDecode callback) {
		try {
			GameFavoriteRequestBean bean = new GameFavoriteRequestBean();
			bean.setType(type);
			bean.setGame_id(gameId);
			String url = FindApi.getFavoriteGame();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void getGameDetail(Context ctx, String gameId, HttpCallbackDecode callback) {
		try {
			GameDetailRequestBean bean = new GameDetailRequestBean();
			bean.setGameid(gameId);
			String url = FindApi.getGameDetail();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, false, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void getGameCommentList(Context ctx, int gameId, int page, int offset, HttpCallbackDecode callback) {
		try {
			GameCommentListRequestBean bean = new GameCommentListRequestBean();
			bean.setGame_id(gameId);
			bean.setPage(page);
			bean.setOffset(offset);
			String url = FindApi.getGameCommentList();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void circleComment(Context ctx, int articleId, String content, HttpCallbackDecode callback) {
		try {
			PostCommentRequest bean = new PostCommentRequest();
			bean.setPost_id(articleId);
			bean.setContent(content);
			String url = FindApi.getPostComment_twhp();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void likeKOLArticle(Context ctx, int articleId, int type, HttpCallbackDecode callback) {
		try {
			LikeRequestBean bean = new LikeRequestBean();
			bean.setAppname("News");
			bean.setTable("detail");
			bean.setRow(articleId);
			bean.setJump("");
			String url = FindApi.getSupportComment();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void likeCircleArticle(Context ctx, int articleId, int type, HttpCallbackDecode callback) {
		try {
			LikeRequestBean bean = new LikeRequestBean();
			bean.setAppname("Group");
			bean.setTable("post");
			bean.setRow(articleId);
			bean.setJump("");
			String url = FindApi.getSupportComment();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void likeCircleComment(Context ctx, int commentId, int type, HttpCallbackDecode callback) {
		try {
			LikeRequestBean bean = new LikeRequestBean();
			bean.setAppname("Group");
			bean.setTable("group_post_reply");
			bean.setRow(commentId);
			bean.setJump("");
			String url = FindApi.getSupportComment();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void likeGameComment(Context ctx, int commentId, int type, HttpCallbackDecode callback) {
		try {
			LikeRequestBean bean = new LikeRequestBean();
			bean.setAppname("Game");
			bean.setTable("local_comment");
			bean.setRow(commentId);
			bean.setJump("");
			String url = FindApi.getSupportComment();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void likeKOLComment(Context ctx, int commentId, int type, HttpCallbackDecode callback) {
		try {
			LikeRequestBean bean = new LikeRequestBean();
			bean.setAppname("News");
			bean.setTable("local_comment");
			bean.setRow(commentId);
			bean.setJump("");
			String url = FindApi.getSupportComment();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void getKoLDetail(Context ctx, int memId, HttpCallbackDecode callback) {
		try {
			KOLRequest bean = new KOLRequest();
			bean.setKol_id(memId);
			String url = FindApi.getKoLDetail();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void getUserGame(Context ctx, int memId, int page, HttpCallbackDecode callback) {
		try {
			KOLRequest bean = new KOLRequest();
			bean.setKol_id(memId);
			bean.setPage(page);
			String url = FindApi.getUserGame();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void getNewsList(Context ctx, int memId, int page, HttpCallbackDecode callback) {
		try {
			KOLRequest bean = new KOLRequest();
			bean.setKol_id(memId);
			bean.setPage(page);
			String url = FindApi.getNewsList();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void getMyGuoups(Context ctx, int page, HttpCallbackDecode callback) {
		try {
			PageSizeRequest bean = new PageSizeRequest();
			bean.setPage(page);
			String url = FindApi.getMyGuoups();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void getHotGroups(Context ctx, int page, HttpCallbackDecode callback) {
		try {
			PageSizeRequest bean = new PageSizeRequest();
			bean.setPage(page);
			String url = FindApi.getHotGroups();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void getCircleGroups(Context ctx, int page, HttpCallbackDecode callback) {
		try {
			PageSizeRequest bean = new PageSizeRequest();
			bean.setPage(page);
			String url = FindApi.getCircleGroups();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void getCirclePostList(Context ctx, int groupId, int page, HttpCallbackDecode callback) {
		try {
			CircleTieZiListRequest bean = new CircleTieZiListRequest();
			bean.setGroup_id(groupId);
			bean.setPage(page);
			String url = FindApi.getCirclePostList();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void getCircleDetail(Context ctx, int groupId, HttpCallbackDecode callback) {
		try {
			CircleIdRequest bean = new CircleIdRequest();
			bean.setId(groupId);
			String url = FindApi.getCircleDetail();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void getInformCircle(Context ctx, int groupId, HttpCallbackDecode callback) {
		try {
			CircleQuitRequest bean = new CircleQuitRequest();
			bean.setGroup_id(groupId);
			String url = FindApi.getInformCircle();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void deletePost(Context ctx, int postId, HttpCallbackDecode callback) {
		try {
			PostIdRequest bean = new PostIdRequest();
			bean.setPost_id(postId);
			String url = FindApi.getPostDel();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void createCircle(Context ctx, String title, String detail, String logo, String bg, HttpCallbackDecode callback) {
		try {
			CreateCircleBean bean = new CreateCircleBean();
			bean.setTitle(title);
			bean.setDetail(detail);
			bean.setLogo(logo);
			bean.setBackground(bg);
			String url = FindApi.getCreateCircle();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void joinCircle(Context ctx, int groupId, HttpCallbackDecode callback) {
		try {
			CircleQuitRequest bean = new CircleQuitRequest();
			bean.setGroup_id(groupId);
			String url = FindApi.getCircleJoin();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void quitCircle(Context ctx, int groupId, HttpCallbackDecode callback) {
		try {
			CircleQuitRequest bean = new CircleQuitRequest();
			bean.setGroup_id(groupId);
			String url = FindApi.getCircleQuit();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void putRichPost(Context ctx, String title, String content, int groupId, HttpCallbackDecode callback) {
		try {
			CircleCreateTieZiRequest bean = new CircleCreateTieZiRequest();
			bean.setTitle(title);
			bean.setContent(content);
			bean.setGroup_id(groupId);
			String url = FindApi.postPut_twhp();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void putPost(Context ctx, String title, String content, int groupId, String attachId, HttpCallbackDecode callback) {
		try {
			CircleCreateTieZiRequest bean = new CircleCreateTieZiRequest();
			bean.setTitle(title);
			bean.setContent(content);
			bean.setGroup_id(groupId);
			bean.setAttach_id(attachId);
			String url = FindApi.postPut();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void editPost(Context ctx, int id, String title, String content, int groupId, HttpCallbackDecode callback) {
		try {
			CircleCreateTieZiRequest bean = new CircleCreateTieZiRequest();
			bean.setId(id);
			bean.setTitle(title);
			bean.setContent(content);
			bean.setGroup_id(groupId);
			String url = FindApi.edit_TieZi_twhp();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void getPostDetail(Context ctx, int id, HttpCallbackDecode callback) {
		try {
			CircleIdRequest bean = new CircleIdRequest();
			bean.setId(id);
			String url = FindApi.getPostDetail();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}

	public static void getPostCommentList(Context ctx, int articleId, int page, HttpCallbackDecode callback) {
		try {
			PostIdRequest bean = new PostIdRequest();
			bean.setPost_id(articleId);
			bean.setPage(page);
			String url = FindApi.getPostCommentList();

			// set callback
			callback.setShowTs(false);
			callback.setLoadingCancel(false);
			callback.setShowLoading(false);
			callback.setLoadMsg(ctx.getResources().getString(MResource.getIdByName(ctx, "R.string.loading")));

			// request
			delayOrNow(ctx, bean, url, true, false, callback);
		} catch (Exception e) {
			e.printStackTrace();
			if (callback != null) {
				callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
			}
		}
	}
}
