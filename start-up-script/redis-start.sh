
# REDIS_DATA_FOLDER=C:/Users/ghosh/OneDrive/Desktop/projects/github-projects/video-converter/data/redis

docker run -d \
  -h redis \
  -e REDIS_PASSWORD=I5k4kQdExcbYCTpDNQyzc6H7yQRWwuQM \
  -v C:/Users/ghosh/OneDrive/Desktop/projects/github-projects/video-converter/data/redis:/data:rw \
  -p 6379:6379 \
  --name redis \
  --restart always \
  redis:latest /bin/sh -c 'redis-server --appendonly yes --requirepass ${REDIS_PASSWORD}'