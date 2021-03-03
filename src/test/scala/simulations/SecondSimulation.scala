package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by Juan Camilo Dvera on 3/2/21
  */
class SecondSimulation extends Simulation{

  val baseURL = "http://localhost:8081"

  val httpProtocol = http.baseUrl(baseURL)

  val getAccountDetails = scenario("second scenario")
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
    )

  setUp(getAccountDetails.inject(atOnceUsers(1)).protocols(httpProtocol))

}
