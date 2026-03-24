#pragma once

#include <stdio.h>
#include <array>
#include <string>
#include "wrapper.h"


class AbilityHandData {
  public:
    AbilityHandData();
    ~AbilityHandData();
    bool isReachable() const;
    bool setPosition(std::array<float, 6> cmd) const;
    bool setTorque(std::array<float, 6> cmd) const;
    bool setDuty(std::array<float, 6> cmd) const;
  private:
    //std::string title;
    static AHWrapper wrapper;

};
