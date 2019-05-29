//
//  ServerConnection.swift
//  Lounge
//
//  Created by An on 07/05/2019.
//  Copyright © 2019 An. All rights reserved.
//

import Foundation
import SwiftSocket

class ServerConnection {
    let client = TCPClient(address: "192.168.123.8", port: Int32(5050))
    var responseData : String = ""
    var flag = false
    static let sharedInstance = ServerConnection()
    
    private init(){}
    
    func sendData(data: String) -> String{
        
        switch client.connect(timeout: 2) {
        case .success:
            if let response = sendRequest(string: data, using: client) {
                responseData = response
            }
        case .failure(let error):
            responseData = String(describing: error)
        }
        return responseData
    }
    
    func sendData2(data: String) -> String {
        if let response = sendRequest(string: data, using: client) {
            responseData = response
        }
        return responseData
    }
    
    private func sendRequest(string: String, using client: TCPClient) -> String? {
        switch self.client.send(string: string) {
        case .success:
            return readResponse(from: client)
        case .failure(let error):
            print(String(describing: error))
            return nil
        }
    }
    
    private func readResponse(from client: TCPClient) -> String? {
        guard let response = client.read(1024*10,timeout: 2)
            else {
                return nil
        }
        return String(bytes: response, encoding: .utf8)
    }
    
    func close() {
        self.client.close()
    }
}
