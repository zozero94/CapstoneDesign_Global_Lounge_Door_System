from socket import *
from select import *

import data
import sys
import time
import ultrasonicSensor as uls


def doit(clientSocket):
    try:

        code = data.toStringQr()
        # test
        print(code)
        clientSocket.send(code.encode())
        clientSocket.send(b'\n')
        response = clientSocket.recv(1024)
        dic = data.toDict(response)
        #        time.sleep(5)

        if (dic['seqType'] == '301'):
            if (uls.a_distance(time.time())):

                clientSocket.send(data.toString(400))
                clientSocket.send(b'\n')
            else:
                clientSocket.send(data.toString(401))
                clientSocket.send(b'\n')

        else:
            return

    except Exception as e:
        # error type
        # [Errno 101] Network is unreachable
        print(e)
        exit(0)
