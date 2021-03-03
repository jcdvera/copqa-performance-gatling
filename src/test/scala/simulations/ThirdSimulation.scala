package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by Juan Camilo Dvera on 3/2/21
  */
class ThirdSimulation extends Simulation{

  val baseURL = "http://localhost:8081"

  val httpProtocol = http.baseUrl(baseURL)
    .proxy(Proxy("localhost", 8866))

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
    .exec(http("Get Account Details")
      .get("/account/1234/details")
      .header("Authorization", "${accessToken}")
      .check(status.is(200))
      .check(jsonPath("$.account.amount").saveAs("currentMoney"))
    )
    .exec(session => {
      println("CURRENT AMOUNT OF MONEY ----> " + session("currentMoney").as[String])
      session
    })
    .exec(http("Send All Monwy")
      .post("/account/1234/wiretransfer")
      .header("Authorization", "${accessToken}")
      .header("Content-Type", "application/json")
      .body(StringBody(
        "{\n    \"name\": \"juan\",\n    \"accountID\": 123,\n    \"ammount\": ${currentMoney}\n}"
      ))
      .check(status.is(200))
      .check(bodyString.saveAs("responseBody"))
    )
    .exec(session => {
      println("Response Body ----> " + session("responseBody").as[String])
      println(session)
      session
    })

  setUp(transferMoney.inject(atOnceUsers(1)).protocols(httpProtocol))

}
