package contracts.current_account

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should return account info if valid access token is used")

    request {
        method("GET")
        url("/account/me")
        headers {
            contentType "application/json"
            authorization "mikuiloveyoumorethananyoneelse"
        }
    }
    response {
        headers {
            contentType "application/json"
        }
        status OK()
        body(
                [
                        "id"       : "mikiismylove",
                        "username" : "mikiismylove",
                        "email"    : "odeyalo@gmail.com",
                        "birthdate": "2000-12-03",
                        "country"  : "JP",
                        "gender"   : "MALE"
                ]
        )
    }
}
