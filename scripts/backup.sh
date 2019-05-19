#!/bin/bash
docker run --rm --link mongo:mongodb --net rmd_back_default -v /root/mongo_backup/temp:/backup mongo bash -c 'mongodump --out /backup --host mongo:27017 --authenticationDatabase admin --username root --password введипароль'
cd /root/mongo_backup
tar -cvzf "$(date '+%F').tar.gz" temp
rm -r temp