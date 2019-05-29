//
//  RSAWrapper.swift
//  Lounge
//
//  Created by An on 14/05/2019.
//  Copyright © 2019 An. All rights reserved.
//

import Foundation

class RSAWrapper {
    private var publicKey : SecKey?
    private var privateKey : SecKey?
    
    init() {}
    
    func generateKeyPair(keySize: UInt, privateTag: String, publicTag: String) -> Bool {
        
        self.publicKey = nil
        self.privateKey = nil
        
        if (keySize != 512 && keySize != 1024 && keySize != 2048) {
            // Failed
            print("Key size is wrong")
            return false
        }
        
        let publicKeyParameters: [NSString: AnyObject] = [
            kSecAttrIsPermanent: true as AnyObject,
            kSecAttrApplicationTag: publicTag as AnyObject
        ]
        let privateKeyParameters: [NSString: AnyObject] = [
            kSecAttrIsPermanent: true as AnyObject,
            kSecAttrApplicationTag: publicTag as AnyObject
        ]
        let parameters: [String: AnyObject] = [
            kSecAttrKeyType as String: kSecAttrKeyTypeRSA,
            kSecAttrKeySizeInBits as String: keySize as AnyObject,
            kSecPrivateKeyAttrs as String: privateKeyParameters as AnyObject,
            kSecPublicKeyAttrs as String: publicKeyParameters as AnyObject
        ];
        
        let status : OSStatus = SecKeyGeneratePair(parameters as CFDictionary, &(self.publicKey), &(self.privateKey))
        
        return (status == errSecSuccess && self.publicKey != nil && self.privateKey != nil)
    }
    
    func encrypt(text: String) -> [UInt8] {
        let plainBuffer = [UInt8](text.utf8)
        var cipherBufferSize : Int = Int(SecKeyGetBlockSize((self.publicKey)!))
        var cipherBuffer = [UInt8](repeating:0, count:Int(cipherBufferSize))
        
        // Encrypto  should less than key length
        let status = SecKeyEncrypt((self.publicKey)!, SecPadding.PKCS1, plainBuffer, plainBuffer.count, &cipherBuffer, &cipherBufferSize)
        if (status != errSecSuccess) {
            print("Failed Encryption")
        }
        return cipherBuffer
    }
    
    func decprypt(encrpted: [UInt8]) -> String? {
        var plaintextBufferSize = Int(SecKeyGetBlockSize((self.privateKey)!))
        var plaintextBuffer = [UInt8](repeating:0, count:Int(plaintextBufferSize))
        
        let status = SecKeyDecrypt((self.privateKey)!, SecPadding.PKCS1, encrpted, plaintextBufferSize, &plaintextBuffer, &plaintextBufferSize)
        
        if (status != errSecSuccess) {
            print("Failed Decrypt")
            return nil
        }
        return NSString(bytes: &plaintextBuffer, length: plaintextBufferSize, encoding: String.Encoding.utf8.rawValue)! as String
    }
    
    
    func encryptBase64(text: String) -> String {
        let plainBuffer = [UInt8](text.utf8)
        var cipherBufferSize : Int = Int(SecKeyGetBlockSize((self.publicKey)!))
        var cipherBuffer = [UInt8](repeating:0, count:Int(cipherBufferSize))
        
        // Encrypto  should less than key length
        let status = SecKeyEncrypt((self.publicKey)!, SecPadding.PKCS1, plainBuffer, plainBuffer.count, &cipherBuffer, &cipherBufferSize)
        if (status != errSecSuccess) {
            print("Failed Encryption")
        }
        
        let mudata = NSData(bytes: &cipherBuffer, length: cipherBufferSize)
        
        return mudata.base64EncodedString(options: NSData.Base64EncodingOptions.lineLength64Characters)
    }
    
    
    
    func decryptBase64(encrpted: String) -> String? {
        
        let data : NSData = NSData(base64Encoded: encrpted, options: .ignoreUnknownCharacters)!
        let count = data.length / MemoryLayout<UInt8>.size
        
        var array = [UInt8](repeating: 0, count : count)
        data.getBytes(&array, length: count * MemoryLayout<UInt8>.size)
        
        var test = [UInt8]()
        for i in 1...count/64{
            var plaintextBufferSize = Int(SecKeyGetBlockSize((self.privateKey)!))
            var plaintextBuffer = [UInt8](repeating:0, count:Int(plaintextBufferSize))
            var temp = [UInt8]()
            for j in 0...63 {
                temp.append(array[(i-1)*64+j])
            }
            
            let status = SecKeyDecrypt((self.privateKey)!, SecPadding.PKCS1, temp, plaintextBufferSize, &plaintextBuffer, &plaintextBufferSize)
            if (status != errSecSuccess) {
                print("Failed Decrypt")
                return nil
            }
            for j in 0...plaintextBufferSize-1{
                test.append(plaintextBuffer[j])
            }
        }
        
        return NSString(bytes: &test, length: test.count, encoding: String.Encoding.utf8.rawValue)! as String
    }
    
    func getPublicKey() -> SecKey? {
        return self.publicKey
    }
    
    func getPrivateKey() -> SecKey? {
        return self.privateKey
    }
}
