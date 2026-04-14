package studentservice.http

import cats.effect.IO
import munit.CatsEffectSuite
import org.http4s.{Method, Request, Status}
import org.http4s.implicits.uri
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import studentservice.config.StudentServiceConfig

class HealthcheckRoutesSpec extends CatsEffectSuite:
  private given Logger[IO] = Slf4jLogger.getLogger[IO]

  test("GET /healthcheck returns the configured application version"):
    val request = Request[IO](Method.GET, uri"/healthcheck")

    HealthcheckRoutes
      .routes(StudentServiceConfig(version = "test-version"))
      .orNotFound
      .run(request)
      .flatMap: response =>
        response.as[String].map: body =>
          assertEquals(response.status, Status.Ok)
          assertEquals(body, "test-version")
