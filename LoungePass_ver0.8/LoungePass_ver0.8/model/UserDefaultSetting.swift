//
//  UserDefaultSetting.swift
//  LoungePass_ver0.8
//
//  Created by MacBook on 25/05/2019.
//  Copyright © 2019 LimSoYul. All rights reserved.
//

import Foundation

open class UserDefaultSetting {
    
    func setIsAuto(value:Bool){
        UserDefaults.standard.set(value,forKey: "auto")
        UserDefaults.standard.synchronize()
    }
    
    func setUserInfo(user:UserInfo)  {
        UserDefaults.standard.set(user.id, forKey: "id")
        UserDefaults.standard.set(user.pw, forKey: "pwd")
        UserDefaults.standard.set(user.tag, forKey: "tag")
        UserDefaults.standard.synchronize()
    }
    
    func setBrightness(brightness:Float){
        UserDefaults.standard.set(brightness, forKey: "brightness")
        UserDefaults.standard.synchronize()
    }
    
    func removeAll() {
        UserDefaults.standard.removeObject(forKey: "auto")
        UserDefaults.standard.removeObject(forKey: "id")
        UserDefaults.standard.removeObject(forKey: "pwd")
        UserDefaults.standard.removeObject(forKey: "tag")
        UserDefaults.standard.synchronize()
    }

    func getInfo(key:String) -> Any? {
        return UserDefaults.standard.object(forKey: key)
    }
    
}
