package models

import play.api.db.DB
import play.api.Play.current
import org.joda.time.DateTime
import anorm.SQL
import java.util.Date

case class Tweet(
  id: Int, content: String, createdTime: Date
)

object Tweet {

  def selectList(page: Int, limit:Int): List[Tweet] = {
    DB.withConnection { implicit c =>
      val selectQuery = SQL("select * from tweet order by createdTime desc limit {limitStart}, {limit}")
        .on('limitStart -> (page * limit), 'limit -> limit)

      selectQuery().map(row =>
        Tweet(row[Int]("id"), row[String]("content"), row[Date]("createdTime"))
      ).toList
    }
  }

  def create(content: String): Unit = {
    DB.withConnection { implicit c =>
      SQL("insert into tweet(content, createdTime) values ({content}, {createdTime})")
        .on('content -> content, 'createdTime -> DateTime.now().toDate()).executeInsert()
    }
  }

  def delete(id: Int): Unit = {
    DB.withConnection { implicit c =>
      SQL("delete from tweet where id = {id}").on('id -> id).executeUpdate()
    }
  }

  def hasPrevPage(page: Int, limit:Int): Boolean = {
    hasPage(page - 1, limit)
  }

  def hasNextPage(page: Int, limit:Int): Boolean = {
    hasPage(page + 1, limit)
  }

  private def hasPage(page: Int, limit:Int): Boolean = {
    if (page < 0) {
      false
    } else {
      val list = selectList(page, limit)
      list.size > 0
    }
  }
}
