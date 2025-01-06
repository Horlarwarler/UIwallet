package com.crezent.finalyearproject.models

data class PubicKeyWithDecrypted <T>(
    val  data : T,
   // val ecPublicKey :String,
    val rsaPublicKey : String
)
