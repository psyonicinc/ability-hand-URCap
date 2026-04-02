#include <array>
#include <stdio.h>
#include <iostream>
#include <pthread.h>
#include <unistd.h>

#include "wrapper.h"
#include "AbilityHandData.hpp"


using namespace std;

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
  local_cmd =
  pthread_join(pos_thread, NULL);
  return true;
}

bool AbilityHandData::stopGripThread() {
  if (!g_running) {
        return false;
    }

  g_running = false;
  //join thread?
  pthread_join(grip_thread, NULL);
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


