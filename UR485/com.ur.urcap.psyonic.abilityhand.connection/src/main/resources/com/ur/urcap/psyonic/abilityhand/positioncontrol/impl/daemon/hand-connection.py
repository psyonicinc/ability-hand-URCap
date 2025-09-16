#!/usr/bin/env python


import sys
import os

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "lib"))
import serial
from ah_wrapper import AHSerialClient

import time
import socket
import threading
import json

HOST='127.0.0.1'
PORT=12345

from SimpleXMLRPCServer import SimpleXMLRPCServer
from SocketServer import ThreadingMixIn


def isReachable():
    return True


client_connections = []
client_threads = []
server_running = True

def connect_to_hand():
    global client
    client = AHSerialClient()

def send_command_to_hand(command):
    command_decoded = list(command)
    if command_decoded[0] == "P":
        print('position')
        print(command_decoded[1:])
        client.set_position(command_decoded[1:])

    elif command_decoded[0] == "T":
        print('torque')
        print(command_decoded[1:])
        client.set_torque(command_decoded[1:])

    elif command_decoded[0] == "E":
        print('end client')
        print(command_decoded[0])
        client.close()
        shutdown_server()
    else:
        print('ERROR! INVALID INPUT')

def shutdown_server():
    print("Initiating server shutdown...")
    for conn in client_connections[:]:
        try:
            conn.close()
        except:
            pass

    try:
        server_socket.close()
        server_running = False
        print('server closed')
    except:
        pass

def start_socket_server():
    global server_socket
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    try:
        server_socket.bind((HOST, PORT))
        server_socket.listen(10) #10 queued msgs
        print('listening...')

        while server_running:
            try:
                server_socket.settimeout(1.0)
                conn, addr = server_socket.accept()
                if not server_running:
                    conn.close()
                    break
                client_connections.append(conn)
                client_thread_handler = threading.Thread(target=handle_client_message, args=(conn, addr))
                client_threads.append(client_thread_handler)
                client_thread_handler.start()
            except socket.timeout:
                continue
            except socket.error as e:
                if server_running:
                    print("Socket error!: ", e)
                break
    except socket.error as e:
        print("socket server error", e)
    finally:
        for thread in client_threads:
            thread.join(timeout=2.0)

        try:
            server_socket.close()
        except:
            pass
        print('Server completely shutdown!')


def handle_client_message(conn, addr):
    print('connection!!')
    try:
        while server_running:
            try:
                conn.settimeout(1.0)
                data = conn.recv(1024) #might change bufsize later?
                if not data:
                    print("Client disconnected", addr)
                    break
                recieved_msg = data.decode('utf-8')
                recieved_msg_json = json.loads(recieved_msg)
                send_command_to_hand(recieved_msg_json)

                if not server_running:
                    break
            except socket.timeout:
                continue
            except socket.error as e:
                if server_running:
                    print("Socket error", e)
                break
    except Exception as e:
        print("Unexpected Error", e)
    finally:
        try:
            conn.close()
        except:
            pass

        if conn in client_connections:
            client_connections.remove(conn)

sys.stderr.write("MyDaemon22 daemon started")


class MultithreadedSimpleXMLRPCServer(ThreadingMixIn, SimpleXMLRPCServer):
	pass

server = MultithreadedSimpleXMLRPCServer(("127.0.0.1", 60050))
server.register_function(isReachable, "isReachable")
server.RequestHandlerClass.protocol_version = "HTTP/1.1"
server.register_function(connect_to_hand, "connect_to_hand")
server.register_function(start_socket_server, "start_socket_server")
server.serve_forever()