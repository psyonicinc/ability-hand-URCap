#include <array>
#include <stdio.h>
#include <iostream>
#include <pthread.h>
#include <unistd.h>

#include "wrapper.h"
#include "AbilityHandData.hpp"

// #include <sstream>

using namespace std;

AHWrapper AbilityHandData::wrapper = AHWrapper(0x50, 921600);


AbilityHandData::AbilityHandData() {
  m_curr_cmd = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
  wrapper.connect("");
  //init mutex
  pthread_mutex_init(&mutex, NULL);
}

AbilityHandData::~AbilityHandData() {
  AbilityHandData::stopPositionThread();
  pthread_mutex_destroy(&mutex);
}

bool AbilityHandData::isReachable() {
    return true;
}

static void* threadEntry(void* arg) {
  AbilityHandData* self = static_cast<AbilityHandData*>(arg);
  self->pushPosition();
  return NULL;
}


bool AbilityHandData::startPositionThread() {
  if (running) {
        return false;
    }

  running = true;
  //start thread
  if (pthread_create(&pos_thread, NULL, threadEntry, this) != 0) {
    running = false;
    return false;
  }

  return true;
}

bool AbilityHandData::stopPositionThread() {
  if (!running) {
        return false;
    }

  running = false;
  //join thread?
  pthread_join(pos_thread, NULL);
  return true;
}


void AbilityHandData::pushPosition() {

  while (running) {

    std::array<float, 6> local_cmd;

    pthread_mutex_lock(&mutex);
    local_cmd = m_curr_cmd;
    wrapper.read_write_once(m_curr_cmd, POSITION, 0);
    pthread_mutex_unlock(&mutex);

    //add sleep to not allow race conditions
    usleep(3000); //should be 3 micro seconds (~3 cycles at br?)
  }
}

bool AbilityHandData::setPosition(std::array<float, 6> cmd) {
  //with mutex:
    pthread_mutex_lock(&mutex);
    m_curr_cmd = cmd;
    pthread_mutex_unlock(&mutex);

  return true;
}

// bool AbilityHandData::setGrip(uint8_t cmd, uint8_t speed) {

// }

bool AbilityHandData::setTorque(std::array<float, 6> cmd) {
  for (size_t i = 0; i < 2; ++i) {
    wrapper.read_write_once(cmd, CURRENT, 0);

  }
  return true;
}

bool AbilityHandData::setDuty(std::array<float, 6> cmd) {
  for (size_t i = 0; i < 2; ++i) {
    wrapper.read_write_once(cmd, DUTY, 0);

  }
  return true;
}


