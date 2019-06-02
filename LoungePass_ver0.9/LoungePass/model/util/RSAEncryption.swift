//
//  RSAEncryption.swift
//  LoungePass
//
//  Created by MacBook on 01/06/2019.
//  Copyright © 2019 LimSoYul. All rights reserved.
//

import Foundation

class RSAEncryption{
    
    private var publicKey : SecKey?
    private var privateKey : SecKey?
    private var exponent:String?
    private var hex :String?
    
    init(keySize: UInt, privateTag: String, publicTag: String) {
        if generateKeyPair(keySize: keySize, privateTag: publicTag, publicTag: publicTag) {
            setHex()
            setExponent()
        }
    }
    
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
    
    
    func encryptBase64(text: String) -> String {
        let plainBuffer = [UInt8](text.utf8)
        var cipherBufferSize : Int = Int(SecKeyGetBlockSize((self.publicKey)!))
        var cipherBuffer = [UInt8](repeating:0, count:Int(cipherBufferSize))
        
        let status = SecKeyEncrypt((self.publicKey)!, SecPadding.PKCS1, plainBuffer, plainBuffer.count, &cipherBuffer, &cipherBufferSize)
        if (status != errSecSuccess) {
            print("Failed Encryption")
        }
        
        let mudata = NSData(bytes: &cipherBuffer, length: cipherBufferSize)
        print("buffer")
        print(cipherBuffer)
        print(cipherBufferSize)
        return mudata.base64EncodedString(options: NSData.Base64EncodingOptions.lineLength64Characters)
    }
    
    
    func decpryptBase64(encrpted: String) -> String? {
        
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
    
    func getHex() ->String?{
        return self.hex
    }
    
    func getExponent()->String? {
        return self.exponent
    }
    
    func setHex(){
        let temp : String?  = self.publicKey.debugDescription
        let startIdx = temp!.index(temp!.startIndex, offsetBy: 144)
        let endIdx = temp!.index(temp!.startIndex, offsetBy: 272) //116
        let range = startIdx..<endIdx
        
        self.hex = temp!.substring(with: range)
    }
    
    func setExponent() {
        
        let temp :String  = self.publicKey.debugDescription
        let startIdx = temp.index(temp.startIndex, offsetBy: 111) //111
        let endIdx = temp.index(temp.startIndex, offsetBy: 116)
        let range = startIdx..<endIdx
        
        self.exponent = temp.substring(with: range)
    }
    
}
