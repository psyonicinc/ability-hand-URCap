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
    bool startPositionThread();
    bool stopPositionThread();
    bool setPosition(std::array<float, 6> cmd);
    bool setTorque(std::array<float, 6> cmd);
    bool setDuty(std::array<float, 6> cmd);
  private:
    //std::string title;
    static AHWrapper wrapper;
    bool running = false;
    std::array<float, 6> m_curr_cmd;
    pthread_t pos_thread;
    pthread_mutex_t mutex;

};
