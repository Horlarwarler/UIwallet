package com.crezent.finalyearproject

const val SERVER_PORT = 8080

//const val TRANSFORMATION = "ECIES/ECB/PKCS1Padding"
//const val TRANSFORMATION = "ECDSA"

const val ANDROID_TRANSFORMATION = "EC"
const val TRANSFORMATION = "RSA/ECB/OAEPPadding"
const val AES_TRANSFORMATION = "AES/GCM/NoPadding"


const val RSA_ALIAS = "apiKeys"
const val EC_ALIAS = "apiSignature"
const val ANDROID_KEY_STORE = "AndroidKeyStore"

const val SIGNATURE_ALGORITHM = "SHA256withECDSA"

const val ELLIPTIC_CURVE_STANDARD_NAME = "secp256r1"
