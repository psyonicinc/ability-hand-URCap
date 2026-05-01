#include <array>
#include <vector>
#include <deque>
#include <stdio.h>
#include <iostream>
#include <pthread.h>
#include <unistd.h>

#include "wrapper.h"
#include "AbilityHandData.hpp"


using namespace std;

struct Sensor {

    double x = 0;
    double f_val = 0;
    double baseline = 0;
    double alphaB = 0.01;
    double alphaS = 0.15;

    double contactScale = 80.0;

    bool init = false;

    void addReading(uint16_t val) {
      x = static_cast<double>(val);
      if (!init) {
        init = true;
        f_val = x;
        baseline = x;
        return;
      }

      f_val = alphaS * x + (1.0 - alphaS) * f_val;
      baseline = alphaB * (f_val) + (1.0-alphaB) * baseline;
    }

    double getNormalized() {

      if (!init || contactScale <= 0.0) return 0;

      double norm = (f_val - baseline) / contactScale;

      return norm;
    }

};

AHWrapper AbilityHandData::wrapper = AHWrapper(0x50, 921600);


AbilityHandData::AbilityHandData() {
  m_curr_cmd = {30.0, 30.0, 30.0, 30.0, 30.0, 0.0};
  m_curr_grip = 0;
  m_curr_speed = 255;
  wrapper.connect("");
  //init mutex
  pthread_mutex_init(&mutex, NULL);
}

AbilityHandData::~AbilityHandData() {
  AbilityHandData::stopPositionThread();
  AbilityHandData::stopGripThread();
  pthread_mutex_destroy(&mutex);
}

bool AbilityHandData::isReachable() {
    return true;
}

static void* threadEntryPos(void* arg) {
  AbilityHandData* self = static_cast<AbilityHandData*>(arg);
  self->pushPosition();
  return NULL;
}

static void* threadEntryGrip(void* arg) {
  AbilityHandData* self = static_cast<AbilityHandData*>(arg);
  self->pushGrip();
  return NULL;
}


bool AbilityHandData::startPositionThread() {
  if (p_running || g_running) {
        return false;
    }

  p_running = true;
  //start thread
  if (pthread_create(&pos_thread, NULL, threadEntryPos, this) != 0) {
    p_running = false;
    return false;
  }

  return true;
}

bool AbilityHandData::startGripThread() {
  if (g_running || p_running) {
        return false;
    }

  g_running = true;
  //start thread
  if (pthread_create(&grip_thread, NULL, threadEntryGrip, this) != 0) {
    g_running = false;
    return false;
  }

  return true;
}

bool AbilityHandData::stopPositionThread() {
  if (!p_running) {
        return false;
    }

  p_running = false;
  //join thread?
  pthread_join(pos_thread, NULL);
  pthread_mutex_unlock(&mutex);
  return true;
}

bool AbilityHandData::stopGripThread() {
  if (!g_running) {
        return false;
    }

  g_running = false;
  //join thread?
  pthread_join(grip_thread, NULL);
  pthread_mutex_unlock(&mutex);
  return true;
}


void AbilityHandData::pushPosition() {

  std::array<float, 6> local_cmd;

  while (p_running) {

    pthread_mutex_lock(&mutex);
    local_cmd = m_curr_cmd;
    wrapper.read_write_once(local_cmd, POSITION, 0);
    pthread_mutex_unlock(&mutex);

    //add sleep to not allow race conditions
    usleep(3000); //should be 3 milliseconds
  }
}

void AbilityHandData::pushGrip() {

  uint8_t local_grip = 0;
  uint8_t local_speed = 255;

  while (g_running) {

    pthread_mutex_lock(&mutex);
    local_grip = m_curr_grip;
    local_speed = m_curr_speed;
    wrapper.read_write_once(local_grip, local_speed);
    pthread_mutex_unlock(&mutex);

    //add sleep to not allow race conditions
    usleep(3000); //should be 3 milliseconds
  }
}

bool AbilityHandData::setPosition(std::array<float, 6> cmd) {
  //with mutex:
  pthread_mutex_lock(&mutex);
  m_curr_cmd = cmd;
  pthread_mutex_unlock(&mutex);

  return true;
}

bool AbilityHandData::setGrip(uint8_t cmd_grip, uint8_t speed) {
  pthread_mutex_lock(&mutex);
  m_curr_grip = cmd_grip;
  m_curr_speed = speed;
  pthread_mutex_unlock(&mutex);

  return true;
}

bool AbilityHandData::setTorque(std::array<float, 6> cmd) {
  if (p_running || g_running) {
    return false;
  }

  for (size_t i = 0; i < 100; ++i) {
    if (p_running || g_running) {
      return false;
    }
    else {
      wrapper.read_write_once(cmd, CURRENT, 0);
    }
    
  }
  std::array<float, 6> pos;
  pos = wrapper.hand.pos;
  pthread_mutex_lock(&mutex);
  m_curr_cmd = pos;
  pthread_mutex_unlock(&mutex);

  return true;
}

bool AbilityHandData::setDuty(std::array<float, 6> cmd) {
  if (p_running || g_running) {
    return false;
  }

  
  for (size_t i = 0; i < 100; ++i) {
    if (p_running || g_running) {
      return false;
    }
    else {
      wrapper.read_write_once(cmd, DUTY, 0);
    }
    
  }
  // std::array<float, 6> pos;
  // pos = wrapper.hand.pos;
  // pthread_mutex_lock(&mutex);
  m_curr_cmd = wrapper.hand.pos;
  // pthread_mutex_unlock(&mutex);

  return true;
}

bool AbilityHandData::moveTillContact(std::array<float, 6> cmd_vel) {
  if (p_running || g_running) {
    return false;
  }

  std::array<uint16_t, 30> fsr;
  vector<Sensor> allSensors(30);
  int8_t cmd_fings = 0;
  double sum = 0;

  cmd_vel[5] = 0.0;

  for (auto &cmd : cmd_vel) {
    if (cmd >= 5.0) {
      cmd_fings += 1;
    }
    else {
      cmd = 0.0;
    }
  }

  for (size_t i=0; i < 100; ++i) {
    fsr = wrapper.hand.fsr;
    for (size_t j = 0; j < 30; ++j) {
      allSensors[j].addReading(fsr[j]);
    }
  }


  for (size_t i = 0; i < 300; ++i) {
    if (p_running || g_running) {
      return false;
    }

    wrapper.read_write_once(cmd_vel, DUTY, 0);

    fsr = wrapper.hand.fsr;
    for (size_t j = 0; j < 30; ++j) {
      allSensors[j].addReading(fsr[j]);
    }

    for (size_t j = 0; j < fsr.size(); j+=6) {
      sum = 0;
      for (size_t k = 0; k<6; k++) {
        allSensors[j+k].addReading(fsr[j+k]);
        sum += allSensors[j+k].getNormalized();

      }

      cout << sum/6 << ' ' << j/6+1 << ' ' << cmd_vel[0] << ' ' << cmd_vel[1] << ' ' << cmd_vel[2] << ' ' << cmd_vel[3] << ' ' << cmd_vel[4] << ' ' << '\n';

      if ((sum / 6) >= 0.125 && cmd_vel[j/6] != 0.0 && i > 10){
        cmd_vel[j/6] = 0.0;
        cmd_fings -= 1;
      }

    }

    if (cmd_fings == 0) {
      m_curr_cmd = wrapper.hand.pos;
      return true;
    }
  }

  return true;

}


