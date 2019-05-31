//
//  ConvertData.swift
//  LoungePass_ver0.8
//
//  Created by MacBook on 25/05/2019.
//  Copyright © 2019 LimSoYul. All rights reserved.
//

import Foundation

class ConvertData{
    
    
    func dictionaryToJsonString(dic:[String:Any]) -> String? {
        
        do {
            let jsonData = try JSONSerialization.data(withJSONObject: dic)
            return String(bytes: jsonData, encoding: String.Encoding.utf8)
        } catch {
            print(error.localizedDescription)
        }
        
        return nil
    }
    
    func jsonStringToDictionary(text: String) -> [String: Any]? {
        
        if let data = text.data(using: .utf8) {
            do {
                return try JSONSerialization.jsonObject(with: data, options: []) as? [String: Any]
            } catch {
                print(error.localizedDescription)
            }
        }
        return [:]
    }
    
    //server통신 seqence
    func getLoginData(seq:String,id:String,exponent:String,modulus:String,type:String) -> String {
        
        let data:Dictionary = ["id":id,"exponent":exponent,"modulus":modulus,"type":type]
        let stringData :String? = dictionaryToJsonString(dic: data)
        let loginData : Dictionary = ["seqType":seq ,"data":stringData!]
        let stringLoginData : String? = dictionaryToJsonString(dic: loginData)
        
        return stringLoginData!+"\n"
        
    }
    
    func getSeqData(seq:String) -> String {
        
        let seqData :Dictionary = ["seqType": seq]
        let stringSeqData:String? = dictionaryToJsonString(dic: seqData)
        return stringSeqData!+"\n"
        
    }
    
    
    
}
