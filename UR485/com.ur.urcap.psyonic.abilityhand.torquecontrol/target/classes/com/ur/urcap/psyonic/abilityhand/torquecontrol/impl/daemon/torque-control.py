#!/usr/bin/env python

import sys
import os
import time

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "lib"))
import serial
from ah_wrapper import AHSerialClient

from SimpleXMLRPCServer import SimpleXMLRPCServer
from SocketServer import ThreadingMixIn


def isReachable():
	return True


init_finger_positions = {
    'index': 0,
    'middle': 0,
    'ring': 0,
    'pinky': 0,
    'thumb': 0,
    'thumb_rot': 0
}

torque_per_finger = {
    'index': 0,
    'middle': 0,
    'ring': 0,
    'pinky': 0,
    'thumb': 0,
    'thumb_rot': 0
}



def set_init_position(index_pos, middle_pos, ring_pos, pinky_pos, thumb_pos, thumb_rot):
    init_finger_positions['index'] = float(index_pos)
    init_finger_positions['middle'] = float(middle_pos)
    init_finger_positions['ring'] = float(ring_pos)
    init_finger_positions['pinky'] = float(pinky_pos)
    init_finger_positions['thumb'] = float(thumb_pos)
    init_finger_positions['thumb_rot'] = float(thumb_rot)
    return init_finger_positions


def set_torque(index_tor, middle_tor, ring_tor, pinky_tor, thumb_tor):
    torque_per_finger['index'] = float(index_tor)
    torque_per_finger['middle'] = float(middle_tor)
    torque_per_finger['ring'] = float(ring_tor)
    torque_per_finger['pinky'] = float(pinky_tor)
    torque_per_finger['thumb'] = float(thumb_tor)
    torque_per_finger['thumb_rot'] = float(0)
    return torque_per_finger

def get_finger_dist(curr_pos, fin_pos, threshold=2):
    goal_true = [abs(curr_i - fin_i) < threshold for curr_i, fin_i in zip(curr_pos, fin_pos)]
    return goal_true


def send_motor_commands():
    keys = ['index', 'middle', 'ring', 'pinky', 'thumb', 'thumb_rot']
    init = [init_finger_positions[key] for key in keys]
    torque = [torque_per_finger[key] for key in keys]
    client = AHSerialClient(rs_485=True)
    try:
        time.sleep(1000/client.rate_hz)
        thumb_move = client.hand.get_position()
        thumb_move[-2] = 0
        client.set_position(positions=thumb_move)
        time.sleep(200/client.rate_hz)
        thumb_move[-1] = 0
        client.set_position(positions=thumb_move)
        time.sleep(200/client.rate_hz)
        client.set_position(positions=init)
        time.sleep(1000/client.rate_hz)
        client.set_torque(torque)
        time.sleep(1000/client.rate_hz)
        
    except KeyboardInterrupt:
        pass
    finally:
        client.close()
    return "True"

sys.stdout.write("MyDaemon daemon1 started")
sys.stderr.write("MyDaemon daemon2 started")

class MultithreadedSimpleXMLRPCServer(ThreadingMixIn, SimpleXMLRPCServer):
	pass

server = MultithreadedSimpleXMLRPCServer(("127.0.0.1", 60002))
server.register_function(isReachable, "isReachable")
server.RequestHandlerClass.protocol_version = "HTTP/1.1"
server.register_function(set_init_position, "set_init_position")
server.register_function(set_torque, "set_torque")
server.register_function(send_motor_commands, "send_motor_commands")
server.serve_forever()