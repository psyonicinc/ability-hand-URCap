#pragma once

#include <stdio.h>
#include <array>
#include <string>
#include "wrapper.h"


class AbilityHandData {
  public:
    AbilityHandData();
    ~AbilityHandData();
    
    bool isReachable();
    bool setPosition(std::array<float, 6> cmd);
    bool setTorque(std::array<float, 6> cmd);
    bool setDuty(std::array<float, 6> cmd);
  private:
    //std::string title;
    static AHWrapper wrapper;

};
