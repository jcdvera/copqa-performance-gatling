package config


import com.typesafe.config._
/**
  * Created by Juan Camilo Dvera on 3/3/21
  */
object Config {

  def users = getProperty("USERS", "5").toInt
  def duration = getProperty("DURATION", "5").toInt

  private def getProperty(propertyName: String, defaultValue: String) = {
    Option(System.getenv(propertyName))
      .orElse(Option(System.getProperty(propertyName)))
      .getOrElse(defaultValue)
  }
}
