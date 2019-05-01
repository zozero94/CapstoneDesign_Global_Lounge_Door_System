from socket import *
import socket_C
import data


HOST = '192.168.0.7'
PORT = 5050
BUFSIZE = 1024
ADDR = (HOST, PORT)

clientSocket = socket(AF_INET, SOCK_STREAM)
clientSocket.connect(ADDR)
clientSocket.send(data.toString(601))
clientSocket.send(b'\n')
    
while(1) :
    socket_C.doit(clientSocket)
    