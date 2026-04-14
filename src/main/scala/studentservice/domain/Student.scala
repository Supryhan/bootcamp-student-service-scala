package studentservice.domain

import io.circe.Codec

final case class Student(
    id: StudentId,
    firstName: String,
    lastName: String,
    email: String
) derives Codec.AsObject
