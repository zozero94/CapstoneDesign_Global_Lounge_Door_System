//
//  user.swift
//  LoungePass
//
//  Created by MacBook on 01/06/2019.
//  Copyright © 2019 LimSoYul. All rights reserved.
//

import Foundation


class User{
    
    var id : String?
    var pw : String?
    var tag : String?
    
    var name : String?
    var gender :String?
    var nationality :String?
    var department :String?
    var college : String?
    
    static let sharedInstance = User()
    
    func resetInfo() {
        self.id = nil
        self.pw = nil
        self.tag = nil
        
        self.name = nil
        self.gender = nil
        self.nationality = nil
        self.department = nil
        self.college = nil
    }
    
}
