package studentservice.domain

import io.circe.Codec

final case class CreateStudentRequest(
    firstName: String,
    lastName: String,
    email: String
) derives Codec.AsObject
