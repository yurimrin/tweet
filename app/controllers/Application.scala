package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.Tweet

object Application extends Controller {

  private val linelimit = 3

  private val tweetForm = Form(
    "content" -> nonEmptyText(maxLength = 10)
  )

  def list(page: Int) = Action {
    Ok(views.html.tweet(tweetForm, Tweet.selectList(page, linelimit), page, Tweet.hasNextPage(page, linelimit), Tweet.hasPrevPage(page, linelimit)))
  }

  def tweet(page: Int) = Action {
    implicit request =>
      tweetForm.bindFromRequest.fold(
        errors => BadRequest(
          views.html.tweet(errors, Tweet.selectList(page, linelimit), page, Tweet.hasNextPage(page, linelimit), Tweet.hasPrevPage(page, linelimit))),
        content => {
          Tweet.create(content)
          Redirect(routes.Application.list(page))
        }
      )
  }

  def deleteTweet(page: Int, id: Int) = Action {
    Tweet.delete(id)
    Redirect(routes.Application.list(page))
  }
}