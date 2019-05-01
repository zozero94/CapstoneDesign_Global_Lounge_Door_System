import decodingQrcode
import json

def toStringQr() :
    qr = decodingQrcode.qrcode()
    jsonObject = {'seqType': '300', 'data': qr}
    outMsg = json.dumps(jsonObject)
    
    return outMsg

def toString(num) :
    jsonObject = {'seqType': num}
    outMsg = json.dumps(jsonObject)
    
    return outMsg

def toDict(inMsg) :
    inDic = json.loads(inMsg)
    
    return inDic
    