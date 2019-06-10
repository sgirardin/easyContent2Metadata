#!/bin/sh

export COMPOSE_FILE_PATH=${PWD}/target/classes/docker/docker-compose.yml

if [ -z "${M2_HOME}" ]; then
  export MVN_EXEC="mvn"
else
  export MVN_EXEC="${M2_HOME}/bin/mvn"
fi

start() {
    docker volume create easyContent2Metadata-acs-volume
    docker volume create easyContent2Metadata-db-volume
    docker volume create easyContent2Metadata-ass-volume
    docker-compose -f $COMPOSE_FILE_PATH up --build -d
}

start_share() {
    docker-compose -f $COMPOSE_FILE_PATH up --build -d easyContent2Metadata-share
}

start_acs() {
    docker-compose -f $COMPOSE_FILE_PATH up --build -d easyContent2Metadata-acs
}

down() {
    docker-compose -f $COMPOSE_FILE_PATH down
}

purge() {
    docker volume rm easyContent2Metadata-acs-volume
    docker volume rm easyContent2Metadata-db-volume
    docker volume rm easyContent2Metadata-ass-volume
}

build() {
    docker rmi alfresco-content-services-easyContent2Metadata:development
    docker rmi alfresco-share-easyContent2Metadata:development
    $MVN_EXEC clean install -DskipTests=true
}

build_share() {
    docker-compose -f $COMPOSE_FILE_PATH kill easyContent2Metadata-share
    yes | docker-compose -f $COMPOSE_FILE_PATH rm -f easyContent2Metadata-share
    docker rmi alfresco-share-easyContent2Metadata:development
    $MVN_EXEC clean install -DskipTests=true -pl easyContent2Metadata-share-jar
}

build_acs() {
    docker-compose -f $COMPOSE_FILE_PATH kill easyContent2Metadata-acs
    yes | docker-compose -f $COMPOSE_FILE_PATH rm -f easyContent2Metadata-acs
    docker rmi alfresco-content-services-easyContent2Metadata:development
    $MVN_EXEC clean install -DskipTests=true -pl easyContent2Metadata-platform-jar
}

tail() {
    docker-compose -f $COMPOSE_FILE_PATH logs -f
}

tail_all() {
    docker-compose -f $COMPOSE_FILE_PATH logs --tail="all"
}

test() {
    $MVN_EXEC verify -pl integration-tests
}

case "$1" in
  build_start)
    down
    build
    start
    tail
    ;;
  start)
    start
    tail
    ;;
  stop)
    down
    ;;
  purge)
    down
    purge
    ;;
  tail)
    tail
    ;;
  reload_share)
    build_share
    start_share
    tail
    ;;
  reload_acs)
    build_acs
    start_acs
    tail
    ;;
  build_test)
    down
    build
    start
    test
    tail_all
    down
    ;;
  test)
    test
    ;;
  *)
    echo "Usage: $0 {build_start|start|stop|purge|tail|reload_share|reload_acs|build_test|test}"
esac