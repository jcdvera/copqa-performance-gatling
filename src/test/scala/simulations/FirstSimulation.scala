package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by Juan Camilo Dvera on 3/2/21
  */
class FirstSimulation extends Simulation{

  val baseURL = "http://localhost:8081"

  val httpProtocol = http.baseUrl(baseURL)

  val getToken = scenario("first scenario")
      .exec(http("Get Access Token")
        .post("/auth")
        .header("Content-Type", "application/x-www-form-urlencoded")
        .header("Authorization", "Basic anVhbjpWYWxpZFBhc3MxMjM=")
        .check(status.is(200))
      )

  setUp(getToken.inject(atOnceUsers(1))).protocols(httpProtocol)

}
