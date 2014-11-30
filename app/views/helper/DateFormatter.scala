package views.helper

import java.util.Date
import java.text.SimpleDateFormat

object DateFormatter{
  def format(date: Date, formatString: String): String = {
    val df = new SimpleDateFormat(formatString)
    df.format(date)
  }
}
