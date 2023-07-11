package contracts.change_password

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should return 400 if old password is invalid")

    request {
        method PUT()
        url "/account/settings/change-password"
        headers {
            contentType "application/json"
            authorization "mikuiloveyoumorethananyoneelse"
        }
        body(
                [
                        "old_password": "invalidoldpassword",
                        "new_password": "new_password123"
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
                                "code"             : "old_password_mismatch",
                                "description"      : "Wrong old password was provided",
                                "possible_solution": "Use correct old password"
                        ]
                ]
        )
    }
}