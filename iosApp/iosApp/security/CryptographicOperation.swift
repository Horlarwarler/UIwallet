//
//  CryptographicOperation.swift
//  iosApp
//
//  Created by Mikail Ramadan on 03/01/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation

@objc public class CryptographicOperation :NSObject{

    
    // Encyption (server public key)
    private var publicKey : SecKey?
    
    private var publicKeyString :String?
    
    private var clientKeyPair :SecKey?
    public override init() {
        clientKeyPair = KeyPairGenerator().getPrivateKey()
    }
    
    @objc public func encryptData(data:String, publicKeyString:String) throws ->String {
        print("Received key length: \(publicKeyString.count)")
        
        if  publicKey == nil || publicKeyString != self.publicKeyString {
            print("Recalcuting the public key")
            
            
            let data = NSData(base64Encoded: publicKeyString,
               options:NSData.Base64DecodingOptions.ignoreUnknownCharacters)!
            
          //  print("Key Data \(keyData)  ns data \(nsData) publick key is \(publicKeyString)")
            let options: [String: Any] = [
                        kSecAttrKeyType as String: kSecAttrKeyTypeEC,
                        kSecAttrKeyClass as String: kSecAttrKeyClassPublic,
            ]
                    
            print("Decoded key length: \(data.count)")

            var error: Unmanaged<CFError>?
        
            
            guard let secPublicKey = SecKeyCreateWithData(data as CFData, options as CFDictionary, &error) else {
                if let error = error?.takeRetainedValue() {
                                print("Key creation error: \(error.localizedDescription)")
                                // Print the underlying error code
                                let code = CFErrorGetCode(error)
                                let domain = CFErrorGetDomain(error) as String
                                print("Error code: \(code), domain: \(domain)")
                            }
                throw NSError(domain: "Invalid public key conversion", code: -2, userInfo: nil)
            }
           publicKey = secPublicKey
        }
        
        guard let dataToEncrypt = data.data(using: .utf8) else {
            throw NSError(domain: "Invalid input data", code: -3, userInfo: nil)
        }
        if !verifyPublicKey(publicKey:publicKey!) {
            throw NSError(domain: "Key not suitable for encryption", code: -4, userInfo: nil)
        }
        
        var error: Unmanaged<CFError>?
        guard let encryptedData = SecKeyCreateEncryptedData(
                   publicKey!,
                   SecKeyAlgorithm.eciesEncryptionCofactorX963SHA256AESGCM,
                   dataToEncrypt as CFData,
                   &error
               ) else {
                   throw error!.takeRetainedValue() as Error
               }
               
        return (encryptedData as Data).base64EncodedString()
        
        //throw
     //   let encryptionAlgorithm:SecKeyAlgorithm = .ec
    }
    
    // MARK: - Decryption
       @objc public func decryptData(encryptedData: String) throws -> String {
          
           guard let privateKey = clientKeyPair else {
               throw NSError(domain: "No Private", code: -1)
           }
           
           let algorithm: SecKeyAlgorithm =   SecKeyAlgorithm.eciesEncryptionCofactorX963SHA256AESGCM

           
           guard SecKeyIsAlgorithmSupported(privateKey, .sign, algorithm) else {
               throw  NSError(domain: "No Private", code: -1)
           }
           
          
           guard let encryptedDataBytes = Data(base64Encoded: encryptedData) else {
               throw NSError(domain: "Invalid encrypted data", code: -3, userInfo: nil)
           }
           
           var error: Unmanaged<CFError>?
           guard let decryptedData = SecKeyCreateDecryptedData(
               privateKey,
               SecKeyAlgorithm.eciesEncryptionCofactorX963SHA256AESGCM,
               encryptedDataBytes as CFData,
               &error
           ) else {
               throw error!.takeRetainedValue() as Error
           }
           
           return String(data: decryptedData as Data, encoding: .utf8)!
       }
       
    
    //Decryption (client private key)
    
    
    // Signing (client private key)
    
    @objc public func signData(encryptedData:String)  throws -> String {
        
        guard let privateKey = clientKeyPair else {
            throw NSError(domain: "No Private", code: -1)
        }
        
        let algorithm: SecKeyAlgorithm =   SecKeyAlgorithm.ecdsaSignatureMessageX962SHA256

        
        guard SecKeyIsAlgorithmSupported(privateKey, .sign, algorithm) else {
            throw  NSError(domain: "No Private", code: -1)
        }
        guard let dataToSign = encryptedData.data(using: .utf8) else {
            throw NSError(domain: "Invalid input data", code: -3, userInfo: nil)
        }
        
        var error: Unmanaged<CFError>?
        guard let signature = SecKeyCreateSignature(
                    privateKey,
                    SecKeyAlgorithm.ecdsaSignatureMessageX962SHA256,
                    dataToSign as CFData,
                    &error
                ) else {
                    throw error!.takeRetainedValue() as Error
                }
                
        return (signature as Data).base64EncodedString()
        
    }
    
    
    // Verifying (server public key)
    
    // MARK: - Verifying
    @objc public func verifySignature(data: String, signature: String, publicKey: String) throws ->String {
        guard let keyData = Data(base64Encoded: publicKey) else {
            throw NSError(domain: "Invalid public key", code: -1, userInfo: nil)
        }
        
        let options: [String: Any] = [
            kSecAttrKeyType as String: kSecAttrKeyTypeEC,
            kSecAttrKeyClass as String: kSecAttrKeyClassPublic,
            kSecAttrCanVerify as String: true,
            kSecAttrKeySizeInBits as String: 256
        ]
        
        guard let secPublicKey = SecKeyCreateWithData(keyData as CFData, options as CFDictionary, nil) else {
            throw NSError(domain: "Invalid public key conversion", code: -2, userInfo: nil)
        }
        
        guard let dataToVerify = data.data(using: .utf8),
              let signatureData = Data(base64Encoded: signature) else {
            throw NSError(domain: "Invalid data or signature", code: -3, userInfo: nil)
        }
        
        var error: Unmanaged<CFError>?
        let isValid = SecKeyVerifySignature(
            secPublicKey,
            SecKeyAlgorithm.ecdsaSignatureMessageX962SHA256,
            dataToVerify as CFData,
            signatureData as CFData,
            &error
        )
        
        if let error = error {
            throw error.takeRetainedValue() as Error
        }
        if isValid {return "V"} else {
           return "I"
        }
      //  return isValid
    }
    
    func verifyPublicKey(publicKey: SecKey) -> Bool {
        guard SecKeyIsAlgorithmSupported(publicKey,
                                       .encrypt,
                                       .eciesEncryptionCofactorX963SHA256AESGCM) else {
            print("Algorithm not supported for this key")
            return false
        }
        return true
    }
    
}
