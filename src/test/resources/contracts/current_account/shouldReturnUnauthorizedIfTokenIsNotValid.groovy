package contracts.account

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should return 401 unauthorized status if access token is invalid")

    request {
        method("GET")
        url("/account/me")
        headers {
            contentType "application/json"
            authorization "invalidtoken"
        }
    }
    response {
        headers {
            contentType "application/json"
        }
        status UNAUTHORIZED()
    }
}
