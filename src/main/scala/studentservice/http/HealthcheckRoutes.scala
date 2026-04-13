package studentservice.http

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.io.*
import org.typelevel.log4cats.Logger
import studentservice.config.StudentServiceConfig

object HealthcheckRoutes:
  def routes(config: StudentServiceConfig)(using logger: Logger[IO]): HttpRoutes[IO] =
    HttpRoutes.of[IO]:
      case GET -> Root / "healthcheck" =>
        logger.info(s"Received GET /healthcheck request for version ${config.version}") *>
          Ok(config.version)
