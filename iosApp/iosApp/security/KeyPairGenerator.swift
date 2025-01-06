//
//  KeyPairGenerator.swift
//  iosApp
//
//  Created by Mikail Ramadan on 03/01/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation


@objc public class KeyPairGenerator : NSObject{
    let tag = "com.crezent.uiwallet.keys".data(using: .utf8)!

   @objc public func generateKeyPair() throws {
        
       if getPrivateKey() != nil {

           return
       }
       
       //TAB
       
        
        let attributes: [String: Any] =
            [kSecAttrKeyType as String:            kSecAttrKeyTypeEC,
             kSecAttrKeySizeInBits as String:      256,
             kSecAttrCanEncrypt as String: true,
             kSecAttrCanDecrypt as String:true,
             kSecAttrCanVerify as String: true,
             kSecAttrCanSign as String: true,
             kSecPrivateKeyAttrs as String:
                [kSecAttrIsPermanent as String:    true,
                 kSecAttrApplicationTag as String: tag],
           
        ]
        
        var error: Unmanaged<CFError>?
        guard let privateKey = SecKeyCreateRandomKey(attributes as CFDictionary, &error) else {
            throw error!.takeRetainedValue() as Error
        }
       

    }
    
    @objc public func getPublicKeyString() -> String? {
        guard let publicKey = getPublicKey() else {
            print("No public key")
            return nil
        }
        
        var error:Unmanaged<CFError>?
        guard let cfdata = SecKeyCopyExternalRepresentation(publicKey, &error) else{
            return nil
        }
      //  let data:Data = cfdata as Data
        // Add X.509 Header
        let rawPublicKeyBytes = cfdata as Data
        let x509Header: [UInt8] = [
               0x30, 0x59,               // SEQUENCE
               0x30, 0x13,               // SEQUENCE
               0x06, 0x07, 0x2A, 0x86, 0x48, 0xCE, 0x3D, 0x02, 0x01, // OID: 1.2.840.10045.2.1 (ecPublicKey)
               0x06, 0x08, 0x2A, 0x86, 0x48, 0xCE, 0x3D, 0x03, 0x01, 0x07, // OID: 1.2.840.10045.3.1.7 (P-256)
               0x03, 0x42, 0x00          // BIT STRING
           ]
           var x509KeyData = Data(x509Header)
           x509KeyData.append(rawPublicKeyBytes)

           return x509KeyData.base64EncodedString()
    }
    
     func getPublicKey() -> SecKey? {
        
        guard let privateKey = getPrivateKey() else{
            return nil
        }
        
    
        return SecKeyCopyPublicKey(privateKey)
        
    }
    
     func getPrivateKey()  -> SecKey?{
        let query: [String: Any] = [kSecClass as String: kSecClassKey,
                                       kSecAttrApplicationTag as String: tag,
                                       kSecAttrKeyType as String: kSecAttrKeyTypeEC,
                                       kSecReturnRef as String: true]
        var item: CFTypeRef?
        let status = SecItemCopyMatching(query as CFDictionary, &item)
        guard status == errSecSuccess else {
            return nil
        }
        let key = item as! SecKey
        return key
    }
    
    
    
}
