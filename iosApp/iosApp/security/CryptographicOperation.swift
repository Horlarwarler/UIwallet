//
//  CryptographicOperation.swift
//  iosApp
//
//  Created by Mikail Ramadan on 03/01/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import CryptoKit
import composeApp
//import CommonCrypto
 class IosCryptographicOperation :CryptographicOperation{

    // Encyption (server public key)
    private var publicKey : SecKey?
    
    private var publicKeyString :String?
    
    private var clientEcKeyPair :SecKey?
    private var clientRsaKeyPair: SecKey?
     init() {
         clientEcKeyPair = IosKeyPairGenerator().getEcPrivateKey()
         clientRsaKeyPair = IosKeyPairGenerator().getRsaPrivateKey()
     }
  
    //Mark Encryption
     func encryptData(serverPublicKey: String, data: String) -> SharedEncryptionKeyValue? {
         // Step 1: Generate AES Key
         let randomString = IosKeyPairGenerator().generateRandomString(length: 16) // this will serve as our secret key
         let aesKeyData = Data(randomString.data(using: .utf8)!)
         let aesKey = SymmetricKey(data: aesKeyData)
         
         //Step 2; Cipher data with AES key
         let dataToEncrypt = data.data(using: .utf8)!
         let aesCipher = try! AES.GCM.seal(dataToEncrypt, using: aesKey)
         let tag = aesCipher.tag
         
      ///   AES.GCM.seal(utf8Data, using: aesKey, authenticating: .)
         let encryptedData = aesCipher.ciphertext + tag
         let encryptedDataBase64 = encryptedData.base64EncodedString()
         let iv = aesCipher.nonce.withUnsafeBytes { Data($0).base64EncodedString() }
         let aesEncryptedString = "\(encryptedDataBase64):\(iv)"

         // Step 3: Code the AES key wht Rsa Public key
         var unManageError: Unmanaged<CFError>?
         let publicKeyData = Data(base64Encoded: serverPublicKey, options: .ignoreUnknownCharacters)!
         guard let secPublicKey = IosKeyPairGenerator().decodePublicKey(data: publicKeyData, error:  &unManageError, type: kSecAttrKeyTypeRSA ) else{
             print("Unable to generate RSA key")
             return nil
         }
         var error: Unmanaged<CFError>?

         guard let rsaEncryptedKey = SecKeyCreateEncryptedData(
               secPublicKey,
                SecKeyAlgorithm.rsaEncryptionOAEPSHA1,
                aesKeyData as CFData,
                &error
           ) else {
             print("Error Occurs \(error.debugDescription)")
             return nil
           }
         let rsaEncryptedKeyString = (rsaEncryptedKey as Data).base64EncodedString()
         return SharedEncryptionKeyValue(aesEncryptedString: aesEncryptedString, rsaEncryptedKey: rsaEncryptedKeyString)
         
     }
     
     
     func decryptData(encryptedData: String, encryptedAesKey: String) -> String? {
         guard let privateKey = clientRsaKeyPair else {
             return  nil
         }
         
         //Generate the encrypted Secret Key
         
         let rsaAlgorithm: SecKeyAlgorithm =   SecKeyAlgorithm.rsaEncryptionOAEPSHA1
         
         
         guard SecKeyIsAlgorithmSupported(privateKey, .decrypt, rsaAlgorithm) else {
             return nil
         }
         
         guard let encryptedAesKey = Data(base64Encoded: encryptedAesKey) else {
             
            print("Decode Aes key went wrong")
            return nil
         }
         
         var error: Unmanaged<CFError>?
         guard let decryptedAesKey = SecKeyCreateDecryptedData(
            privateKey,
            rsaAlgorithm,
            encryptedAesKey as CFData,
            &error
         ) else {
             print(error.debugDescription)
             return nil
         }
        
         
         let aesKey = SymmetricKey(data: decryptedAesKey as Data)
         
         // Split the data
         
         
         let splittedData =  encryptedData.split(separator: ":")
         let cipherTextPart = splittedData[0]
         let ivPart = splittedData[1]
         

         guard let ciphertextData = Data(base64Encoded: String(cipherTextPart), options: .ignoreUnknownCharacters),
               let ivData = Data(base64Encoded: String(ivPart), options: .ignoreUnknownCharacters) else {
             print("Invalid Base64 encoding for ciphertext, tag, or IV")
             return nil
         }

         do {
             let nonce = try AES.GCM.Nonce(data: ivData)
             let cipherText = ciphertextData.prefix(ciphertextData.count-16)
             let tagData = ciphertextData.suffix(16)
             print("will create Sealed box")

             let sealedBox = try AES.GCM.SealedBox(nonce: nonce, ciphertext: cipherText, tag: tagData)
             print("Sealed box created")
             // Decrypt the data
             let decryptedData = try AES.GCM.open(sealedBox, using: aesKey)
             let decryptedString = String(data: decryptedData, encoding: .utf8)
             print(decryptedString)
             return decryptedString
         } catch {
             // Detailed error description
             
             print("Error while decrypting: \(error.localizedDescription)")
             return nil
         }
     }
     func signData(dataToSign: String) -> String? {
         let algorithm: SecKeyAlgorithm =   SecKeyAlgorithm.ecdsaSignatureMessageX962SHA256
         
         guard let privateKey = IosKeyPairGenerator().getEcPrivateKey() else {
             print("No private key found")
             return nil
         }
         
         guard SecKeyIsAlgorithmSupported(privateKey, .sign, algorithm) else {
            print("Algorithm not supported")
             return nil
         }
         guard let dataToSign = dataToSign.data(using: .utf8) else {
             print("Invalid data to sign ")
             return nil
         }
         
         var error: Unmanaged<CFError>?
         guard let signature = SecKeyCreateSignature(
                    privateKey,
                     SecKeyAlgorithm.ecdsaSignatureMessageX962SHA256,
                     dataToSign as CFData,
                     &error
                 ) else {
             print("Unable to create signature")
                    return nil
                 }
                 
         return (signature as Data).base64EncodedString()
         
     }
     
     func verifySignature(signature: String, dataToVerify: String, publicKey: String) -> Bool {
         
         var unManageError : Unmanaged<CFError>? = nil
        
         guard let publicKeyData = Data(base64Encoded: publicKey, options: .ignoreUnknownCharacters), let ecPublicKey = IosKeyPairGenerator().decodePublicKey(data: publicKeyData, error: &unManageError, type: kSecAttrKeyTypeEC) else {
             print("Unable to generate EC key")

             return false
         }
         
        
         
         guard let dataToVerify = dataToVerify.data(using: .utf8),
               let signatureData = Data(base64Encoded: signature, options: .ignoreUnknownCharacters) else {
             print("Signature is invalid")
             return false
         }
         
         var error: Unmanaged<CFError>?
         let isValid = SecKeyVerifySignature(
             ecPublicKey,
             SecKeyAlgorithm.ecdsaSignatureMessageX962SHA256,
             dataToVerify as CFData,
             signatureData as CFData,
             &error
         )
         
         if let error = error {
             let errorMessage = (error.takeRetainedValue() as Error).localizedDescription
             print("Unable to verify \(errorMessage)")
           return false
         }
         print("Verification successfull")
         return isValid
         
     }

    private  func secCall<Result>(_ body: (_ resultPtr: UnsafeMutablePointer<Unmanaged<CFError>?>) -> Result?) throws -> Result {
        var errorQ: Unmanaged<CFError>? = nil
        guard let result = body(&errorQ) else {
            throw errorQ!.takeRetainedValue() as Error
        }
        return result
    }
  

    
}
