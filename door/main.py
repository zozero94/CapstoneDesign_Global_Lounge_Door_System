from socket import *
import movement as mv
import data


HOST = '192.168.123.4'
PORT = 5050
BUFSIZE = 1024
ADDR = (HOST, PORT)

client_socket = socket(AF_INET, SOCK_STREAM)
client_socket.connect(ADDR)
client_socket.send(data.toString(601))
    
m = mv.MainMovement(client_socket)
q = mv.QrMovement(client_socket)

m.start()
q.start()