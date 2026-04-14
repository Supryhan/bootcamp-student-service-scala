package studentservice.domain

import io.circe.Codec

final case class UpdateStudentRequest(
    firstName: String,
    lastName: String,
    email: String
) derives Codec.AsObject
