package studentservice

import cats.effect.{IO, IOApp}
import cats.syntax.semigroupk.*
import com.comcast.ip4s.*
import org.http4s.server.middleware.Logger
import org.http4s.ember.server.EmberServerBuilder
import org.typelevel.log4cats.Logger as CatsLogger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import studentservice.config.StudentServiceConfig
import studentservice.http.{HealthcheckRoutes, StudentRoutes}
import studentservice.repository.InMemoryStudentRepository
import studentservice.service.DefaultStudentService

object StudentServiceApp extends IOApp.Simple:
  private given CatsLogger[IO] = Slf4jLogger.getLogger[IO]

  override val run: IO[Unit] =
    for
      config <- StudentServiceConfig.load
      host <- IO.fromOption(Host.fromString(config.http.host))(
        IllegalArgumentException(s"Invalid server host: ${config.http.host}")
      )
      port <- IO.fromOption(Port.fromInt(config.http.port))(
        IllegalArgumentException(s"Invalid server port: ${config.http.port}")
      )
      studentRepository <- InMemoryStudentRepository.create
      studentService = DefaultStudentService(studentRepository)
      routes = HealthcheckRoutes.routes(config) <+> StudentRoutes.routes(studentService)
      httpApp = Logger.httpApp(logHeaders = true, logBody = false)(
        routes.orNotFound
      )
      _ <- EmberServerBuilder
        .default[IO]
        .withHost(host)
        .withPort(port)
        .withHttpApp(httpApp)
        .build
        .use(_ => CatsLogger[IO].info(s"StudentService is running on http://$host:$port") *> IO.never)
    yield ()
