package studentservice.config

import cats.effect.IO
import pureconfig.ConfigReader
import pureconfig.ConfigSource

final case class HttpServerConfig(host: String = "0.0.0.0", port: Int = 8080) derives ConfigReader

final case class StudentServiceConfig(
    version: String,
    http: HttpServerConfig = HttpServerConfig()
) derives ConfigReader

object StudentServiceConfig:
  def load: IO[StudentServiceConfig] =
    IO.fromEither(
      ConfigSource.default
        .at("student-service")
        .load[StudentServiceConfig]
        .left
        .map(failures => IllegalStateException(failures.prettyPrint()))
    )
