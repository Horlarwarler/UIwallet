package com.crezent.finalyearproject

const val SERVER_PORT = 8080

//const val TRANSFORMATION = "ECIES/ECB/PKCS1Padding"
//const val TRANSFORMATION = "ECDSA"

const val ANDROID_TRANSFORMATION = "EC"

//const val RSA_TRANSFORMATION = "RSA/ECB/OAEPPadding"
const val RSA_TRANSFORMATION = "RSA/ECB/OAEPWithSHA-1AndMGF1Padding"

const val AES_TRANSFORMATION = "AES/GCM/NoPadding"
const val CARD_AES_TRANSFORMATION = "AES/CBC/PKCS5Padding"
const val PAYSTACK_PUBLIC_KEY = "pk_test_370e8d626fed1819c3a137e0476d6dbaeb971fb7"


//


const val RSA_ALIAS = "apiKeys"
const val EC_ALIAS = "apiSignature"
const val ANDROID_KEY_STORE = "AndroidKeyStore"

const val SIGNATURE_ALGORITHM = "SHA256withECDSA"

const val ELLIPTIC_CURVE_STANDARD_NAME = "secp256r1"

const val VERIFY_EMAIL_PURPOSE = "Verify Email"
const val RESET_PASSWORD_PURPOSE = "Reset Password"
