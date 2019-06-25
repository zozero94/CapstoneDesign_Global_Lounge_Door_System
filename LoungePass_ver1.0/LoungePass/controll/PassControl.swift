//
//  Pass.swift
//  LoungePass
//
//  Created by MacBook on 01/06/2019.
//  Copyright © 2019 LimSoYul. All rights reserved.
//

import Foundation

class PassControl:ServerConstant{
    
    private let dataConverter = ConvertData()
    private let imageGenerator = ImageGenerator()
    private let socket = ServerConnect.sharedInstance
    let user = User.sharedInstance
    
    func logoutClicked() {
        
        UserDefaults.standard.set(false, forKey: "auto")
        UserDefaults.standard.synchronize()
        
        user.resetInfo()
        socket.closing()
        
    }
    func requestData(key :String)->Bool {
        if key == "qr"{
            return socket.sendData(string: dataConverter.getSeqData(seq: STATE_REQ))
        }else if key == "img" {
            return socket.sendData(string: dataConverter.getSeqData(seq: STATE_IMG))
        }else if key == "beacon"{
            return socket.sendData(string: dataConverter.getSeqData(seq: STATE_ADMIN))
        }else if key == "expiration" {
            return socket.sendData(string: dataConverter.getSeqData(seq: STATE_EXP))
        }
        return false
    }
    
    func isResponse() ->[String :Any]?{
        
        let response : String? = socket.readResponse()
        if response == nil { return nil}
        
        var dic = dataConverter.jsonStringToDictionary(text: response!)
        
        switch dic["seqType"] as! String {
        case STATE_CREATE : dic["qr"] = imageGenerator.getQRCode(from: dic["qr"] as! String)
        case STATE_URL : dic["img"] = imageGenerator.getUrlImage(urlString: dic["img"] as! String)
        default: break
        }
        return dic
    }
    
}

