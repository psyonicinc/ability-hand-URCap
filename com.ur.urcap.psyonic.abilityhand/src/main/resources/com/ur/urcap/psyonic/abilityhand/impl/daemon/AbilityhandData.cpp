#include <array>
#include <stdio.h>

#include "wrapper.h"
#include "AbilityHandData.hpp"

// #include <sstream>

using namespace std;

AHWrapper AbilityHandData::wrapper = AHWrapper(0x50, 921600);


AbilityHandData::AbilityHandData(): {

  wrapper.connect("");

}

AbilityHandData::~AbilityHandData() {
}

bool Data::isReachable() const {
    return true;
}

bool AbilityHandData::setPosition(std::array<float, 6> cmd) {
  for (size_t i = 0; i < 1000, ++i) {
    wrapper.read_write_once(cmd, POSITION, 0);
  }
  return true;
}

// bool AbilityHandData::setGrip(uint8_t cmd, uint8_t speed) {

// }

bool AbilityHandData::setTorque(std::array<float, 6> cmd) {
  for (size_t i = 0, i < 1000, ++i) {
    wrapper.read_write_once(cmd, CURRENT, 0);

  }
  return true;
}

bool AbilityHandData::setDuty(std::array<float, 6> cmd) {
  for (size_t i = 0, i < 1000, ++i) {
    wrapper.read_write_once(cmd, DUTY, 0);

  }
  return true;
}


