#!/usr/bin/env bash

REPOSITORY=/home/ubuntu/fodongServer #프로젝트 저장 경로

APP_NAME=fodongServer
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

echo "> 현재 구동중인 애플리케이션 Pid 확인"
CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ]
then
  echo ">종료할 애플리케이션이 없습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

cd $REPOSITORY/build/libs

echo "> JAR Name : $JAR_NAME"
echo "> $JAR_NAME 실행 권한 추가"
chmod +x $JAR_NAME

nohup java -jar \
  -Dspring.config.location=classpath:/application.yml,$REPOSITORY/src/main/resources/application-real.yml \
  -Dspring.yml.active=real \
  $JAR_NAME > nohup.out 2>&1 &

