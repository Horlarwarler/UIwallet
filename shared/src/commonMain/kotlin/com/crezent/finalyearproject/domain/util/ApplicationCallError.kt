package com.crezent.finalyearproject.domain.util

interface ApplicationCallError : Error {

    data object DataNotVerified : ApplicationCallError


}