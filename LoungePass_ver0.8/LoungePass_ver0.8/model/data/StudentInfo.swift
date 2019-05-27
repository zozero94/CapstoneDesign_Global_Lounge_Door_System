//
//  StudentInfo.swift
//  LoungePass_ver0.8
//
//  Created by MacBook on 25/05/2019.
//  Copyright © 2019 LimSoYul. All rights reserved.
//

import Foundation

class StudentInfo{
    
    var studentID :String?
    var name : String?
    var gender :String?
    var nationality :String?
    var department :String?
    var college : String?
    
    static let sharedInstance = StudentInfo()
    
    func setStudentInfo(dic : [String:Any]) {
        self.name = dic["name"] as? String
        self.college = dic["college"] as? String
        self.gender = dic["gender"] as? String
        self.studentID = dic["studentID"] as? String
        self.nationality = dic["nationality"] as? String
        self.department = dic["department"] as? String
    }
    
    
}
