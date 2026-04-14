package studentservice.domain

import io.circe.{Decoder, Encoder}

import java.util.UUID

final case class StudentId(value: UUID) extends AnyVal

object StudentId:
  given Encoder[StudentId] = Encoder[UUID].contramap(_.value)
  given Decoder[StudentId] = Decoder[UUID].map(StudentId.apply)
