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
    let client = TCPClient(address: "192.168.0.7", port: Int32(5050))
    var responseData : String = ""
    var flag = false
    static let sharedInstance = ServerConnection()
    
    private init(){}
    
    func sendData(data: String) -> String{
        print("data->\(data)")
        
        switch client.connect(timeout: 2) {
        case .success:
            if let response = sendRequest(string: data, using: client) {
                responseData = response
            }
        case .failure(let error):
            print(String(describing: error))
            responseData = String(describing: error)
        }
        return responseData ////// ViewController에서 if/else if/ else로 나눠서 로그인/로그인x/서버 닫혀있는지 판별 httpPost에서
    }
    
    func sendData2(data: String) -> String {
        if let response = sendRequest(string: data, using: client) {
            responseData = response
        }
        return responseData
    }
    
    private func sendRequest(string: String, using client: TCPClient) -> String? {
        print("Sending data ... ")
        switch self.client.send(string: string) {
        case .success:
            print("success")
            return readResponse(from: client)
        case .failure(let error):
            print(String(describing: error))
            return nil
        }
    }
    
    private func readResponse(from client: TCPClient) -> String? {
        print("..............")
        guard let response = client.read(1024*10,timeout: 2)
            else {
                return nil
        }
        return String(bytes: response, encoding: .utf8)
    }
    
    func close() {
        print("close()")
        
        self.client.close()
    }
}
