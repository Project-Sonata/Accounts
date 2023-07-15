package contracts.change_password

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should return 401 on access token missing")

    request {
        method PUT()
        url "/account/settings/change-password"
        headers {
            contentType "application/json"
        }
        body(
                [
                        "old_password": "old_password123",
                        "new_password": "new_password123"
                ]
        )
    }

    response {
        status UNAUTHORIZED()
        headers {
            contentType "application/json"
        }
    }
}