package contracts.change_password

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should return 400 if new password not pass pattern")

    request {
        method PUT()
        url "/account/settings/change-password"
        headers {
            contentType "application/json"
            authorization "mikuiloveyoumorethananyoneelse"
        }
        body(
                [
                        "old_password": "old_password123",
                        "new_password": "invalid"
                ]
        )
    }

    response {
        status BAD_REQUEST()
        headers {
            contentType "application/json"
        }
        body(
                [
                        "updated"      : false,
                        "error_details": [
                                "code"             : "invalid_password",
                                "description"      : "The password is invalid, password must contain at least 8 characters and 1 number",
                                "possible_solution": "To fix the problem - input the correct password with required format"
                        ]
                ]
        )
    }
}