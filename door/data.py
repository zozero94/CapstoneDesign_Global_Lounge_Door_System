import decodingQrcode
import json

def toStringQr() :
    qr = decodingQrcode.qrcode()
    json_object = {'seqType': '300', 'data': qr}
    out_msg = json.dumps(json_object)
    
    return out_msg

def toString(num) :
    json_object = {'seqType': num}
    out_msg = json.dumps(json_object)
    
    return out_msg

def toDict(inMsg) :
    in_dic = json.loads(inMsg)
    
    return in_dic
    