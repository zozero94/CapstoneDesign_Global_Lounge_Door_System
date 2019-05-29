//
//  Convertor.swift
//  Lounge
//
//  Created by An on 16/05/2019.
//  Copyright © 2019 An. All rights reserved.
//

import Foundation

class Convertor {
    init() {}
    
    //jsonStringData -> Dictionary
    func convertStringToDic(jsonString: String) -> [String : String]{
        let jsonString = jsonString
        let jsonData = jsonString.data(using: .utf8)!
        let dictionary = try? JSONSerialization.jsonObject(with: jsonData, options: .mutableLeaves)
        
        if dictionary != nil {
            return dictionary as! [String : String]
        } else {
            return [ "" : "" ]
        }
    }
}
