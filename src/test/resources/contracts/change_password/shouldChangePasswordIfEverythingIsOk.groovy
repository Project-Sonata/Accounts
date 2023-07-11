package contracts.change_password

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should change password and return 200 OK")

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
                        "new_password": "new_password123"
                ]
        )
    }

    response {
        status OK()
        headers {
            contentType "application/json"
        }
        body(
                [
                        "updated": true
                ]
        )
    }
}