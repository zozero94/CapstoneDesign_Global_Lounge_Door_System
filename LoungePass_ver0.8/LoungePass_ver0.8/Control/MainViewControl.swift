//
//  MainViewControl.swift
//  LoungePass_ver0.8
//
//  Created by MacBook on 25/05/2019.
//  Copyright © 2019 LimSoYul. All rights reserved.
//

import Foundation

class MainViewControll:ServerConstant{
    
    
    private let socket = ServerConnect.sharedInstance
    private let studentInfo = StudentInfo.sharedInstance
    private let dataConverter = ConvertData()
    private let bypass = SejongConnect()
    private let defaultInfo = UserDefaultSetting()
    private let encryption = RSAEncryption(keySize: 512, privateTag: "makeKeyPair", publicTag: "makeKeyPair")
    
    private var auto = false
    
    func initBrightness(brightness: Float) {
        defaultInfo.setBrightness(brightness: brightness)
    }
    
    func autoLoginClicked(isAuto : Bool) {
        self.auto = isAuto
    }
    
    func loginClicked(user :UserInfo) ->Int8 {
        
        var pass :Int8
        
        if user.tag == "1"{ pass = sejongPass(user: user)}
        else {pass = serverPass(user: user)}
        if pass == 7 {setAutoLoginInfo(user: user)}
        return pass
    }
    
    func sejongPass(user :UserInfo) -> Int8 {
        var response : String?
        
        if(bypass.connectedToNetwork){
            response = bypass.requestPass(id: user.id!, pw: user.pw!)
            if response == nil {return 0}
            else if response!.contains("아이디") || response!.contains("ID") { return 1}
            else if response!.contains("패스워드") {return 2}
            else {return self.serverPass(user: user)}
            
        }else{ return 3 }
    }
    func serverPass(user :UserInfo) -> Int8 {
        
        if socket.connecting(){
            _ = socket.sendData(string: dataConverter.getLoginData(seq: LOGIN, id: user.id!, exponent: encryption.getExponent()!, modulus: encryption.getHex()!, type: user.tag!))
            var response : String?
            while response == nil { response = socket.readResponse()}
            let dic = dataConverter.jsonStringToDictionary(text: response!)
            
            // setting student info
            let seq = dic!["seqType"] as! String
            if seq == LOGIN_OK{
                let info = encryption.decpryptBase64(encrpted:dic!["data"] as! String)
                let plainTxt = dataConverter.jsonStringToDictionary(text: info!)
                studentInfo.setStudentInfo(dic: plainTxt!)
                return 7
            }
            else if seq == LOGIN_ALREADY{ return 4}
            else if seq == LOGIN_NO_DATA { return 6}
        }
        
        return 5
    }
    
    func setNewIP(newIP:String)  {
        socket.setIP(newIP: newIP)
    }
    
    func setAutoLoginInfo(user : UserInfo) {
        if auto {
            defaultInfo.setIsAuto(value: auto)
            defaultInfo.setUserInfo(user: user)
        }else{
            defaultInfo.removeAll()
        }
    }
    
    // 앱 활성화시 자동로그인 정보와 자동로그인 true
    //userdefault정보로 로그인 시도.
    //우회로그인 -> 서버로그인 인증시 true 아니면 false
    func isAutoLogin() -> Bool {
        let user = UserInfo()
        if defaultInfo.getInfo(key: "auto") as? Bool == true {
            user.id = defaultInfo.getInfo(key: "id") as? String
            user.pw = defaultInfo.getInfo(key: "pwd") as? String
            user.tag = defaultInfo.getInfo(key: "tag") as? String
            self.auto = true
            if loginClicked(user: user) == 7 {return true}
        }
        return false
    }
    
}
