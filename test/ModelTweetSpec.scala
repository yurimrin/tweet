import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

import models.Tweet
import java.util.Date
import anorm.SQL
import play.api.db.DB
import play.api.Play.current

/**
 * Tweet Test.
 */
class ModelTweetSpec extends Specification {
  sequential

  private val testimit = 3

  // 全件削除（テスト用）
  def allDataClear {
    DB.withConnection { implicit c =>
      SQL("delete from Tweet").executeUpdate()
    }
  }

  // 全件取得（テスト用）
  def allDataList: List[Tweet] = {
    DB.withConnection { implicit c =>
      val sql = SQL("select * from tweet order by id")

      sql().map(row =>
        Tweet(row[Int]("id"), row[String]("content"), row[Date]("createdTime"))
      ).toList
    }
  }

  "Tweet Model" should {

    "be get tweet data list" in running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      // データクリア
      allDataClear

      // 1件登録
      Tweet.create("データ１")

      // データ確認
      val list: List[Tweet] = Tweet.selectList(0, 3)
      list.size must equalTo(1)
      list(0).content must equalTo("データ１")
      list(0).createdTime must not beNull
    }

    "Specify the number of pages, be get tweet data list" in running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      // データクリア
      allDataClear

      // 4件登録
      Tweet.create("データ１")
      Tweet.create("データ２")
      Tweet.create("データ３")
      Tweet.create("データ４")

      // データ確認
      val list1: List[Tweet] = Tweet.selectList(0, 3)
      list1.size must equalTo(3)

      val list2: List[Tweet] = Tweet.selectList(1, 3)
      list2.size must equalTo(1)
    }

    "Specify the number of rows, be get tweet data list" in running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      // データクリア
      allDataClear

      // 4件登録
      Tweet.create("データ１")
      Tweet.create("データ２")
      Tweet.create("データ３")
      Tweet.create("データ４")

      // データ確認
      val list1: List[Tweet] = Tweet.selectList(0, 1)
      list1.size must equalTo(1)
    }

    "create tweet data" in running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      // データクリア
      allDataClear

      // 1件登録
      Tweet.create("データ１")

      // 1件かどうかテスト
      val list: List[Tweet] = allDataList
      list.size must equalTo(1)
      list(0).content must equalTo("データ１")
    }

    "delete tweet data" in running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      // データクリア
      allDataClear

      // 1件登録
      Tweet.create("データ１")

      // 登録したデータを削除
      val list: List[Tweet] = allDataList
      for(tweet <- list) {
        Tweet.delete(tweet.id toInt)
      }

      allDataList.size must equalTo(0)
    }

    "has previous page" in running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      // データクリア
      allDataClear

      // 4件登録
      Tweet.create("１")
      Tweet.create("２")
      Tweet.create("３")
      Tweet.create("４")

      // １ページ目はfalse
      Tweet.hasPrevPage(0, testimit) must beFalse

      // ２ページ目はtrue
      Tweet.hasPrevPage(1, testimit) must beTrue
    }

    "has next page" in running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      // データクリア
      allDataClear

      // 4件登録
      Tweet.create("１")
      Tweet.create("２")
      Tweet.create("３")
      Tweet.create("４")

      // １ページ目はtrue
      Tweet.hasNextPage(0, testimit) must beTrue

      // ２ページ目はfalse
      Tweet.hasNextPage(1, testimit) must beFalse
    }
  }
}
