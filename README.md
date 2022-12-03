### Databus Example Project
- Ingest data with REST API and send to Kafta (spring-boot module)
- Stream processsing with Kafta Streams and Akka Streams
- Demo python script for consuming Kafka topics

### Docker commands
```bash
docker-compose up --build -d
docker-compose down -v --remove-orphans
````

### ES Memory error (WSL on Windows)
- wsl -d docker-desktop
- sysctl -w vm.max_map_count=262144
