//
//  MainControl.swift
//  LoungePass
//
//  Created by MacBook on 01/06/2019.
//  Copyright © 2019 LimSoYul. All rights reserved.
//

import Foundation

class MainControl : ServerConstant{
    
    private var autoFlag = false
    private let socket = ServerConnect.sharedInstance
    private let user = User.sharedInstance
    private let dataConverter = ConvertData()
    private let bypass = SejongConnect()
    private let encryption = RSAEncryption(keySize: 512, privateTag: "makeKeyPair", publicTag: "makeKeyPair")
 
    
    func autoLoginClicked(auto:Bool){
        self.autoFlag = auto
    }
    
    /*
     
     */
    
    func loginClicked()->Int8{
        var pass :Int8
        
        // 아이디,패스워드 유무 판단
        if (user.id == "" || user.id == nil) {return 1}
        else if (user.pw == "" || user.pw == nil){return 2}
        
        // 회원 인증
        if user.tag == "1"{ pass = sejongPass()}
        else {pass = serverPass()}
        
        // 인증이 된 경우 자동 로그인 정보 저장
        if pass == 7 {setAutoLoginInfo()}
        
        return pass
    }
    
    func sejongPass() ->Int8 {
        var response : String?
        
        if(bypass.connectedToNetwork){
            response = bypass.requestPass(id: user.id!, pw: user.pw!)
            if response == nil {return 0}
            else if response!.contains("아이디") || response!.contains("ID") { return 1}
            else if response!.contains("패스워드") {return 2}
            else {return self.serverPass()}
            
        }else{ return 3 }
    }
    
    func serverPass() ->Int8{
        
        if socket.connecting(){
            _ = socket.sendData(string: dataConverter.getLoginData(seq: LOGIN, id: user.id!, exponent: encryption.getExponent()!, modulus: encryption.getHex()!, type: user.tag!))
            var response : String?
            while response == nil { response = socket.readResponse()}
            
            let dic = dataConverter.jsonStringToDictionary(text: response!)
            
            // setting student info
            let seq = dic["seqType"] as! String
            if seq == LOGIN_OK{
                let info = encryption.decpryptBase64(encrpted:dic["data"] as! String)
                let plainTxt = dataConverter.jsonStringToDictionary(text: info!)
                self.setUserInfo(dic : plainTxt)
                return 7
            }
            else if seq == LOGIN_ALREADY{ return 4}
            else if seq == LOGIN_NO_DATA{ return 6}
        }
        
        return 5
    }
    
    // 아이디, 패스워드, 유저타입 저장
    func setUserLoginInfo(id:String, pw :String) {
        self.user.id = id
        self.user.pw = pw
        
        if(self.user.id == "admin" && self.user.pw == "admin"){self.user.tag = "2"}
        else {self.user.tag = "1"}
    }
    
    
    func setUserInfo(dic :[String :Any]){
        
        user.name = dic["name"] as? String
        user.college = dic["college"] as? String
        user.gender = dic["gender"] as? String
        user.nationality = dic["nationality"] as? String
        user.department = dic["department"] as? String
    
    }
    
    func setAutoLoginInfo() {
        if (autoFlag == true) {
            UserDefaults.standard.set(user.id, forKey: "id")
            UserDefaults.standard.set(user.pw, forKey: "pw")
            UserDefaults.standard.set(user.tag, forKey: "tag")
            UserDefaults.standard.set(autoFlag, forKey: "auto")
            UserDefaults.standard.synchronize()
        }else{
            UserDefaults.standard.removeObject(forKey: "id")
            UserDefaults.standard.removeObject(forKey: "pw")
            UserDefaults.standard.removeObject(forKey: "tag")
            UserDefaults.standard.set(autoFlag, forKey: "auto")
            UserDefaults.standard.synchronize()
        }
    }
    
    func isAutoLogin() ->Bool {
        
        if UserDefaults.standard.object(forKey: "auto") as? Bool == true {
            user.id = UserDefaults.standard.object(forKey: "id") as? String
            user.pw = UserDefaults.standard.object(forKey:"pw") as? String
            user.tag = UserDefaults.standard.object(forKey:"tag") as? String
            self.autoFlag = true
            if loginClicked() == 7 {return true}
        }
        else {
            self.autoFlag = false
        }
        return false
    }
}
