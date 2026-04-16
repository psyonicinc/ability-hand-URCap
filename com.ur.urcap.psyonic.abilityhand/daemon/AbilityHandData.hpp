#pragma once

#include <stdio.h>
#include <array>
#include <string>
#include "wrapper.h"
#include <pthread.h>


class AbilityHandData {
  public:
    
    AbilityHandData();
    ~AbilityHandData();
    
    bool isReachable();
    void pushPosition();
    void pushGrip();
    bool startPositionThread();
    bool startGripThread();
    bool stopPositionThread();
    bool stopGripThread();
    bool setPosition(std::array<float, 6> cmd);
    bool setGrip(uint8_t cmd_grip, uint8_t speed);
    bool setTorque(std::array<float, 6> cmd);
    bool setDuty(std::array<float, 6> cmd);
    bool moveTillContact(std::array<float, 6> cmd);
    
  private:
    //std::string title;
    static AHWrapper wrapper;
    bool p_running = false;
    bool g_running = false;
    std::array<float, 6> m_curr_cmd;
    uint8_t m_curr_grip;
    uint8_t m_curr_speed;
    pthread_t pos_thread;
    pthread_t grip_thread;
    pthread_mutex_t mutex;

};
