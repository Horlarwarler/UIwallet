//
//  KeyPairGenerator.swift
//  iosApp
//
//  Created by Mikail Ramadan on 03/01/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import CryptoKit
import composeApp
import ASN1Decoder


//import ASN1Decoder
//import ASN1Decoder

class IosKeyPairGenerator : KeyPairGenerator{
    static let EC_KEY = "apiSignature"
    let tag = "com.crezent.uiwallet.keys".data(using: .utf8)!
    let rsaKeyTag = "com.crezent.uiwallet.keys.rsa".data(using: .utf8)!
    
    func generateKeyStore() {
        
        generateEcKeyPair()
        generateRsaKeyPair()
       
       
    }
    private func generateEcKeyPair(){
        if getEcPrivateKey() != nil {
            return
        }
        let attributes: [String: Any] =
            [kSecAttrKeyType as String:            kSecAttrKeyTypeEC,
             kSecAttrKeySizeInBits as String:      256,
             kSecAttrCanVerify as String: true,
             kSecAttrCanSign as String: true,
             kSecPrivateKeyAttrs as String:
                [kSecAttrIsPermanent as String:    true,
                 kSecAttrApplicationTag as String: tag],
           
        ]
        var error: Unmanaged<CFError>?
        SecKeyCreateRandomKey(attributes as CFDictionary, &error)
        if(error != nil){
            let errorDescription = (error!.takeRetainedValue() as Error).localizedDescription
            print(errorDescription)
       }
        
    }
    func getClientKeyPair(alias: String) -> String {
       
        if alias == Self.EC_KEY {
            print("Will get ec key")
            return getEcPublicKeyString()!
        }
        else {
            print("Will get rsa key")

            return getRsaPublicKeyString()!
        }
    }
  
    


    private func generateRsaKeyPair() {
        
        let existingKey = getRsaPrivateKey()
        if existingKey != nil{
            print("Rsa already generated")
            return
        }
        print("Will generated")

        let attributes: [String: Any] =
            [kSecAttrKeyType as String:            kSecAttrKeyTypeRSA,
             kSecAttrKeySizeInBits as String:      2048,
             kSecAttrCanEncrypt as String: true,
             kSecAttrCanDecrypt as String:true,
            
             kSecPrivateKeyAttrs as String:
                [kSecAttrIsPermanent as String:    true,
                 kSecAttrApplicationTag as String: rsaKeyTag],
           
        ]
        
        var error: Unmanaged<CFError>?
        SecKeyCreateRandomKey(attributes as CFDictionary, &error)
        if(error != nil){
            let errorDescription = (error!.takeRetainedValue() as Error).localizedDescription
            print(errorDescription)
       }
        else{
            print(" generated")

        }
    
       
        
    }
    
 
    
    public func generateAesKey() -> Data {
        let randomString = generateRandomString(length: 16)
        let key = Data(randomString.data(using: .utf8)!.prefix(32))
        return key
        
    }
    
    func generateRandomString(length:Int) -> String {
        let letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return String((0..<length).map{_ in letters.randomElement()!})
    }
    
    
    func decodePublicKey( 
        data: Data,
        error: UnsafeMutablePointer<Unmanaged<CFError>?>?,
        type: CFString
    ) -> SecKey? {
        
       
            guard let asn1 = try? ASN1DERDecoder.decode(data: data) else {
            
                print("Can't decode string to asn1")
                return nil
            }
            guard let keyData = asn1.first?.sub(1)?.value as? Data else {
                
                print("Key data can't be null")
                return nil
            }
            return SecKeyCreateWithData(keyData as CFData,
                [
                    kSecAttrKeyType as String: type,
                    kSecAttrKeyClass as String: kSecAttrKeyClassPublic,
                   // kSecAttrKeySizeInBits as String: 2048
                    
                ] as CFDictionary,
                error
            )
    }
    
    public func getEcPublicKeyString() -> String? {
        print("Will get EC public key")
        guard let publicKey = getEcPublicKey() else {
                print("No public key")
                return nil
        }
        
        
        var error:Unmanaged<CFError>?
        guard let cfData = SecKeyCopyExternalRepresentation(publicKey, &error) else{
            
            return nil
        }

        let rawPublicKeyBytes = cfData as Data
        
        print("Raw EC Key Bytes: \(rawPublicKeyBytes.map { String(format: "%02x", $0) }.joined())")


    
        // Encode as X.509 structure
            let x509Header: [UInt8] = [
                0x30, 0x59, // SEQUENCE
                0x30, 0x13, // SEQUENCE (Algorithm Identifier)
                0x06, 0x07, 0x2A, 0x86, 0x48, 0xCE, 0x3D, 0x02, 0x01, // OID for ecPublicKey
                0x06, 0x08, 0x2A, 0x86, 0x48, 0xCE, 0x3D, 0x03, 0x01, 0x07, // OID for P-256 curve
                0x03, 0x42, 0x00 // BIT STRING (raw key length)
            ]

            var x509Data = Data(x509Header)
            x509Data.append(rawPublicKeyBytes) // Append the raw public key bytes
//            print("Raw Public Key Bytes: \(rawPublicKeyBytes.map { String(format: "%02x", $0) }.joined())")
//            print("X.509 Encoded Key: \(x509Data.base64EncodedString())")

            return x509Data.base64EncodedString()

    }
    
    
    public func getRsaPublicKeyString() -> String? {
        guard let publicKey = getRsaPublicKey() else {
            print("No RSA public key")
            return nil
        }
            
        var error: Unmanaged<CFError>?
        //let copyKeyMatch = SecKeyCopyPublicKey(publicKey)
        
        guard let rawPublicKey = SecKeyCopyExternalRepresentation(publicKey, &error) else {
            print("Failed to export public key: \(String(describing: error?.takeRetainedValue()))")
            return nil
        }

        let rawKeyData = rawPublicKey as Data
        
         let encodedString =  RSAKeyEncoding().asBase64( data: rawKeyData)
        return encodedString

    }
    
    
    private  func getEcPublicKey() -> SecKey? {
        
        guard let privateKey = getEcPrivateKey() else{
            return nil
        }
        
        return SecKeyCopyPublicKey(privateKey)
        
    }
    private  func getRsaPublicKey () -> SecKey? {
        guard let privateKey = getRsaPrivateKey() else{
            print("No Rsa already generated")

            return nil
        }
        
        return SecKeyCopyPublicKey(privateKey)
    }
    
     func getEcPrivateKey()  -> SecKey?{
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
    func getRsaPrivateKey() -> SecKey? {
        let query :[String: Any] = [
            kSecAttrType as String : kSecAttrKeyTypeRSA,
            kSecClass as String : kSecClassKey,
            kSecAttrApplicationTag as String : rsaKeyTag,
            kSecReturnRef as String : true
        ]
        
        var rsaRef:CFTypeRef?
        
        let status = SecItemCopyMatching(query as CFDictionary, &rsaRef)
        guard status == errSecSuccess else  {
           return nil
        }
        let key  = rsaRef as! SecKey
        return key
    }
    
    
    
    
}

