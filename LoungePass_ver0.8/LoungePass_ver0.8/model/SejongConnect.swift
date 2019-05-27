//
//  SejongConnect.swift
//  LoungePass_ver0.8
//
//  Created by MacBook on 25/05/2019.
//  Copyright © 2019 LimSoYul. All rights reserved.
//

import Foundation
import SystemConfiguration

class SejongConnect{
    
    // 학교 udream의 url 반응 가져오기
    func requestPass(id: String ,pw : String)->String?{
        
        var res: String?
        let strUrl = "https://udream.sejong.ac.kr/main/loginPro.aspx" + "?rUserid=" + id + "&rPW=" + pw + "&pro=1"
        let url = URL(string: strUrl)
        if let _url = url {
            var request = URLRequest(url: _url)
            request.httpMethod = "post"
            
            let session = URLSession.shared
            let group = DispatchGroup.init()
            let queue = DispatchQueue.global()
            
            group.enter()
            queue.async {
                let task = session.dataTask(with: request, completionHandler: {(data, response, error) in
                    guard error == nil && data != nil else {
                        if let err = error { print(err.localizedDescription)}
                        return
                    }
                    if let _data = data {
                        if let strData = NSString(data: _data, encoding: String.Encoding.utf8.rawValue){
                            let str = String(strData)
                            res = str
                            group.leave()
                        }}
                    else { print("data nil") }
                })
                task.resume()
            }
            _ = group.wait(timeout: .distantFuture)
        }
        return res
    }
    
    // 네트워크 상태 확인
    var connectedToNetwork: Bool {
        
        get {
            var zeroAddress = sockaddr_in(sin_len: 0, sin_family: 0, sin_port: 0, sin_addr: in_addr(s_addr: 0), sin_zero: (0, 0, 0, 0, 0, 0, 0, 0))
            
            zeroAddress.sin_len = UInt8(MemoryLayout.size(ofValue: zeroAddress))
            zeroAddress.sin_family = sa_family_t(AF_INET)
            
            let defaultRouteReachability = withUnsafePointer(to: &zeroAddress) {
                $0.withMemoryRebound(to: sockaddr.self, capacity: 1) {zeroSockAddress in
                    SCNetworkReachabilityCreateWithAddress(nil, zeroSockAddress)
                }
            }
            var flags: SCNetworkReachabilityFlags = SCNetworkReachabilityFlags(rawValue: 0)
            if SCNetworkReachabilityGetFlags(defaultRouteReachability!, &flags) == false {
                return false
            }
            
            let isReachable = (flags.rawValue & UInt32(kSCNetworkFlagsReachable)) != 0
            let needsConnection = (flags.rawValue & UInt32(kSCNetworkFlagsConnectionRequired)) != 0
            let ret = (isReachable && !needsConnection)
            
            return ret
        }
    }
}
