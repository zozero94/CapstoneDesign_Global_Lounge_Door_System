//
//  PassViewControl.swift
//  LoungePass_ver0.8
//
//  Created by MacBook on 25/05/2019.
//  Copyright © 2019 LimSoYul. All rights reserved.
//

import Foundation

class PassViewControl:ServerConstant{
    
    private let defaultInfo = UserDefaultSetting()
    private let dataConverter = ConvertData()
    private let imageGenerator = ImageGenerator()
    private let socket = ServerConnect.sharedInstance
    private let studentInfo = StudentInfo.sharedInstance
    
    
    func logoutClicked() {
        
        print("remove")

        defaultInfo.removeAll()
        socket.closing()
    }
    
    func requestData(key:String) ->Bool{
        if key == "qr"{
            return socket.sendData(string: dataConverter.getSeqData(seq: STATE_REQ))
        }else if key == "img" {
            return socket.sendData(string: dataConverter.getSeqData(seq: STATE_IMG))
        }else if key == "beacon"{
            return socket.sendData(string: dataConverter.getSeqData(seq: STATE_ADMIN))
        }
        return false
    }
    
    func getStudentInfo(key:String) -> String {
        
        switch key {
        case "name":
            return "이 름 : " + studentInfo.name!
        case "studentID":
            if studentInfo.studentID != "" {
                if (studentInfo.studentID!.contains("admin")) {
                    return "관리자 번호 : " + studentInfo.studentID!
                }
                else{
                    return "학 번 : " + studentInfo.studentID!}}
        case "department" :
            if studentInfo.department != "" {  return "학 과 : " + studentInfo.department!}
            else {return "부 서 : global Lounge"}
        case "college" :
            if  studentInfo.college != "" {  return  "단과대학 : " + studentInfo.college!}
            else{return "소 속 : 세종대학교"}
        default:
            return ""
        }
        return ""
    }
    
    func requestOpenDoor() -> Bool {
        return requestData(key: "beacon")
    }
    
    func IsResponse() ->[String :Any]?{
        
        var response : String?
        
        while response == nil { response = socket.readResponse()}
        
        print(type(of: response))
        
        var dic = dataConverter.jsonStringToDictionary(text: response!)
        
        switch dic!["seqType"] as! String {
        case STATE_CREATE : dic!["qr"] = imageGenerator.getQRCode(from: dic!["qr"] as! String)
        case STATE_URL : dic!["img"] = imageGenerator.getUrlImage(urlString: dic!["img"] as! String)
        default: break
        }
        return dic
    }
    
    
}
