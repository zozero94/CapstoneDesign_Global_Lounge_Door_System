//
//  SocketManager.swift
//  Lounge
//
//  Created by An on 07/05/2019.
//  Copyright © 2019 An. All rights reserved.
//

import Foundation
import SwiftSocket

class SocketManager {
    
    let socket : TCPClient
    let host = "219.250.232.170"
    let port = 5050
    
    class var sharedInstance : SocketManager {
        struct Singleton {
            static let instance = SocketManager()
        }
        return Singleton.instance
    }
    
    private init(){
        socket = TCPClient(address: host, port: Int32(port))
    }
    
    func onConnect() -> Bool {
        switch socket.connect(timeout: 10) {
        case .success:
            print("success")
            return true
        case .failure(let error):
            print(error)
            return false
        }
    }
    
    func sendMessage(data: String) {

    }
}
