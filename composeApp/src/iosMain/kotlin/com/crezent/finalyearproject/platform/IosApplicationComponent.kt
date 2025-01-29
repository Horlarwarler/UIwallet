package com.crezent.finalyearproject.platform

import com.crezent.finalyearproject.core.data.security.encryption.CryptographicOperation
import com.crezent.finalyearproject.core.data.security.encryption.KeyPairGenerator

class IosApplicationComponent(
    val cryptographicOperation: CryptographicOperation,
    val keyPairGenerator: KeyPairGenerator
)
