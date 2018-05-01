package com.apitore.twitterio.helper;


import java.util.List;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class TwitterHelper {

  private Twitter twitter;

  public TwitterHelper (
      String CONSUMERKEY,
      String CONSUMERSECRET,
      String ACCESSTOKEN,
      String ACCESSSECRET
      ) {
    ConfigurationBuilder confbuilder = new ConfigurationBuilder();
    confbuilder.setOAuthConsumerKey(CONSUMERKEY);
    confbuilder.setOAuthConsumerSecret(CONSUMERSECRET);
    confbuilder.setOAuthAccessToken(ACCESSTOKEN);
    confbuilder.setOAuthAccessTokenSecret(ACCESSSECRET);

    TwitterFactory twitterfactory = new TwitterFactory(confbuilder.build());
    Twitter twitter = twitterfactory.getInstance();
    this.twitter = twitter;
  }

  public Twitter getTwitter() {
    return this.twitter;
  }


  /**
   * ツイート取得（過去に遡って再帰的に）
   *
   * @param rtn
   * @param query
   * @param iter
   * @return
   */
  public String search(List<Status> rtn, Query query, int iter) {
    long mid = query.getMaxId();
    try {
      while (iter>0) {
        iter--;
        query.setMaxId(mid);
        QueryResult statuses = twitter.search(query);
        if (statuses != null) {
          List<Status> tweets = statuses.getTweets();
          if (tweets.isEmpty())
            break;
          mid = tweets.get(tweets.size()-1).getId()-1;
          rtn.addAll(tweets);
        }
        Thread.sleep(200);
      }
    } catch (TwitterException e) {
      return e.toString();
    } catch (InterruptedException e) {
      return e.toString();
    }
    return "Success";
  }

  /**
   * 自分のツイート取得（過去に遡って再帰的に）
   *
   * @param rtn
   * @param paging
   * @param iter
   * @return
   */
  public String myTweet(List<Status> rtn, Paging paging, int iter) {
    long mid = paging.getMaxId();
    try {
      while (iter>0) {
        iter--;
        if (mid>0)
          paging.setMaxId(mid);
        List<Status> tweets = twitter.getUserTimeline(paging);
        if (tweets.isEmpty())
          break;
        mid = tweets.get(tweets.size()-1).getId()-1;
        rtn.addAll(tweets);
        Thread.sleep(200);
      }
    } catch (TwitterException e) {
      return e.toString();
    } catch (InterruptedException e) {
      return e.toString();
    }
    return "Success";
  }

  /**
   * 自分のタイムライン取得（過去に遡って再帰的に）
   *
   * @param rtn
   * @param paging
   * @param iter
   * @return
   */
  public String myTimeline(List<Status> rtn, Paging paging, int iter) {
    long mid = paging.getMaxId();
    try {
      while (iter>0) {
        iter--;
        if (mid>0)
          paging.setMaxId(mid);
        List<Status> tweets = twitter.getHomeTimeline(paging);
        if (tweets.isEmpty())
          break;
        mid = tweets.get(tweets.size()-1).getId()-1;
        rtn.addAll(tweets);
        Thread.sleep(200);
      }
    } catch (TwitterException e) {
      return e.toString();
    } catch (InterruptedException e) {
      return e.toString();
    }
    return "Success";
  }

}