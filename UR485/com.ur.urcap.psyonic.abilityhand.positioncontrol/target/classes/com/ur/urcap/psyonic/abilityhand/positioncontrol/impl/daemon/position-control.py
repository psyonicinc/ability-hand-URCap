#!/usr/bin/env python


import sys
import os

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "lib"))
import serial
from ah_wrapper import AHSerialClient

import time
# from ah_wrapper import AHSerialClient

from SimpleXMLRPCServer import SimpleXMLRPCServer
from SocketServer import ThreadingMixIn


def isReachable():
	return True


finger_positions = {
    'index': 'blank',
    'middle': 'blank',
    'ring': 'blank',
    'pinky': 'blank',
    'thumb': 'blank',
    'thumb_rot': 'blank'
}

def get_index_flex(PCV):
    finger_positions['index'] = float(PCV)
    return float(PCV)

def get_middle_flex(PCV):
    finger_positions['middle'] = float(PCV)
    return float(PCV)

def get_ring_flex(PCV):
    finger_positions['ring'] = float(PCV)
    return float(PCV)

def get_pinky_flex(PCV):
    finger_positions['pinky'] = float(PCV)
    return float(PCV)

def get_thumb_flex(PCV):
    finger_positions['thumb'] = float(PCV)
    return float(PCV)

def get_thumb_rot(PCV):
    finger_positions['thumb_rot'] = float(PCV)
    return float(PCV)

def send_motor_positions():
    client = AHSerialClient(rs_485=True)
    try:
        if finger_positions['thumb'] > any([finger_positions['index'], finger_positions['middle'], finger_positions['ring'], finger_positions['pinky']]):
            time.sleep(200/client.rate_hz)
            client.set_position(positions=[finger_positions['index'], finger_positions['middle'], finger_positions['ring'], finger_positions['pinky'], 0, 0])
            time.sleep(200/client.rate_hz)
            curr_pose = client.hand.get_position()
            time.sleep(200/client.rate_hz)
            client.set_position(positions=[finger_positions['index'], finger_positions['middle'], finger_positions['ring'], finger_positions['pinky'], finger_positions['thumb'], finger_positions['thumb_rot']])
            time.sleep(200/client.rate_hz)
        else:
            time.sleep(200/client.rate_hz)
            curr_pose = client.hand.get_position()
            client.set_position(positions=[curr_pose[0], curr_pose[1], curr_pose[2], curr_pose[3], finger_positions['thumb'], curr_pose[5]])
            time.sleep(200/client.rate_hz)
            client.set_position(positions=[curr_pose[0], curr_pose[1], curr_pose[2], curr_pose[3], finger_positions['thumb'], finger_positions['thumb_rot']])
            time.sleep(200/client.rate_hz)
            client.set_position(positions=[finger_positions["index"],finger_positions["middle"],finger_positions["ring"],finger_positions["pinky"],finger_positions["thumb"],finger_positions["thumb_rot"]])
            time.sleep(200/client.rate_hz)
    except KeyboardInterrupt:
        pass
    finally:
        client.close()

    sys.stdout.write("MyDaemon daemon1 started")
    return True


class MultithreadedSimpleXMLRPCServer(ThreadingMixIn, SimpleXMLRPCServer):
	pass

server = MultithreadedSimpleXMLRPCServer(("127.0.0.1", 60001))
server.register_function(isReachable, "isReachable")
server.RequestHandlerClass.protocol_version = "HTTP/1.1"
server.register_function(get_index_flex, "get_index_flex")
server.register_function(get_middle_flex, "get_middle_flex")
server.register_function(get_ring_flex, "get_ring_flex")
server.register_function(get_pinky_flex, "get_pinky_flex")
server.register_function(get_thumb_flex, "get_thumb_flex")
server.register_function(get_thumb_rot, "get_thumb_rot")
server.register_function(send_motor_positions, "send_motor_positions")
server.serve_forever()
