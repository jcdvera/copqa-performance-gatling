package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import config.Config._
import scala.concurrent.duration._

/**
  * Created by Juan Camilo Dvera on 3/3/21
  */
class ParameterizedBuild extends Simulation{

  val baseURL = "http://localhost:8081"

  val testData = csv("testData.csv").circular

  val httpProtocol = http.baseUrl(baseURL)

  val transferMoney = scenario("third scenario")
    .exec(http("Get Access Token")
      .post("/auth")
      .header("Content-Type", "application/x-www-form-urlencoded")
      .header("Authorization", "Basic anVhbjpWYWxpZFBhc3MxMjM=")
      .check(status.is(200))
      .check(jsonPath("$.access_token").saveAs("accessToken"))
    )
    .exec(session => {
      println("ACCESS TOKEN ----> " + session("accessToken").as[String])
      session
    })
    .feed(testData)
    .exec(http("Send Monwy")
      .post("/account/1234/wiretransfer")
      .header("Authorization", "${accessToken}")
      .header("Content-Type", "application/json")
      .body(ElFileBody("transferBody.json")).asJson
      .check(status.is(200))
      .check(bodyString.saveAs("responseBody"))
    )

  setUp(transferMoney.inject(rampUsers(users) during(duration seconds)).protocols(httpProtocol))
}
