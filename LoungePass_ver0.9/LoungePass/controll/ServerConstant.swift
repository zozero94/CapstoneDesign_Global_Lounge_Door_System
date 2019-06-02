//
//  ServerConstant.swift
//  LoungePass
//
//  Created by MacBook on 01/06/2019.
//  Copyright © 2019 LimSoYul. All rights reserved.
//

import Foundation

open class ServerConstant{
    
    let LOGIN = "104" //로그인 요청
    
    let LOGIN_OK = "101" //로그인 성공
    let LOGIN_ALREADY = "102" //로그인 중 (중복로그인요청)
    let LOGIN_NO_DATA = "103" //정보없음
    
    let STATE_REQ = "200" //qr요청
    let STATE_EXP = "201"
    let STATE_IMG = "203" //이미지요청
    
    let STATE_CREATE = "202"
    let STATE_URL = "204" //server -> client
    let STATE_ADMIN = "205" // 문열기 요청
    let STATE_NO = "207" // 출입문 열결 안됨
    
    let LOGOUT = "500" // 로그아웃
    
}
